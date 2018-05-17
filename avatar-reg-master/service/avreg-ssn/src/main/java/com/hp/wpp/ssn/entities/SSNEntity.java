package com.hp.wpp.ssn.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hp.wpp.ssn.util.Utility;

@Entity
@Table(name = "signed_serial_number")
public class SSNEntity {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	
	
	@Column(name="serial_number")
	private String serialNumber;
	
	@Column(name= "issuance_counter")
	private int issuanceCounter;
	
	@Column(name="overrun_bit")
	private Boolean overrunBit; 
	
	@Column(name="domain_index")
	private int domainIndex;
	
	@Column(name= "instant_ink")
	private Boolean instantInkFlag;
	
	@Column(name="version")
	private String version;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIssuanceCounter() {
		return issuanceCounter;
	}

	public void setIssuanceCounter(int issuanceCounter) {
		this.issuanceCounter = issuanceCounter;
	}

	public Boolean getOverrunBit() {
		return overrunBit;
	}

	public void setOverrunBit(Boolean overRunBit) {
		this.overrunBit = overRunBit;
	}

	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}

	public Boolean getInstantInkFlag() {
		return instantInkFlag;
	}

	public void setInstantInkFlag(Boolean instantInkFlag) {
		this.instantInkFlag = instantInkFlag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * returns hashed/secured serial number.
	 * 
	 * @return
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = Utility.generateSHA256AndBase64(serialNumber.getBytes());
	}
	
}
