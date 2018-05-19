package com.hp.wpp.ssnclaim.restmodel.json.schema;

import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class RegisterPrinter {
	@NotNull
	@JsonProperty("version")
	private String version;

	@NotNull
	@JsonProperty("cloud_id")
	private String cloudId;
	
	@NotNull
	@JsonProperty("entity_id")
	private String entityId;
	
	@NotNull
	@JsonProperty("domain_index")
	private int domainIndex;

	@JsonIgnore
	private String deviceUUID;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

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

	public String getdeviceUUID() {
		return deviceUUID;
	}

	public void setdeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}
}
