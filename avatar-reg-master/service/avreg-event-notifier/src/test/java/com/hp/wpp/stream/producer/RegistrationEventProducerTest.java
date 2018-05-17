package com.hp.wpp.stream.producer;

import com.hp.wpp.about.AboutResponse;
import com.hp.wpp.about.schema.AboutResponseCreation;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.messages.exception.MessageStreamRetriableException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by root on 3/13/17.
 */
public class RegistrationEventProducerTest {
    @Mock
    private EventProducer eventProducer;

    @Mock
    private AboutResponse aboutApiResource;

    @Mock
    private AboutResponseCreation aboutResponseCreation;

    @Mock
    private EntityIdentificationBO entityIdentificationBO;

    @Mock
    private EntityConfigurationBO entityConfigurationBO;

    @BeforeMethod
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(aboutApiResource.createResponse()).thenReturn(aboutResponseCreation);
        Mockito.when(aboutResponseCreation.getServiceName()).thenReturn("avatar_reg");
        Mockito.when(aboutResponseCreation.getServiceRevision()).thenReturn("1.3.31");
        Mockito.when(entityIdentificationBO.getEntityId()).thenReturn("PLM3674954ER");
        Mockito.when(entityIdentificationBO.getEntityModel()).thenReturn("PLM870A");
        Mockito.when(entityIdentificationBO.getEntityVersionDate()).thenReturn("2017-01-02");
        Mockito.when(entityIdentificationBO.getEntityRevision()).thenReturn("PLM1732AR");
        Mockito.when(entityIdentificationBO.getEntityType()).thenReturn("device");
        Mockito.when(entityIdentificationBO.getEntityClassifier()).thenReturn("printer");
        Mockito.when(entityIdentificationBO.getCountryAndRegionName()).thenReturn("unitedStates");
        Mockito.when(entityIdentificationBO.getLanguage()).thenReturn("english");
        Mockito.when(entityIdentificationBO.getEntityUUID()).thenReturn("ahup-63kd-2kp0-my76");
        Mockito.when(entityIdentificationBO.getResetCounter()).thenReturn(6);
        Mockito.when(entityIdentificationBO.getOriginator()).thenReturn("OOBE");
        Mockito.when(entityIdentificationBO.getPodCode()).thenReturn((short) 1);
        Mockito.when(entityIdentificationBO.getEntityMCID()).thenReturn("MFG:Hewlett-Packard;CMD:PJL,PCLXL,PCL");
        Mockito.when(entityIdentificationBO.getEntityName()).thenReturn("HPPhotoEnvy6800Series");

        Mockito.when(entityConfigurationBO.getCloudId()).thenReturn("T2HI2S3Is4SaAT3kS85Km-SlL");
    }


    @Test
    public void testRegistrationSuccessEvent() throws IOException, MessageStreamRetriableException {
        RegistrationEventProducer registrationEventProducer = Mockito.spy(new RegistrationEventProducer(eventProducer, aboutApiResource, "registration_stream"));

        Mockito.doReturn("2017-02-1712:32:00TZ").when(registrationEventProducer).getDate();
        Mockito.doReturn("172.168.1.89").when(registrationEventProducer).getHostAddress();
        registrationEventProducer.sendRegistrationEvent(entityIdentificationBO, entityConfigurationBO, HttpStatus.SC_OK, null);

        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(captor1.capture(), captor.capture());

        Assert.assertEquals(captor1.getAllValues().get(0), "registration_stream");
        Assert.assertEquals(captor.getAllValues().get(0).length > 10, true);

        String result = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("eventdata/RegistrationEventSuccess.json")).replace("\n","").replace(" ", "");
        Assert.assertEquals(new String(captor.getAllValues().get(0)), result);
    }

    @Test
    public void testRegistrationFailureEvent() throws IOException, MessageStreamRetriableException {
        RegistrationEventProducer registrationEventProducer = Mockito.spy(new RegistrationEventProducer(eventProducer, aboutApiResource, "registration_stream"));

        Mockito.doReturn("2017-02-1712:32:00TZ").when(registrationEventProducer).getDate();
        Mockito.doReturn("172.168.1.89").when(registrationEventProducer).getHostAddress();
        registrationEventProducer.sendRegistrationEvent(entityIdentificationBO, null, HttpStatus.SC_INTERNAL_SERVER_ERROR, "110001");

        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(captor1.capture(), captor.capture());

        Assert.assertEquals(captor1.getAllValues().get(0), "registration_stream");
        Assert.assertEquals(captor.getAllValues().get(0).length > 10, true);

        String result = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("eventdata/RegistrationEventFailure.json")).replace("\n","").replace(" ", "");
        Assert.assertEquals(new String(captor.getAllValues().get(0)), result);
    }
}
