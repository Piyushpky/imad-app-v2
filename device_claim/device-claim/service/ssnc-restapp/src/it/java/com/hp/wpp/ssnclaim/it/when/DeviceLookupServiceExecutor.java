package com.hp.wpp.ssnclaim.it.when;

import com.hp.wpp.ssnclaim.it.given.DeviceLookupRequestGenerator;
import com.hp.wpp.ssnclaim.it.then.DeviceLookupResponseValidator;
import com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource;
import com.hp.wpp.ssnclaim.restapp.resources.PrinterResource;
import com.hp.wpp.ssnclaim.restapp.resources.SNKeyLookUpResource;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by sundharb on 9/30/2015.
 */
@Component
public class DeviceLookupServiceExecutor {

    @Autowired
    private DeviceLookupRequestGenerator deviceLookupRequestGenerator;

    @Autowired
    private DeviceLookupResponseValidator deviceLookupResponseValidator;

    @Autowired
    private PrinterCodeResource printerCodeResource;

    @Autowired
    private SNKeyLookUpResource snKeyLookUpResource;

    @Autowired
    private PrinterResource printerResource;

    private Response res;

    @Autowired
    protected ApplicationContext springAppContext;

    private TJWSEmbeddedJaxrsServer server;

    private static final String VALIDATE_SSN_URI = "http://localhost:9000/v1/SSN_info/";
    private static final String INFO_SNKEY = "http://localhost:9000/v1/printer?SN_key=";
    private static final String PRINTER_REGISTRATION = "http://localhost:9000/v1/notification/printer";
    private static final String PRINTERCODE__URI = "http://localhost:9000/v1/printer_code_info/";
    private static final String REG_NOTIFICATION_URI = "http://localhost:9000/v1/printer/claim_code/";


    /**
     * This method will be executed before test cases. If you are overriding this method
     * then make sure that you are calling <code>super.setup();</code> as the first line of your method.
     */
    @Before
    public void setup() {
        //RestAssured.baseURI = "http://" + avatarPrintCtfConfig.getAvatarPrintHost() + avatarPrintCtfConfig.getAvatarContext();
        //RestAssured.baseURI = "http://localhost:9000";
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(9000);
        server.start();
        server.getDeployment().getRegistry().addSingletonResource(getPrinterCodeResource());
        server.getDeployment().getRegistry().addSingletonResource(getSNKeyLookUpResource());
        server.getDeployment().getRegistry().addSingletonResource(getPrinterResource());
    }

    protected Object getPrinterCodeResource() {
        return printerCodeResource;
    }

    protected Object getSNKeyLookUpResource() {
        return snKeyLookUpResource;
    }

    protected Object getPrinterResource() {
        return printerResource;
    }

    /**
     * This method will be executed after the test case is complete. If you are overriding this method
     * then make sure that you are calling <code>super.tearDown();</code> as the first line of your method.
     */
    @After
    public void tearDown() {
        server.stop();
    }


    @When("^I call the get printerCode info api")
    public void callPrinterCodeLookup()throws IOException{
        String path=PRINTERCODE__URI+deviceLookupRequestGenerator.getClaimCode();
      try{
        res=given().when().get(path);
        deviceLookupResponseValidator.setDeviceLookupResponse(res);
          }
          catch (Exception e){
          e.printStackTrace();
              Assert.fail(e.getMessage().toString());
          }
    }


    @When("^I call the delete printerCode info api")
    public void deletePrinterCodeLookup()throws IOException{
        String path=PRINTERCODE__URI+deviceLookupRequestGenerator.getClaimCode();
        try{
            res=given().when().delete(path);
            deviceLookupResponseValidator.setDeviceLookupResponse(res);
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage().toString());
        }
    }

    @When("^I call the device claim api$")
    public void callDeviceLookup() throws IOException {
        String path = VALIDATE_SSN_URI + deviceLookupRequestGenerator.getClaimCode();
        try {
            res = given().when().post(path);
            deviceLookupResponseValidator.setDeviceLookupResponse(res);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage().toString());
        }
    }


    @When("^I call the get info by snkey api for \"(.*?)\"$")
    public void callDeviceSnKeyLookup(String snKey) throws IOException {
        String path = INFO_SNKEY + snKey;
        try {
            res = given().when().get(path);
            deviceLookupResponseValidator.setDeviceLookupResponse(res);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
    }

    @When("^I call the printer registration notification api$")
    public void callPrinterNotifcation() throws IOException {
        String path = PRINTER_REGISTRATION;
        try {
            res = given().contentType(ContentType.JSON).body(deviceLookupRequestGenerator.getPrintNotificationJsonString()).when().put(path);
            deviceLookupResponseValidator.setDeviceLookupResponse(res);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
    }

    @When("^I call the post reg notification by printerId api for \"(.*?)\"$")
    public void callRLSNotification(String printerId) throws IOException {
        String path = REG_NOTIFICATION_URI + printerId;
        try {
            res = given().when().post(path);
            deviceLookupResponseValidator.setRegNotificationResponse(res);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
    }

    @When("^I call the GET Info by printerId and claimCode api for printer \"(.*?)\" and claimcode \"(.*?)\"$")
    public void callInfoByClaimCode(String printerId,String claimCode) throws IOException {
        String path = REG_NOTIFICATION_URI + printerId+ "/"+claimCode;
        try {
            res = given().when().get(path);
            deviceLookupResponseValidator.setRegNotificationResponse(res);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
    }
}
