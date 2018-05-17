package com.hp.wpp.avatar.restmodel.json.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;





@JsonPropertyOrder({ "version", "cloud_id", "configurations"})
public class EntityConfig {

    protected String version;
    
    @JsonProperty("cloud_id")
	private String cloudId;
    
    protected List<Link> configurations;

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public List<Link> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList<Link>();
        }
        return this.configurations;
    }
	@JsonIgnore
	private String domainIndex;

	public String getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(String domainIndex) {
		this.domainIndex = domainIndex;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
	
	public static class Link {

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

}
