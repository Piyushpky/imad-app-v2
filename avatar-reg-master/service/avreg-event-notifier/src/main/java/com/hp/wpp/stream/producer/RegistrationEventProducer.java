package com.hp.wpp.stream.producer;

import com.hp.wpp.about.AboutResponse;
import com.hp.wpp.avatar.framework.eventnotification.schema.*;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.messages.exception.MessageStreamRetriableException;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by root on 3/13/17.
 */
public class RegistrationEventProducer implements AvregEventProducer {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RegistrationEventProducer.class);
    public static final String ORIGINATOR_NAMESPACE = "com.hp.cloud.wpp";
    public static final String SCHEMA_VERSION = "1.0.0";
    public static final String ORIGINATOR_VERSION = "1.0.0";
    public static final String REGISTRATION_EVENT_NAMESPACE = "com.hp.cloud.wpp.event.registration";
    public static final String REGISTRATION_SCHEMA_VERSION = "1.0.0";
    private EventProducer eventProducer;
    private AboutResponse aboutResponse;
    private String streamName;



    public RegistrationEventProducer(EventProducer eventProducer, AboutResponse aboutResponse, String streamName){
        this.eventProducer = eventProducer;
        this.aboutResponse = aboutResponse;
        this.streamName = streamName;
    }

     public void sendRegistrationEvent(EntityIdentificationBO entityIdentification, EntityConfigurationBO entityConfigurationBO, Integer httpStatus, String status) throws MessageStreamRetriableException {
        notifyRegistrationEvent(entityIdentification, entityConfigurationBO, httpStatus, status);
    }

    private void notifyRegistrationEvent(EntityIdentificationBO entityIdentificationBO, EntityConfigurationBO entityConfigurationBO, Integer httpStatus, String status) throws MessageStreamRetriableException {

        RegistrationEventNotificationMessage registrationEventNotificationMessage = new RegistrationEventNotificationMessage();

        registrationEventNotificationMessage.setVersion(SCHEMA_VERSION);
        EventRegistrationOriginator eventRegistrationOriginator = new EventRegistrationOriginator();
        eventRegistrationOriginator.setOriginatorId(ORIGINATOR_NAMESPACE);
        EventRegistrationOriginatorDescription eventRegistrationOriginatorDescription = new EventRegistrationOriginatorDescription();

        try {
            eventRegistrationOriginatorDescription.setHostName(getHostAddress());
        } catch (UnknownHostException e) {
            LOG.debug("Error while sending event. Message {}", e.getMessage());
        }


        try {
            eventRegistrationOriginatorDescription.setServiceName(aboutResponse.createResponse().getServiceName());
            eventRegistrationOriginatorDescription.setServiceVersion(aboutResponse.createResponse().getServiceRevision());
        }catch (IOException e) {
            LOG.debug("Error while sending event. Message {}", e.getMessage());
        }


        eventRegistrationOriginatorDescription.setVersion(ORIGINATOR_VERSION);
        eventRegistrationOriginator.setOriginatorDescription(eventRegistrationOriginatorDescription);
        registrationEventNotificationMessage.setEventOriginator(eventRegistrationOriginator);

        EventRegistrationDetails eventRegistrationDetails = new EventRegistrationDetails();

        eventRegistrationDetails.setEventCategory(REGISTRATION_EVENT_NAMESPACE);
        EventRegistrationDetailsDescription eventRegistrationDetailsDescription = new EventRegistrationDetailsDescription();
        eventRegistrationDetailsDescription.setVersion(REGISTRATION_SCHEMA_VERSION);
        if(entityConfigurationBO != null) {
            eventRegistrationDetailsDescription.setCloudID(entityConfigurationBO.getCloudId());
            if(!StringUtils.isBlank(entityConfigurationBO.getDomainIndex()))
            eventRegistrationDetailsDescription.setDomainIndex(entityConfigurationBO.getDomainIndex());
        }
        eventRegistrationDetailsDescription.setCountryAndRegionName(entityIdentificationBO.getCountryAndRegionName());
        eventRegistrationDetailsDescription.setDeviceUUID(entityIdentificationBO.getEntityUUID());
        eventRegistrationDetailsDescription.setEntityClassifier(entityIdentificationBO.getEntityClassifier());
        eventRegistrationDetailsDescription.setEntityID(entityIdentificationBO.getEntityId());
        eventRegistrationDetailsDescription.setEntityModel(entityIdentificationBO.getEntityModel());
        if(entityIdentificationBO.getHostIP()!=null){
            eventRegistrationDetailsDescription.setHostIP(entityIdentificationBO.getHostIP());
        }
        if(entityIdentificationBO.getEntityMCID()!=null)
        eventRegistrationDetailsDescription.setMcid(entityIdentificationBO.getEntityMCID());

        //model name to be populated later
        eventRegistrationDetailsDescription.setEntityModelName(entityIdentificationBO.getEntityName());
        eventRegistrationDetailsDescription.setEntityType(entityIdentificationBO.getEntityType());
        eventRegistrationDetailsDescription.setEntityVersion(entityIdentificationBO.getEntityRevision());
        eventRegistrationDetailsDescription.setEntityVersionDate(entityIdentificationBO.getEntityVersionDate());
        eventRegistrationDetailsDescription.setEventCreatedAt(getDate());
        eventRegistrationDetailsDescription.setLanguage(entityIdentificationBO.getLanguage());
        eventRegistrationDetailsDescription.setPodID(new Integer(entityIdentificationBO.getPodCode()).toString());
        eventRegistrationDetailsDescription.setResetCounter(new Integer(entityIdentificationBO.getResetCounter()).toString());
        eventRegistrationDetailsDescription.setRequestOriginator(entityIdentificationBO.getOriginator());
        eventRegistrationDetailsDescription.setResponseCode(httpStatus.toString());
        eventRegistrationDetailsDescription.setResponseErrorCode(status);

        eventRegistrationDetails.setEventRegistrationDetailsDescription(eventRegistrationDetailsDescription);

        registrationEventNotificationMessage.setEventDetails(eventRegistrationDetails);

        eventProducer.sendNotification(streamName, registrationEventNotificationMessage.toJsonAsBytes());

    }

    public String getHostAddress() throws UnknownHostException {
        Enumeration<NetworkInterface> nets = null;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new UnknownHostException();
        }
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    return  inetAddress.getHostAddress();
                }
            }
        }

        return "";
    }

    public String getDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date currentDate = new Date();
        String date =format.format(currentDate);
        return date;
    }
}
