package com.hp.wpp.ssnclaim.kinesis.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.amazonaws.services.kinesisanalytics.model.RecordFormatType.JSON;

/**
 * Created by kumaniti on 11/15/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AVRegAsyncEventPayload {
    private  static  final ObjectMapper JSON = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @JsonProperty("eventDetails")
    private EventRegistrationDetails eventDetails;

    public EventRegistrationDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventRegistrationDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public static AVRegAsyncEventPayload fromJsonAsBytes(byte[] bytes)  throws IOException {
        return JSON.readValue(bytes,AVRegAsyncEventPayload.class);
    }
    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }
}
