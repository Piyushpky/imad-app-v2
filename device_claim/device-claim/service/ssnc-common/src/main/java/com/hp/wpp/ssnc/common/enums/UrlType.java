package com.hp.wpp.ssnc.common.enums;



public enum UrlType {

	
	 SSNCLAIM("ssn_claim"),
	 SNKEYLOOKUP("sn_key_lookup"),
	 PRINTERINFO("printer_info"),
	 PRINTERINFOREGISTERED("printer_info_registered"),
	 CLOUDCONFIG("cloud_config_url"),
	 EMAIL("email_address_url"),
	 DISCOVERYTYPE("discovery_url"),
	 DEVICESTATUS("device_status_url"),
	PRINTERCODE("printer_code"),
	 DEVICEINFO("device_info_url"),
	CLAIMCODE("claim_code"),
	CLAIMCODEVALIDATION("claim_code_validation");
	
	   private final String urlType;   

	   UrlType(String urlType) {
	      this.urlType = urlType;
	    }
	    
	    public String geturlType(){
	    	return this.urlType;
	    }
	}

