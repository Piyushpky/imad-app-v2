package com.hp.wpp.avatar.framework.enums;

public enum ServiceInstanceType {

	PRINTER_CAPS("printer_caps"),

	CONNECTIVITY_CONFIG("connectivity_config"),
	
	SERVICE_CONFIG("service_config"),
	
	CREDENTIAL_REFRESH("credential_refresh");
	
	private final String type;   

	ServiceInstanceType(String value) {
      this.type = value;
    }
    
    public String getValue(){
    	return this.type;
    }
}
