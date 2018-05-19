package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "version", "cloud_id", "links" })

public class ResponseUUID {

    @NotNull
    @JsonProperty("version")
    protected String version;

    @NotNull
    @JsonProperty("cloud_id")
    protected String cpid;

    @NotNull
    @JsonProperty("links")
    protected List<Link> links;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public List<Link> getLinks() {
        if(links == null){
            links = new ArrayList();
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
