package com.hp.wpp.ssnclaim.restmodel.json.schema;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityDiscoveryResponse {
	@JsonProperty("version")
	protected String version="1.0";
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonProperty("links")
	protected List<DiscoveryLink> links;


	public List<DiscoveryLink> getlinks() {
		 if (links == null) {
			 links = new ArrayList();
	        }
		return this.links ;
	}

}
