package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class DiscoveryLink {
	@JsonProperty("rel")
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected String rel;
	
	@JsonProperty("href")
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected String href;
	
	 
    public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	
}
