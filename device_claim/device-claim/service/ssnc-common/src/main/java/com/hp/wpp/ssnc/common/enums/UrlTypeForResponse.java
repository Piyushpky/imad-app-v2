package com.hp.wpp.ssnc.common.enums;

public enum UrlTypeForResponse {

	SELF("self"),
	SNKEYLOOKUP("sn_key_lookup"),
	PRINTERINFO("printer_info"),
	EMAIL("email_address"),
	CLOUDCONFIG("cloud_config"),
	DISCOVERYSERVICE("discovery_entities"),
	DEVICESTATUS("device_status"),
	PRINTERCODE("printer_code"),
	DEVICEINFO("device_info"),
	CLAIMCODEVALIDATION("claim_code_validation"),
	IPP("ipp_endpoint"),
	DATABRIDGE("databridge");

	private final String urlTypeForResponse;

	UrlTypeForResponse(String urlTypeForResponse) {
		this.urlTypeForResponse = urlTypeForResponse;
	}

	public String geturlTypeForResponse(){
		return this.urlTypeForResponse;
	}

}
