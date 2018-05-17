
package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eventCategory",
    "eventDescription"
})
public class EventRegistrationDetails {

    @JsonProperty("eventCategory")
    private String eventCategory;
    @JsonProperty("eventDescription")
    private EventRegistrationDetailsDescription eventRegistrationDetailsDescription;

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public EventRegistrationDetailsDescription getEventRegistrationDetailsDescription() {
        return eventRegistrationDetailsDescription;
    }

    public void setEventRegistrationDetailsDescription(EventRegistrationDetailsDescription eventRegistrationDetailsDescription) {
        this.eventRegistrationDetailsDescription = eventRegistrationDetailsDescription;
    }
}
