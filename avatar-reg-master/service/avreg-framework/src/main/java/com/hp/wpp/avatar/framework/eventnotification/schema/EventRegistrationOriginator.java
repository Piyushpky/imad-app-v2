
package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "originatorId",
    "originatorDescription"
})
public class EventRegistrationOriginator {

    @JsonProperty("originatorId")
    private String originatorId;
    @JsonProperty("originatorDescription")
    private EventRegistrationOriginatorDescription originatorDescription;


    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public EventRegistrationOriginatorDescription getOriginatorDescription() {
        return originatorDescription;
    }

    public void setOriginatorDescription(EventRegistrationOriginatorDescription originatorDescription) {
        this.originatorDescription = originatorDescription;
    }
}
