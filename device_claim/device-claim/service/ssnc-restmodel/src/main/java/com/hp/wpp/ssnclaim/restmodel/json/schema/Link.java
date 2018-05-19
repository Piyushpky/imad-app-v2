package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonPropertyOrder({"rel","href"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {

    protected String href;
    protected String rel;

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        this.href = value;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String value) {
        this.rel = value;
    }

}
