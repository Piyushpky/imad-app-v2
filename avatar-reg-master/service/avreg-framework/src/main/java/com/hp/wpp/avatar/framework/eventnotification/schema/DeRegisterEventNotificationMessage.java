
package com.hp.wpp.avatar.framework.eventnotification.schema;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "subscription_id",
    "event_originator",
    "event_details"
})
public class DeRegisterEventNotificationMessage {

    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @JsonProperty("version")
    private String version;
    @JsonProperty("subscription_id")
    private String subscriptionId;
    @JsonProperty("event_originator")
    private EventOriginator eventOriginator;
    @JsonProperty("event_details")
    private EventDetails eventDetails;



    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getSubscriptionId() {
        return subscriptionId;
    }


    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }


    public EventOriginator getEventOriginator() {
        return eventOriginator;
    }


    public void setEventOriginator(EventOriginator eventOriginator) {
        this.eventOriginator = eventOriginator;
    }


    public EventDetails getEventDetails() {
        return eventDetails;
    }


    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    public static DeRegisterEventNotificationMessage fromJsonAsBytes(byte[] bytes) {
        try {
            return JSON.readValue(bytes, DeRegisterEventNotificationMessage.class);
        } catch (IOException e) {
            return null;
        }
    }

    public String toJsonAsString() {
        try {
            return JSON.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }
}
