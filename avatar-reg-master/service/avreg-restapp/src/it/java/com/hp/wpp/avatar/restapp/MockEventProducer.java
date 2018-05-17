package com.hp.wpp.avatar.restapp;

import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.messages.exception.MessageStreamRetriableException;
import org.testng.Assert;

/**
 * Created by sadaship on 2/18/2017.
 */
public class MockEventProducer implements EventProducer {

    @Override
    public void sendNotification(String streamName, byte[] bytes) {
        Assert.assertNotNull(streamName);
        Assert.assertNotNull(bytes);
    }

    @Override
    public void sendNotification(String s, String s1, byte[] bytes) throws MessageStreamRetriableException {

    }
}
