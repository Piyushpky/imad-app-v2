package com.hp.wpp.avatar.framework.processor.data;

import java.util.ArrayList;
import java.util.List;

public class EntityConfigurationBO {

	private String specVersion;
	
	private String cloudId;
	
	private List<LinkBO> configurations;

	public List<LinkBO> getConfigurations() {
		if (configurations == null) {
			configurations = new ArrayList<LinkBO>();
		}
		return this.configurations;
	}

	private String domainIndex;

	public String getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(String domainIndex) {
		this.domainIndex = domainIndex;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
}