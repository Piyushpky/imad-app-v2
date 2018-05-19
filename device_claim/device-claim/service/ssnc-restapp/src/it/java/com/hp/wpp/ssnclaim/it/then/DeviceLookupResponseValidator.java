package com.hp.wpp.ssnclaim.it.then;

import com.hp.wpp.ssnc.common.util.JSONUtility;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by sundharb on 10/5/2015.
 */
@Component
public class DeviceLookupResponseValidator {

    private Response deviceLookupResponse;



    private Response regNotificationResponse;


    @Autowired
    private PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;

    @Then("^the device lookup response code should be (\\d+)$")
    public void validateResponseCode(int expectedStatusCode) throws IOException {
        Assert.assertEquals(expectedStatusCode,deviceLookupResponse.getStatusCode());
        System.out.println("Response ---> " + deviceLookupResponse.asString());
    }
    @Then("^the device lookup response code should be (\\d+) with header as \"(.*?)\"$")
    public void validateResponseCodeWithHeader(int expectedStatusCode,String header) throws IOException {
        Assert.assertEquals(expectedStatusCode,deviceLookupResponse.getStatusCode());
        Assert.assertEquals(header,deviceLookupResponse.getHeaders().getValue("internal-error-code"));
        System.out.println("Response ---> " + deviceLookupResponse.asString());
    }
    @Then("^the delete device lookup response code should be (\\d+)$")
    public void validateDeleteDevicelookupResponseCode(int expectedStatusCode) throws IOException {
        Assert.assertEquals(expectedStatusCode,deviceLookupResponse.getStatusCode());
        System.out.println("Response ---> " + deviceLookupResponse.asString());
    }

    @Then("^the payload should have \"(.*?)\", \"(.*?)\", \"(.*?)\" and \"(.*?)\"$")
    public void validateResponsePayload(String snKey, String isRegistered, String printerId, String inkFlag) throws IOException {
        PrinterInfo printerInfo = (PrinterInfo) JSONUtility.unmarshal(PrinterInfo.class, deviceLookupResponse.asString());
        Assert.assertEquals(snKey,printerInfo.getSnKey());
        Assert.assertEquals(Boolean.parseBoolean(isRegistered),printerInfo.getIsPrinterRegistered());
        Assert.assertEquals(Boolean.parseBoolean(inkFlag),printerInfo.getIsInkCapable());
        if(!printerId.trim().equals("")){
            //Assert printerId only in registered scenarios
            //That is only if printerId is expected in the response
            Assert.assertEquals(printerId,printerInfo.getPrinterId());
        }

    }

    @Then("^the snkey info response payload should have \"(.*?)\", \"(.*?)\", \"(.*?)\" and \"(.*?)\"$")
    public void validateSNKeyInfoResponsePayload(String snKey, String isRegistered, String printerId, String inkFlag) throws IOException {
        System.out.println(" SNKey Info Response --- " + deviceLookupResponse.asString());

        /*
        PrinterInfo printerInfo = (PrinterInfo) JSONUtility.unmarshal(PrinterInfo.class, deviceLookupResponse.asString());
        Assert.assertEquals(snKey,printerInfo.getSnKey());
        Assert.assertEquals(Boolean.parseBoolean(isRegistered),printerInfo.getIsPrinterRegistered());
        Assert.assertEquals(Boolean.parseBoolean(inkFlag),printerInfo.getIsInkCapable());
        if(!printerId.trim().equals("")){
            //Assert printerId only in registered scenarios
            //That is only if printerId is expected in the response
            Assert.assertEquals(printerId,printerInfo.getPrinterId());
        }
        */

    }

    @Then("^the \"(.*?)\" should be updated in the database with the correct \"(.*?)\"$")
    public void verifySnKeyInDatabase(String cloudId,String snKey){
        PrinterDataEntity printerDataEntity = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        if(null != printerDataEntity) {
            Assert.assertEquals(cloudId, printerDataEntity.getCloudId());
            Assert.assertEquals(snKey, printerDataEntity.getSnKey());
        }
        else{
            throw new AssertionError("SNKey not found");
        }
    }

    @Then("^the reg based response code should be (\\d+)$")
    public void postRegNotificationResponseCode(int expectedStatusCode) throws IOException {
        Assert.assertEquals(expectedStatusCode,regNotificationResponse.getStatusCode());
        System.out.println("Response ---> " + regNotificationResponse.asString());
    }

    public Response getDeviceLookupResponse() {
        return deviceLookupResponse;
    }

    public void setDeviceLookupResponse(Response deviceLookupResponse) {
        this.deviceLookupResponse = deviceLookupResponse;
    }

    public Response getRegNotificationResponse() {
        return regNotificationResponse;
    }

    public void setRegNotificationResponse(Response regNotificationResponse) {
        this.regNotificationResponse = regNotificationResponse;
    }
}
