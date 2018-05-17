
package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "source",
    "time_of_creation"
})
public class EventOriginator {

    @JsonProperty("source")
    private String source;
    @JsonProperty("time_of_creation")
    private String timeOfCreation;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(String timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

}
