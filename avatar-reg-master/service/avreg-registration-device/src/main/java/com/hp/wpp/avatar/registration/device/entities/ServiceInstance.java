package com.hp.wpp.avatar.registration.device.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="service_instance")
public class ServiceInstance {

	@Id
	@Column(name="service_instance_id")
	private long serviceInstanceId;
	
	@Column(name="service_type")
	private String serviceType;
	
	@Column(name = "service_url")
	private String url;
	
	@Column(name = "spec_version")
	private String specVersion;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}
}
