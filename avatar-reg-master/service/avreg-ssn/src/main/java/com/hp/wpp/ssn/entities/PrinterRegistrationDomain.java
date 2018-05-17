package com.hp.wpp.ssn.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="printer_registration_domain")
public class PrinterRegistrationDomain {

	@Id
	@Column(name="domain_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long domainId;
	
	@Column(name="registration_domain")
	private String registrationDomain;
	
	@Column(name="ssn_index")
	private int ssnIndex;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "domain_id")
	private PrinterRegistrationDomainKey domainKey;

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public String getRegistrationDomain() {
		return registrationDomain;
	}

	public void setRegistrationDomain(String registrationDomain) {
		this.registrationDomain = registrationDomain;
	}

	public int getSsnIndex() {
		return ssnIndex;
	}

	public void setSsnIndex(int ssnIndex) {
		this.ssnIndex = ssnIndex;
	}

	public PrinterRegistrationDomainKey getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(PrinterRegistrationDomainKey domainKey) {
		this.domainKey = domainKey;
	}
	
}
