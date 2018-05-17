package com.hp.wpp.avatar.registration.device.entities.domain;

import javax.persistence.*;

@Entity
@Table(name="registration_domain")
public class RegistrationDomain {

	@Id
	@Column(name="domain_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long domainId;
	
	@Column(name="entity_domain")
	private String entityDomain;
	
	@Column(name="domain_index")
	private int domainIndex;
	
	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public String getEntityDomain() {
		return entityDomain;
	}

	public void setEntityDomain(String entityDomain) {
		this.entityDomain = entityDomain;
	}

	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}


}
