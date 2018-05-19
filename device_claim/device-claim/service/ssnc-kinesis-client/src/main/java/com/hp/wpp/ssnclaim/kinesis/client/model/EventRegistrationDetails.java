package com.hp.wpp.ssnclaim.kinesis.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kumaniti on 11/15/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRegistrationDetails {

    public EventRegistrationDetailsDescription getEventRegistrationDetailsDescription() {
        return eventRegistrationDetailsDescription;
    }

    public void setEventRegistrationDetailsDescription(EventRegistrationDetailsDescription eventRegistrationDetailsDescription) {
        this.eventRegistrationDetailsDescription = eventRegistrationDetailsDescription;
    }

    @JsonProperty("eventDescription")
    private EventRegistrationDetailsDescription eventRegistrationDetailsDescription;
}
