package com.hp.wpp.ssnclaim.service.printercode.data;

public class PrinterCodeValidatorData {
	
	private String serialNumber;
	
	private byte[] header;
	
	private int domainIndex;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}

}
