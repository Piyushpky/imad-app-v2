package com.hp.wpp.stream.producer;

import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.stream.messages.exception.MessageStreamRetriableException;

/**
 * Created by parsh on 3/16/2017.
 */
public interface AvregEventProducer {
    public void sendRegistrationEvent(EntityIdentificationBO entityIdentification, EntityConfigurationBO entityConfigurationBO, Integer httpStatus, String status) throws MessageStreamRetriableException;
}
