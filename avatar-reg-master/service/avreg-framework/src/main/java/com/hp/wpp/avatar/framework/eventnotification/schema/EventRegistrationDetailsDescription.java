package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;


/**
 * Created by root on 3/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "version",
        "entityID",
        "entityModel",
        "entityModelName",
        "entityVersionDate",
        "entityVersion",
        "entityType",
        "entityClassifier",
        "countryAndRegionName",
        "language",
        "domainIndex",
        "deviceUUID",
        "resetCounter",
        "requestOriginator",
        "cloudID",
        "podID",
        "mcid",
        "responseCode",
        "responseErrorCode",
        "eventCreatedAt",
        "hostIP"
})
public class EventRegistrationDetailsDescription {

    @JsonProperty("version")
    private String version;
    @JsonProperty("entityID")
    private String entityID;
    @JsonProperty("entityModel")
    private String entityModel;
    @JsonProperty("entityModelName")
    private String entityModelName;
    @JsonProperty("entityVersionDate")
    private String entityVersionDate;
    @JsonProperty("entityVersion")
    private String entityVersion;
    @JsonProperty("entityType")
    private String entityType;
    @JsonProperty("entityClassifier")
    private String entityClassifier;
    @JsonProperty("countryAndRegionName")
    private String countryAndRegionName;
    @JsonProperty("language")
    private String language;
    @JsonProperty("deviceUUID")
    private String deviceUUID;
    @JsonProperty("resetCounter")
    private String resetCounter;
    @JsonProperty("requestOriginator")
    private String requestOriginator;
    @NotNull
    @JsonProperty("cloudID")
    private String cloudID;
    @JsonProperty("podID")
    private String podID;
    @JsonProperty("mcid")
    private String mcid;
    @JsonProperty("responseCode")
    private String responseCode;

    @JsonProperty("domainIndex")
    private String domainIndex;

    @NotNull
    @JsonProperty("responseErrorCode")
    private String responseErrorCode;
    @JsonProperty("eventCreatedAt")
    private String eventCreatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("hostIP")
    private String hostIP;

    public String getDomainIndex() {
        return domainIndex;
    }

    public void setDomainIndex(String domainIndex) {
        this.domainIndex = domainIndex;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getEntityModel() {
        return entityModel;
    }

    public void setEntityModel(String entityModel) {
        this.entityModel = entityModel;
    }

    public String getEntityModelName() {
        return entityModelName;
    }

    public void setEntityModelName(String entityModelName) {
        this.entityModelName = entityModelName;
    }

    public String getEntityVersionDate() {
        return entityVersionDate;
    }

    public void setEntityVersionDate(String entityVersionDate) {
        this.entityVersionDate = entityVersionDate;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityClassifier() {
        return entityClassifier;
    }

    public void setEntityClassifier(String entityClassifier) {
        this.entityClassifier = entityClassifier;
    }

    public String getCountryAndRegionName() {
        return countryAndRegionName;
    }

    public void setCountryAndRegionName(String countryAndRegionName) {
        this.countryAndRegionName = countryAndRegionName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getMcid() {return  mcid; }

    public  void setMcid(String mcid) { this.mcid = mcid; }

    public String getResetCounter() {
        return resetCounter;
    }

    public void setResetCounter(String resetCounter) {
        this.resetCounter = resetCounter;
    }

    public String getRequestOriginator() {
        return requestOriginator;
    }

    public void setRequestOriginator(String requestOriginator) {
        this.requestOriginator = requestOriginator;
    }

    public String getCloudID() {
        return cloudID;
    }

    public void setCloudID(String cloudID) {
        this.cloudID = cloudID;
    }

    public String getPodID() {
        return podID;
    }

    public void setPodID(String podID) {
        this.podID = podID;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseErrorCode() {
        return responseErrorCode;
    }

    public void setResponseErrorCode(String responseErrorCode) {
        this.responseErrorCode = responseErrorCode;
    }

    public String getEventCreatedAt() {
        return eventCreatedAt;
    }

    public void setEventCreatedAt(String eventCreatedAt) {
        this.eventCreatedAt = eventCreatedAt;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

}