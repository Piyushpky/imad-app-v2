package com.hp.wpp.ssnclaim.kinesis.client.model;

/**
 * Created by parsh on 6/14/2017.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VPRegSyncEventPayload {

    private  static  final ObjectMapper JSON = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @JsonProperty
    protected String version;

    @JsonProperty
    private VPRegSyncEventPayload.Originator originator;

    public void setVersion(String version)
    {
        this.version=version;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Originator {

        @JsonProperty
        protected String originatorId;

        @JsonProperty
        private Originator.OriginatorDescription originatorDescription;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class OriginatorDescription {

            @JsonProperty
            protected String version;
            @JsonProperty
            protected String serviceName;
            @JsonProperty
            protected String serviceVersion;
            @JsonProperty
            protected String hostName;
            @JsonProperty
            protected String timeCreated;

            public void setOriginatorVersion(String version) {
                this.version=version;
            }

            public void setServiceName(String serviceName) {
                this.serviceName=serviceName;
            }

            public void setServiceVersion(String serviceVersion) {
                this.serviceVersion=serviceVersion;
            }

            public void setHostName(String hostName) {
                this.hostName=hostName;
            }

            public void setTimeCreated(String timeCreated) {
                this.timeCreated=timeCreated;
            }

            public String getVersion() {
                return version;
            }

            public String getServiceName() {
                return serviceName;
            }

            public String getServiceVersion() {
                return serviceVersion;
            }

            public String getHostName() {
                return hostName;
            }

            public String getTimeCreated() {
                return timeCreated;
            }
        }

        public void setOriginatorId(String originatorId){
            this.originatorId=originatorId;
        }

        public void setOriginatorDescription(VPRegSyncEventPayload.Originator.OriginatorDescription originatorDescription){
            this.originatorDescription=originatorDescription;
        }
        public String getOriginatorId() {
            return originatorId;
        }

        public OriginatorDescription getOriginatorDescription() {
            return originatorDescription;
        }

    }

    @JsonProperty
    private VPRegSyncEventPayload.EventDetails eventDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EventDetails {

        @JsonProperty
        protected String eventCategory;
        @JsonProperty
        private VPRegSyncEventPayload.EventDetails.EventDescription eventDescription;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class EventDescription {
            @JsonProperty
            protected String version;
            @JsonProperty
            protected String cloudID;
            @JsonProperty
            protected String modelNumber;
            @JsonProperty
            protected String countryAndRegionName;
            @JsonProperty
            protected String language;
            @JsonProperty
            protected String printerEmailAddress;
            @JsonProperty
            protected Boolean eprintSettings;
            @JsonProperty
            protected Boolean instantInkSettings;
            @JsonProperty
            protected Boolean mobilePrintSettings;
            @JsonProperty
            protected  int responseCode;
            @JsonProperty
            protected String responseErrorCode;
            @JsonProperty
            protected String eventCreatedAt;
            @JsonProperty
            protected Boolean sipsSettings;

            public void setEventVersion(String eventVersion) {
                this.version=eventVersion;
            }
            public void setCloudID(String cloudID) {
                this.cloudID=cloudID;
            }
            public void setModelNumber(String modelNumber) {
                this.modelNumber=modelNumber;
            }
            public void setCountryAndRegionName(String countryAndRegionName) {
                this.countryAndRegionName=countryAndRegionName;
            }
            public void setLanguage(String language) {
                this.language=language;
            }
            public void setPrinterEmailAddress(String emailAddress) {
                this.printerEmailAddress=emailAddress;
            }
            public void setEprintSettings(Boolean eprintSettings) {
                this.eprintSettings=eprintSettings;
            }
            public void setInstantInkSettings(Boolean instantInkSettings) {
                this.instantInkSettings=instantInkSettings;
            }
            public void setMobilePrintSettings(Boolean mobilePrintSettings) {
                this.mobilePrintSettings=mobilePrintSettings;
            }
            public void setResponseCode(int responseCode) {
                this.responseCode=responseCode;
            }
            public void setResponseErrorCode(String responseErrorCode) {
                this.responseErrorCode=responseErrorCode;
            }
            public void setEventCreatedAt(String eventCreatedAt) {
                this.eventCreatedAt=eventCreatedAt;
            }
            public void setSipsSettings(Boolean sipsSettings) {
                this.sipsSettings=sipsSettings;
            }

            public String getVersion() {
                return version;
            }

            public String getCloudID() {
                return cloudID;
            }

            public String getModelNumber() {
                return modelNumber;
            }

            public String getCountryAndRegionName() {
                return countryAndRegionName;
            }

            public String getLanguage() {
                return language;
            }

            public String getPrinterEmailAddress() {
                return printerEmailAddress;
            }

            public Boolean getEprintSettings() {
                return eprintSettings;
            }

            public Boolean getInstantInkSettings() {
                return instantInkSettings;
            }

            public Boolean getMobilePrintSettings() {
                return mobilePrintSettings;
            }

            public int getResponseCode() {
                return responseCode;
            }

            public String getResponseErrorCode() {
                return responseErrorCode;
            }

            public String getEventCreatedAt() {
                return eventCreatedAt;
            }

            public Boolean getSipsSettings() {
                return sipsSettings;
            }
        }

        public void setEventCategory(String eventCategory) {
            this.eventCategory=eventCategory;
        }

        public void setEventDescription(VPRegSyncEventPayload.EventDetails.EventDescription eventDescription){
            this.eventDescription=eventDescription;
        }

        public EventDescription getEventDescription() {
            return eventDescription;
        }

        public String getEventCategory() {
            return eventCategory;
        }
    }


    public void setEventDetails(VPRegSyncEventPayload.EventDetails eventDetails){
        this.eventDetails=eventDetails;
    }

    public void setOriginator(VPRegSyncEventPayload.Originator originator){
        this.originator=originator;
    }


    public String getVersion() {
        return version;
    }

    public Originator getOriginator() {
        return originator;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }
    public static VPRegSyncEventPayload fromJsonAsBytes(byte[] bytes)  throws IOException {
        return JSON.readValue(bytes,VPRegSyncEventPayload.class);
    }

    public String toJsonAsString() throws JsonProcessingException {
        return JSON.writeValueAsString(this);
    }

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }
}
