package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;
import javax.validation.constraints.NotNull;

public class RequestUUID {

    @NotNull
    @JsonProperty("version")
    protected String version;

    @NotNull
    @JsonProperty("uuid")
    protected String uuid;


    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
