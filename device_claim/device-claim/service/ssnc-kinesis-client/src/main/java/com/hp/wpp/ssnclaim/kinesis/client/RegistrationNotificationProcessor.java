package com.hp.wpp.ssnclaim.kinesis.client;

import com.hp.wpp.dataproducer.util.Operation;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;

import com.hp.wpp.ssnclaim.hystrix.command.PushToSQSCommand;
import com.hp.wpp.ssnclaim.kinesis.client.model.AVRegAsyncEventPayload;
import com.hp.wpp.ssnclaim.kinesis.client.model.EventRegistrationDetailsDescription;
import com.hp.wpp.ssnclaim.kinesis.client.model.RegNotificationPayload;
import com.hp.wpp.ssnclaim.kinesis.client.model.VPRegSyncEventPayload;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import com.hp.wpp.ssnclaim.sqshelper.SQSEventHelper;
import com.hp.wpp.streamconsumer.exception.StreamNonRetriableException;
import com.hp.wpp.streamconsumer.exception.StreamRetriableException;
import com.hp.wpp.streamconsumer.message.SingleRecordProcessor;
import com.netflix.hystrix.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by parsh on 6/14/2017.
 */
public class RegistrationNotificationProcessor implements SingleRecordProcessor {
    private static final WPPLogger LOG = WPPLoggerFactory
            .getLogger(RegistrationNotificationProcessor.class);
    private static final String STEP3_SYNC_EVENT = "com.hp.cloud.wpp.event.registration.deviceconfig";
    private static final String STEP1_ASYNC_EVENT = "com.hp.cloud.wpp.event.registration";

    @Autowired
    private PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;
    @Autowired
    private DeviceLookUpService deviceLookUpService;
    @Autowired
    private SQSEventHelper sqsEventHelper;
    @Autowired
    private PrinterResourceServiceConfig printerResourceServiceConfig;

    @Override
    public void process(byte[] data) throws StreamRetriableException {
        VPRegSyncEventPayload vpRegSyncEventPayload = null;
        AVRegAsyncEventPayload avRegAsyncEventPayload=null;
        String cloudID = null;
        if (data == null || data.length == 0) {
            LOG.warning("Received empty byte data from KPL");
        } else {
            try {
                String event = new String(data, "UTF-8");
                LOG.debug("Event data ={}",event);
                if (event.contains(STEP3_SYNC_EVENT)) {
                    vpRegSyncEventPayload = VPRegSyncEventPayload.fromJsonAsBytes(data);
                    if (vpRegSyncEventPayload == null)
                        LOG.warning("Failed to model byte data from KPL into json class");
                    else {
                        VPRegSyncEventPayload.EventDetails.EventDescription regEvent = vpRegSyncEventPayload.getEventDetails().getEventDescription();
                        if (regEvent.getResponseCode() == 200) {
                            LOG.debug("Step 3 event,Notifying RLS");
                            cloudID = regEvent.getCloudID();
                            PrinterDataEntity printerDataEntity = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByCloudID(cloudID);
                            if(printerDataEntity == null){
                                LOG.warning("Printer Data Entity not available for cloudId={}",cloudID);
                                throw new StreamRetriableException("Data not found in Dynamo DB for cloudId="+cloudID);
                            }else {
                                //Checking is registered flag to see if it was first time step3 event
                                if(!StringUtils.isBlank(printerDataEntity.getPrinterCode()) && !printerDataEntity.getIsRegistered()) {
                                    RegNotificationPayload regNotificationPayload = new RegNotificationPayload();
                                    regNotificationPayload.setCloudId(printerDataEntity.getCloudId());
                                    regNotificationPayload.setSnkey(printerDataEntity.getSnKey());
                                    regNotificationPayload.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                                    notifyDCS(regNotificationPayload);
                                }
                                printerDataEntity.setInkCapable(regEvent.getInstantInkSettings());
                                printerDataEntity.setIsRegistered(true);
                                printerDataLookUpDaoImpl.createPrinterDataLookUp(printerDataEntity);
                            }
                        }
                    }
                }
                if (event.contains(STEP1_ASYNC_EVENT) && event.contains("Avatar-Registration")  ) {
                    avRegAsyncEventPayload = AVRegAsyncEventPayload.fromJsonAsBytes(data);
                    if (avRegAsyncEventPayload == null)
                        LOG.warning("Failed to model byte data for step1 from KPL into json class");
                    else {
                        EventRegistrationDetailsDescription  regEvent = avRegAsyncEventPayload.getEventDetails().getEventRegistrationDetailsDescription();
                        if (regEvent.getResponseCode().equals("201") && !StringUtils.isBlank(regEvent.getDomainIndex())) {
                            RegisterPrinter regDeviceClaimPayload= new RegisterPrinter();
                            regDeviceClaimPayload.setVersion(regEvent.getVersion());
                            regDeviceClaimPayload.setCloudId(regEvent.getCloudID());
                            regDeviceClaimPayload.setEntityId(regEvent.getEntityID());
                            regDeviceClaimPayload.setDomainIndex(Integer.valueOf(regEvent.getDomainIndex()));
                            regDeviceClaimPayload.setdeviceUUID(regEvent.getDeviceUUID());
                            
                            LOG.info("api=printerNotificationApi , method=PUT, executionType=SyncFlow , executionState=STARTED , cloudId={}", regDeviceClaimPayload.getCloudId());
                            try {
                                deviceLookUpService.processPrinter(regDeviceClaimPayload);
                                LOG.info("api=printerNotificationApi , method=PUT, executionType=SyncFlow , executionState=COMPLETED , status=SUCCESS,  cloudId={}", regDeviceClaimPayload.getCloudId());
                            }catch (Exception e){
                                LOG.error("api=printerNotificationApi,  method=PUT, executionState=COMPLETED, cloudId={}, failureReason=\"{}\",  status=FAILURE", regDeviceClaimPayload.getCloudId(),e);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new StreamNonRetriableException("IO exception while parsing the data exception=" + e.getMessage());
            }
        }
    }

    protected void notifyDCS(RegNotificationPayload regNotificationPayload) {
        try{
            LOG.debug("Adding to Hystrix Command for cloudID={}",regNotificationPayload.getCloudId());
            HystrixCommand cmd = new PushToSQSCommand(regNotificationPayload,printerResourceServiceConfig,sqsEventHelper);
            cmd.queue();
        }catch (Exception e)
        {
            LOG.error("Unable to push to SQS for cloudID={},",e,regNotificationPayload.getCloudId());
        }
    }
}
