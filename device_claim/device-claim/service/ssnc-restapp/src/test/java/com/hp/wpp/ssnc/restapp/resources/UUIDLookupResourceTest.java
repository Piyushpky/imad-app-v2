package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.ssnclaim.exception.UUIDNotFoundException;
import com.hp.wpp.ssnclaim.restapp.resources.UUIDLookupResource;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RequestUUID;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ResponseUUID;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

/**
 * Created by mvsnbharath on 19/12/2017.
 */

public class UUIDLookupResourceTest {
    @InjectMocks
    UUIDLookupResource uuidLookupResource;
    @Mock
    DeviceLookUpService service;

    @Mock
    LookUpResponseGenerator responseGenerator;

    @Mock
    RequestUUID requestUUID;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testgetcloudId() {
        Mockito.when(service.getPrinterInfo(Mockito.anyString())).thenReturn(prepareObj());
        Response response = uuidLookupResource.getcloudId(requestobj());
        Assert.assertEquals(response.getStatus(), 200);
    }
    @Test
    public void testgetcloudIdFailure() {
        Mockito.when(service.getPrinterInfo(Mockito.anyString())).thenThrow(UUIDNotFoundException.class);
        Response response = uuidLookupResource.getcloudId(requestobj());
        Assert.assertEquals(response.getStatus(), 404);
    }
    private ResponseUUID prepareObj() {
        ResponseUUID responseUUID = new ResponseUUID();
        responseUUID.setVersion("1");
        responseUUID.setCpid("cpid");
        return responseUUID;
    }
    private RequestUUID requestobj() {
        RequestUUID requestUUID = new RequestUUID();
        requestUUID.setVersion("1");
        requestUUID.setUuid("mockuuid");
        return requestUUID;
    }
}