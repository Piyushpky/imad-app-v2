package com.hp.wpp.ssnc.common.enums;

public enum RequestType {
	
	SSNCLAIM("ssnClaim"),
	SNKEYLOOKUP("snKeyLookup");
	
	   private final String requestType;   

	RequestType(String requestType) {
	      this.requestType = requestType;
	    }
	    
	    public String getrequestType(){
	    	return this.requestType;
	    }

}
