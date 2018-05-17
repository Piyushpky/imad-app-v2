package com.hp.wpp.ssn.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "printer_registration_domain_key")
public class PrinterRegistrationDomainKey {

	@Id
	@Column(name="domain_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long domainId;
	
	@Column(name="registration_domain_key")
	private String registrationDomainKey;

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public String getRegistrationDomainKey() {
		return registrationDomainKey;
	}

	public void setRegistrationDomainKey(String registrationDomainKey) {
		this.registrationDomainKey = registrationDomainKey;
	}
	
	
}
