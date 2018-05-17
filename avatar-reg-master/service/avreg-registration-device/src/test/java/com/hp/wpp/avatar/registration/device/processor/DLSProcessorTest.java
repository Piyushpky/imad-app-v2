package com.hp.wpp.avatar.registration.device.processor;

import com.hp.wpp.avatar.registration.device.schema.DeviceClaim;
import com.hp.wpp.http.WppHttpClient;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class DLSProcessorTest {

    private static final String DEVICE_CLAIM_URL="https://locahost/url";
    
    private static final String DEVICE_CLAIM_AUTHENTICATION_HEADER="Basic YXZfYXZyZWcxXzExMTdfZGV2Ok9ETXdOakZrWVRFdE9HSXhOaTAwT0RabExXSXdNbUl0TjJaaU5HVTFOV0ZsTXprNA==";
    
    @Mock
    DeviceClaimHeaders deviceClaimHeaders;
    
    @Mock
    WppHttpClient wppHttpClient;

    @BeforeMethod
    public void init(){
        MockitoAnnotations.initMocks(this);
        deviceClaimHeaders=new DeviceClaimHeaders();
        deviceClaimHeaders.setDeviceClaimUrl(DEVICE_CLAIM_URL);
        deviceClaimHeaders.setDeviceClaimAuthHeader(DEVICE_CLAIM_AUTHENTICATION_HEADER);
    }

    @Test
    public void testInvokeDLSAPI() throws Exception{


        DeviceClaim deviceClaim=new DeviceClaim();
        deviceClaim.setVersion("1.0");
        deviceClaim.setCloudId("cloud_id");
        deviceClaim.setDomainIndex(0);
        deviceClaim.setEntityId("entity_id");


        DeviceClaimProcessor deviceClaimProcessor = new DeviceClaimProcessor(wppHttpClient, deviceClaim, deviceClaimHeaders);

        deviceClaimProcessor.run();
        Mockito.verify(wppHttpClient).doPut(Mockito.anyString(), (Map<String, String>) Mockito.anyObject(), (byte[]) Mockito.anyObject());
    }
}
