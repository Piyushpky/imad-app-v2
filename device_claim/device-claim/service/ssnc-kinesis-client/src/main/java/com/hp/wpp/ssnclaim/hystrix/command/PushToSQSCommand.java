package com.hp.wpp.ssnclaim.hystrix.command;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.kinesis.client.model.RegNotificationPayload;
import com.hp.wpp.ssnclaim.sqshelper.SQSEventHelper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Created by karanam on 1/24/2018.
 */
public class PushToSQSCommand extends HystrixCommand<Boolean> {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PushToSQSCommand.class);

    private PrinterResourceServiceConfig printerResourceServiceConfig;
    private SQSEventHelper sqsEventHelper;
    private RegNotificationPayload regNotificationPayload;

    public PushToSQSCommand(RegNotificationPayload regNotificationPayload, PrinterResourceServiceConfig printerResourceServiceConfig, SQSEventHelper sqsEventHelper) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(printerResourceServiceConfig.getHystixKeyNameAVDLS()))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(printerResourceServiceConfig.getHystixThreadPoolCoreSizeAVDLS())
                        .withMaxQueueSize(printerResourceServiceConfig.getHystixThreadPoolMaxQueueSizeAVDLS())
                        .withQueueSizeRejectionThreshold(printerResourceServiceConfig.getHystixThreadRejectionSizeAVDLS()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(printerResourceServiceConfig.getHystixExecutionTimeOutAVDLS())
                        .withExecutionIsolationThreadInterruptOnTimeout(
                                printerResourceServiceConfig.getHystixThreadInteruptTimeOutAVDLS())
                        .withFallbackEnabled(printerResourceServiceConfig.getHystixFallbackEnabledAVDLS())));
        this.printerResourceServiceConfig = printerResourceServiceConfig;
        this.regNotificationPayload = regNotificationPayload;
        this.sqsEventHelper = sqsEventHelper;
    }

    @Override
    protected Boolean run() {
        try {
            LOG.debug("sendToSQSHystrixCommandFor CloudID={}", regNotificationPayload.getCloudId());
            sqsEventHelper.notifyDCS(regNotificationPayload);

        } catch (Throwable e) {
            LOG.error("Error processing record. CloudID={}", e, regNotificationPayload.getCloudId());
        }
        return true;
    }

    @Override
    /**
     * Stubbed fallback which ignores the failures and always returns true
     */
    protected Boolean getFallback() {
        LOG.info("[PushToSQSCommand] Fallback invoked for the cloudID={}"
                , regNotificationPayload.getCloudId());
        return true;
    }
}
