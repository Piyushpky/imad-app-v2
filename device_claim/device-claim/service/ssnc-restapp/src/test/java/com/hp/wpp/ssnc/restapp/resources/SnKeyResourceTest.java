package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.exception.CloudIdNotFoundException;
import com.hp.wpp.ssnclaim.exception.SNKeyNotFoundException;
import com.hp.wpp.ssnclaim.restapp.resources.SNKeyLookUpResource;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ClaimInfo;
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
 * Created by kumaniti on 12/1/2017.
 */
public class SnKeyResourceTest {

    @InjectMocks
    SNKeyLookUpResource snKeyLookUpResource;

    @Mock
    DeviceLookUpService service;

    @Mock
    LookUpResponseGenerator responseGenerator;

    @BeforeClass
    public void setUp() {
        snKeyLookUpResource = new SNKeyLookUpResource();
        MockitoAnnotations.initMocks(this);
    }

    public String mockSnKey = "dummysnkey";

    @Test
    public void testGetOwnership() {
        Mockito.when(service.snKeyValidation(Mockito.anyString())).thenReturn(Mockito.anyObject());
        Mockito.when(responseGenerator.createResponse(Mockito.anyObject(), true)).thenReturn(Mockito.anyObject());
        Response response = snKeyLookUpResource.getOwnership(mockSnKey);
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void testGetOwnershipSnkeyNotFound() {
        Mockito.when(service.snKeyValidation(Mockito.anyString())).thenThrow(SNKeyNotFoundException.class);
        Response response = snKeyLookUpResource.getOwnership(mockSnKey);
        Assert.assertEquals(response.getStatus(), 404);
    }

    @Test
    public void testGetClaimCodeByPrintID() {
        Mockito.when(service.createClaimResponse(Mockito.anyString())).thenReturn(prepareMockObj());
        Response response = snKeyLookUpResource.getClaimCodeByPrinterId("MockPrinterID");
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void testGetClaimCodeByPrintIDNotFound() {
        Mockito.when(service.createClaimResponse(Mockito.anyString())).thenThrow(CloudIdNotFoundException.class);
        Response response = snKeyLookUpResource.getClaimCodeByPrinterId("MockPrinterID");
        Assert.assertEquals(response.getStatus(), 404);
    }

    @Test
    public void testValidateClaimCode() {
        Mockito.when(service.getPrinterCodeData(Mockito.anyString(), Mockito.anyString())).thenReturn(prepareObj());
        Response response = snKeyLookUpResource.validateClaimCode("MockPrinterID", "MockClaimCode");
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void testValidateClaimCodeClaimMismatch() {
        Mockito.when(service.getPrinterCodeData("MockPrinterID", "MockClaimCode")).thenThrow(CloudIdNotFoundException.class);
        Response response = snKeyLookUpResource.validateClaimCode("MockPrinterID", "MockClaimCode");
        Assert.assertEquals(response.getStatus(), 404);
    }

    private PrinterDataEntity prepareObj() {
        PrinterDataEntity entity = new PrinterDataEntity();
        entity.setSnKey("mocksnkey");
        return entity;
    }

    private ClaimInfo prepareMockObj() {
        ClaimInfo info = new ClaimInfo();
        info.setClaimCode("MockCliamCode");
        return info;
    }
}
