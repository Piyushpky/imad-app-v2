package com.hp.wpp.avatar.registration.device.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"cloud_id", "entity_id", "domain_index"})
public class DeviceClaim {

	@JsonProperty("version")
	private String version;

	@JsonProperty("cloud_id")
	private String cloudId;
	
	@JsonProperty("entity_id")
	private String entityId;
	
	@JsonProperty("domain_index")
	private int domainIndex;

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

	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
