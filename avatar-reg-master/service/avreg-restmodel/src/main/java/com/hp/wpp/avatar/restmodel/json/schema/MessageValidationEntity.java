package com.hp.wpp.avatar.restmodel.json.schema;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MessageValidationEntity {

	@NotBlank
	@JsonProperty("version")
	protected String version;

	@NotBlank
	@JsonProperty("message")
	protected String message;
	
	@NotBlank
	@JsonProperty("application_id")
	protected String applicationId;

	@NotBlank
	@JsonProperty("cloud_id")
	protected String cloudId;

	@NotBlank
	@JsonProperty("signature")
	protected String signature;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

		
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
