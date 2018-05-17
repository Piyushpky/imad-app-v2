package com.hp.wpp.avatar.restapp.mock;

import com.hp.wpp.about.AboutResponse;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.producer.AvregEventProducer;
import com.hp.wpp.stream.producer.RegistrationEventProducer;

/**
 * Created by parsh on 3/16/2017.
 */
public class RegistrationEventProducerMock  implements AvregEventProducer {
    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RegistrationEventProducer.class);
    public static final String ORIGINATOR_NAMESPACE = "com.hp.cloud.wpp";
    public static final String SCHEMA_VERSION = "1.0.0";
    public static final String ORIGINATOR_VERSION = "1.0.0";
    public static final String REGISTRATION_EVENT_NAMESPACE = "com.hp.cloud.wpp.event.registration";
    public static final String REGISTRATION_SCHEMA_VERSION = "1.0.0";
    private EventProducer eventProducer;
    private AboutResponse aboutResponse;
    private String streamName;



    public RegistrationEventProducerMock(EventProducer eventProducer, AboutResponse aboutResponse, String streamName){
        this.eventProducer = eventProducer;
        this.aboutResponse = aboutResponse;
        this.streamName = streamName;
    }

    public void sendRegistrationEvent(EntityIdentificationBO entityIdentification, EntityConfigurationBO entityConfigurationBO, Integer httpStatus, String status) {
        //notifyRegistrationEvent(entityIdentification, entityConfigurationBO, httpStatus, status);
    }

}
