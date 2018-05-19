package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;

public class DeviceClaim {
	
	@JsonProperty("cloudId")
	private String cloudId;
	
	@JsonProperty("entityId")
	private String entityId;
	
	@JsonProperty("domainIndex")
	private String domainIndex;

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(String domainIndex) {
		this.domainIndex = domainIndex;
	}

}
