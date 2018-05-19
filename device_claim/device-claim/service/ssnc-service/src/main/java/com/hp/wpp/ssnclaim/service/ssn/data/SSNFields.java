package com.hp.wpp.ssnclaim.service.ssn.data;

import javax.persistence.Column;

public class SSNFields {

	@Column(name = "serialNumber")
	private String serialNumber;

	@Column(name = "issuanceCounter")
	private int issuanceCounter;

	@Column(name = "overrunBit")
	private Boolean overrunBit;

	@Column(name = "domainIndex")
	private int domainIndex;

	@Column(name = "instantInk")
	private Boolean instantInkFlag;

	@Column(name = "version")
	private String version;

	private String ssn;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
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
		this.serialNumber = serialNumber;
	}

}
