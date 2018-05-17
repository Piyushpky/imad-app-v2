package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

import java.io.IOException;

/**
 * Created by root on 3/11/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "version",
        "originator",
        "eventDetails"
})
public class RegistrationEventNotificationMessage {
    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RegistrationEventNotificationMessage.class);

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public EventRegistrationOriginator getEventOriginator() {
        return eventOriginator;
    }

    public void setEventOriginator(EventRegistrationOriginator eventOriginator) {
        this.eventOriginator = eventOriginator;
    }

    public EventRegistrationDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventRegistrationDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    @JsonProperty("version")
    private String version;
    @JsonProperty("originator")
    private EventRegistrationOriginator eventOriginator;
    @JsonProperty("eventDetails")
    private EventRegistrationDetails eventDetails;

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }


    public static RegistrationEventNotificationMessage fromJsonAsBytes(byte[] bytes)  throws IOException {
        return JSON.readValue(bytes,RegistrationEventNotificationMessage.class);
    }

    public String toJsonAsString() throws JsonProcessingException {
        return JSON.writeValueAsString(this);
    }
}
