package com.hp.wpp.avatar.registration.device.processor;

public class DeviceClaimHeaders {
	
	private String deviceClaimUrl;
    private String deviceClaimAuthHeader;
    
    
	public String getDeviceClaimUrl() {
		return deviceClaimUrl;
	}
	public void setDeviceClaimUrl(String deviceClainUrl) {
		this.deviceClaimUrl = deviceClainUrl;
	}
	public String getDeviceClaimAuthHeader() {
		return deviceClaimAuthHeader;
	}
	public void setDeviceClaimAuthHeader(String deviceClaimAuthHeader) {
		this.deviceClaimAuthHeader = deviceClaimAuthHeader;
	}

}
