package com.hp.wpp.avatar.registration.device.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.avatar.registration.device.schema.DeviceClaim;
import com.hp.wpp.http.WppHttpClient;
import com.hp.wpp.http.WppHttpResponse;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class DeviceClaimProcessor implements Runnable {
	public WppHttpClient wppHttpClient;
	public DeviceClaim deviceClaim;
	DeviceClaimHeaders deviceClaimHeaders;

	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(DeviceClaimProcessor.class);

	public DeviceClaimProcessor(WppHttpClient wppHttpClient, DeviceClaim deviceClaim,DeviceClaimHeaders deviceClaimHeaders) {
		this.wppHttpClient = wppHttpClient;
		this.deviceClaim = deviceClaim;
		this.deviceClaimHeaders=deviceClaimHeaders;
	}

	public void updateDeviceClaimService() throws Exception {
		
		LOG.debug("api=updateDLS; method=PUT; executionType=ASyncFlow; cloudId={}; domainIndex={}; executionState=STARTED",deviceClaim.getCloudId(),deviceClaim.getDomainIndex());
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(deviceClaim);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.AUTHORIZATION, deviceClaimHeaders.getDeviceClaimAuthHeader());
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		WppHttpResponse response = wppHttpClient.doPut(deviceClaimHeaders.getDeviceClaimUrl(), headers, json.getBytes());
		
		LOG.info("api=updateDLS; method=PUT; executionType=ASyncFlow; cloudId={}; domainIndex={}; executionState=COMPLETED; statusCode={}",deviceClaim.getCloudId(),deviceClaim.getDomainIndex(), response.getStatusCode());

	}

	@Override
	public void run() {
		try {
			updateDeviceClaimService();
		} catch (Exception e) {
			//TODO retry logic to be implemented.
			LOG.error("api=updateDLS; method=PUT; executionType=ASyncFlow; cloudId={}; domainIndex={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", deviceClaim.getCloudId(),deviceClaim.getDomainIndex(), e);
		}
	}

}
