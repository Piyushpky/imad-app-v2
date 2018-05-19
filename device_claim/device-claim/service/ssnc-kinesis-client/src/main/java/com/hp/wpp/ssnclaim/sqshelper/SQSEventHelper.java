package com.hp.wpp.ssnclaim.sqshelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.dataproducer.exception.ConnectionException;
import com.hp.wpp.dataproducer.exception.InvalidParamException;
import com.hp.wpp.dataproducer.exception.QueueException;
import com.hp.wpp.dataproducer.exception.QueueNameResolutionException;
import com.hp.wpp.dataproducer.producer.SQSMessageProducer;
import com.hp.wpp.dataproducer.util.Operation;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.kinesis.client.model.RegNotificationPayload;

/**
 * Created by karanam on 1/24/2018.
 */
public class SQSEventHelper {
    private static final WPPLogger LOG = WPPLoggerFactory
            .getLogger(SQSEventHelper.class);
    private static final String key = "OPERATION";
    private static final String CREATE_OPERATION = "CREATE";

    private SQSMessageProducer sqsmessageproducer;

    public SQSEventHelper(SQSMessageProducer sqsmessageproducer){
        this.sqsmessageproducer = sqsmessageproducer;
    }

    public void notifyDCS(RegNotificationPayload regNotificationPayload){
        try {
            LOG.info("executionType=enqueueSQS; executionType=AsyncFlow;  cloudId={}; executionState=STARTED;",regNotificationPayload.getCloudId());
            sqsmessageproducer.put(createInputForSQS(regNotificationPayload), Operation.CREATE, null);
            LOG.info("executionType=enqueueSQS; executionType=AsyncFlow;  cloudId={}; executionState=COMPLETED; status=SUCCESS;",regNotificationPayload.getCloudId());
        } catch (InvalidParamException e) {
            logFailure(regNotificationPayload.getCloudId(),e.getMessage());
        } catch (ConnectionException e) {
            logFailure(regNotificationPayload.getCloudId(),e.getMessage());
        } catch (QueueException e) {
            logFailure(regNotificationPayload.getCloudId(),e.getMessage());
        } catch (QueueNameResolutionException e) {
            logFailure(regNotificationPayload.getCloudId(),e.getMessage());
        }
    }

    private void logFailure(String cloudId,String errorMessage){
        LOG.error("executionType=enqueueSQS; executionType=AsyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";",cloudId,errorMessage);
    }

    private String createInputForSQS(RegNotificationPayload regNotificationPayload){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String str = objectMapper.writeValueAsString(regNotificationPayload);
            return  str;
        } catch (JsonProcessingException e) {
            LOG.error("Exception while converting RegNotificationPayload object to String",e);
            return "";
        }
    }
}
