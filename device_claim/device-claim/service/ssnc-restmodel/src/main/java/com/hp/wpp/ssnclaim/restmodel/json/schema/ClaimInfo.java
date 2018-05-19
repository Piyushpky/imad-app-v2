package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by kumaniti on 7/12/2017.
 */
public class ClaimInfo {
    @JsonProperty("version")
    protected String version;

    @JsonProperty("claim_code")
    protected String claimCode;

    @JsonProperty("links")
    protected List<Link> configurations;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    public List<Link> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Link> configurations) {
        this.configurations = configurations;
    }


}
