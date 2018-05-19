package com.hp.wpp.ssnclaim.kinesis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.wpp.http.exception.WppHttpClientException;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.kinesis.client.model.VPRegSyncEventPayload;
import com.hp.wpp.ssnclaim.service.ssn.service.impl.DeviceLookUpServiceImpl;
import com.hp.wpp.ssnclaim.sqshelper.SQSEventHelper;
import com.hp.wpp.streamconsumer.exception.StreamRetriableException;
import org.apache.commons.io.IOUtils;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by parsh on 6/20/2017.
 */
public class RegistrationNotificationProcessorTest {

    @Spy
    @InjectMocks
    public RegistrationNotificationProcessor registrationNotificationProcessor;

    @Mock
    public PrinterDataLookUpDaoImpl printerDataLookUpDao;

    @Mock
    DeviceLookUpServiceImpl deviceLookUpService;

    @Mock
    private SQSEventHelper sqsEventHelper;

    @Mock
    private PrinterResourceServiceConfig printerResourceServiceConfig;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.doReturn(15).when(printerResourceServiceConfig).getHystixThreadPoolCoreSizeAVDLS();
        Mockito.doReturn(60000).when(printerResourceServiceConfig).getHystixExecutionTimeOutAVDLS();
        Mockito.doReturn(false).when(printerResourceServiceConfig).getHystixFallbackEnabledAVDLS();
        Mockito.doReturn("AvatarLookupService").when(printerResourceServiceConfig).getHystixKeyNameAVDLS();
        Mockito.doReturn(false).when(printerResourceServiceConfig).getHystixThreadInteruptTimeOutAVDLS();
        Mockito.doReturn(500).when(printerResourceServiceConfig).getHystixThreadRejectionSizeAVDLS();
        Mockito.doReturn(500).when(printerResourceServiceConfig).getHystixThreadPoolMaxQueueSizeAVDLS();

    }

    @Test
    public void testRLSNotification() throws IOException, StreamRetriableException, WppHttpClientException {
        Mockito.doReturn(getPrinterData()).when(printerDataLookUpDao).getPrinterDataLookUpEntityByCloudID(Mockito.anyString());
        registrationNotificationProcessor.process(prepareMockSyncEventJsonPayloadGenerator());
        Mockito.verify(printerDataLookUpDao,Mockito.times(1)).createPrinterDataLookUp(Mockito.anyObject());
    }
    @Test
    public void testStep1Notification() throws IOException, StreamRetriableException {
        registrationNotificationProcessor.process(readFile("avregStep1Payload").getBytes());
        Mockito.verify(deviceLookUpService).processPrinter(Mockito.any());
    }

    @Test
    public void testStep1NotificationNullDomainIndex() throws IOException, StreamRetriableException {
        registrationNotificationProcessor.process(readFile("avregStep1PayloadNullDomainIndex").getBytes());
        Mockito.verify(deviceLookUpService,Mockito.times(0)).processPrinter(Mockito.any());
    }

    //Test case to validate that Reg event is pushed to SQS for claim before reg events

    @Test(expectedExceptions = StreamRetriableException.class)
    public void testNotifySQSNull() throws JsonProcessingException, StreamRetriableException {
        Mockito.doReturn(null).when(printerDataLookUpDao).getPrinterDataLookUpEntityByCloudID(Mockito.anyString());
        registrationNotificationProcessor.process(prepareMockSyncEventJsonPayloadGenerator());
    }

    @Test
    public void testNotifySQSValid() throws JsonProcessingException, StreamRetriableException {
        Mockito.doReturn(getPrinterDataWithPrinterCode()).when(printerDataLookUpDao).getPrinterDataLookUpEntityByCloudID(Mockito.anyString());
        Mockito.doReturn(getPrinterDataWithPrinterCode()).when(printerDataLookUpDao).createPrinterDataLookUp(Mockito.anyObject());
        registrationNotificationProcessor.process(prepareMockSyncEventJsonPayloadGenerator());
        Mockito.verify(registrationNotificationProcessor,Mockito.times(1)).notifyDCS(Mockito.anyObject());
    }

    @Test
    public void testNotifySQSInvalid() throws JsonProcessingException, StreamRetriableException {
        Mockito.doReturn(getPrinterData()).when(printerDataLookUpDao).getPrinterDataLookUpEntityByCloudID(Mockito.anyString());
        Mockito.doReturn(getPrinterData()).when(printerDataLookUpDao).createPrinterDataLookUp(Mockito.anyObject());
        registrationNotificationProcessor.process(prepareMockSyncEventJsonPayloadGenerator());
        Mockito.verify(registrationNotificationProcessor,Mockito.times(0)).notifyDCS(Mockito.anyObject());
    }

    @Test
    public void testNotifySQSValidDuplicate() throws JsonProcessingException, StreamRetriableException {
        Mockito.doReturn(getPrinterDataDuplicate()).when(printerDataLookUpDao).getPrinterDataLookUpEntityByCloudID(Mockito.anyString());
        Mockito.doReturn(getPrinterDataDuplicate()).when(printerDataLookUpDao).createPrinterDataLookUp(Mockito.anyObject());
        registrationNotificationProcessor.process(prepareMockSyncEventJsonPayloadGenerator());
        Mockito.verify(registrationNotificationProcessor,Mockito.times(0)).notifyDCS(Mockito.anyObject());
    }

    private PrinterDataEntity getPrinterDataDuplicate() {
        String snKey="qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M=";
        int domainIndex=0;
        Boolean isInkCapable=false;
        String printerId="AQAAAAFUgHLcnwAAGR58veve";
        String printerCode = "mockPrinterCode";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerClaimEntity.setPrinterCode(printerCode);
        printerClaimEntity.setIsRegistered(true);
        return printerClaimEntity;
    }

    private byte[] prepareMockSyncEventJsonPayloadGenerator() throws JsonProcessingException {
        VPRegSyncEventPayload syncEventPayload = new VPRegSyncEventPayload();
        VPRegSyncEventPayload.Originator.OriginatorDescription originatorDescription = new VPRegSyncEventPayload.Originator.OriginatorDescription();
        VPRegSyncEventPayload.EventDetails.EventDescription eventDescription = new VPRegSyncEventPayload.EventDetails.EventDescription();
        VPRegSyncEventPayload.Originator originator = new VPRegSyncEventPayload.Originator();
        syncEventPayload.setVersion("1.0.0");
        originator.setOriginatorId("com.hp.cloud.wpp");
        originatorDescription.setHostName("1.2.3.4");
        originatorDescription.setOriginatorVersion("1.0.0");
        originatorDescription.setServiceName("VP-Registration");
        originatorDescription.setServiceVersion("1.0.12");


        originatorDescription.setTimeCreated("xyz");

        originator.setOriginatorDescription(originatorDescription);
        VPRegSyncEventPayload.EventDetails eventDetails = new VPRegSyncEventPayload.EventDetails();

        eventDetails.setEventCategory("com.hp.cloud.wpp.event.registration.deviceconfig");


        eventDescription.setEventVersion("!.0.0");//eventDescription.setEventVersion(cloudConfiguration.getVersion());
        eventDescription.setEprintSettings(true);

        eventDescription.setInstantInkSettings(true);
        eventDescription.setMobilePrintSettings(true);
        eventDescription.setSipsSettings(true);
        eventDescription.setModelNumber("XQWE");
        eventDescription.setCloudID("123445");
        eventDescription.setCountryAndRegionName("in");
        eventDescription.setLanguage("en");
        eventDescription.setPrinterEmailAddress("abcd@hp.com");
        eventDescription.setEventCreatedAt("123");
        eventDescription.setResponseCode(200);
        eventDescription.setResponseErrorCode(null);
        originator.setOriginatorDescription(originatorDescription);
        eventDetails.setEventDescription(eventDescription);
        syncEventPayload.setEventDetails(eventDetails);
        syncEventPayload.setOriginator(originator);
        return syncEventPayload.toJsonAsBytes();
    }

    public PrinterDataEntity getPrinterData(){
        String snKey="qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex=0;
        Boolean isInkCapable=false;
        String printerId="AQAAAAFUgHLcnwAAAAGR58ve";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        return printerClaimEntity;
    }

    public PrinterDataEntity getPrinterDataWithPrinterCode(){
        String snKey="qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M=";
        int domainIndex=0;
        Boolean isInkCapable=false;
        String printerId="AQAAAAFUgHLcnwAAGR58veve";
        String printerCode = "mockPrinterCode";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerClaimEntity.setPrinterCode(printerCode);
        printerClaimEntity.setIsRegistered(false);
        return printerClaimEntity;
    }

    private String readFile(String path) throws IOException {
        InputStream in = RegistrationNotificationProcessor.class.getClassLoader()
                .getResourceAsStream(path);
        return IOUtils.toString(in);
    }
}
