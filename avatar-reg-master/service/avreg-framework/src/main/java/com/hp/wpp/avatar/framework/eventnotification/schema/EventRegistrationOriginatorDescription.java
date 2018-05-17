package com.hp.wpp.avatar.framework.eventnotification.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by root on 3/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "version",
        "serviceName",
        "serviceVersion",
        "hostName"
})
public class EventRegistrationOriginatorDescription {
    @JsonProperty("version")
    private String version;
    @JsonProperty("serviceName")
    private String serviceName;
    @JsonProperty("serviceVersion")
    private String serviceVersion;
    @JsonProperty("hostName")
    private String hostName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
