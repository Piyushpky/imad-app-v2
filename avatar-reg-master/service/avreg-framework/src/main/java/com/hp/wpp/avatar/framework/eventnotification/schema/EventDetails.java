
package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "event_name",
    "cloud_id",
    "model_number"
})
public class EventDetails {

    @JsonProperty("event_name")
    private String eventName;
    @JsonProperty("entity_id")
    private String entityId;
    @JsonProperty("model_number")
    private String modelNumber;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

}
