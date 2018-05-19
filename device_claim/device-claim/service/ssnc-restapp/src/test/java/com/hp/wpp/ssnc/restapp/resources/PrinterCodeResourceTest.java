
package com.hp.wpp.ssnc.restapp.resources;

import static com.jayway.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hp.wpp.ssnclaim.exception.InvalidHeaderSignatureException;
import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeException;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterCodeInfo;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.response.PrinterCodeResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.apache.commons.io.IOUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;


/**
 * Created by naikraj on 5/29/2016.
 */

public class PrinterCodeResourceTest extends BaseResourceTest {

    @Mock
    private DeviceLookUpService deviceLookUpService;

    @Mock
    private PrinterCodeResponseGenerator printerCodeResponse;

    @InjectMocks
    private PrinterCodeResource printerCodeResource;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }


/**
     * Pass the class for which you are writing this test case. This class will
     * be passed to test server for initialization.
     *
     * @return Class to be tested.
     */

    @Override
    protected Object getResourceClassToBeTested() {
        return printerCodeResource;
    }

    @Test
    public void createPrinterRecord() throws IOException {
        SSNFields ssnFields = new SSNFields();
        ssnFields.setDomainIndex(1);
        ssnFields.setInstantInkFlag(true);
        ssnFields.setSerialNumber("O0FVO4BNFF");
        ssnFields.setSsn("HAABKACUO0FVO4BNFF");

        PrinterDataEntity ssnPrinterClaimEntity = new PrinterDataEntity();
        ssnPrinterClaimEntity.setCloudId("AQAAAAFU_JJZnAAAAAFpVlpF");
        ssnPrinterClaimEntity.setInkCapable(true);
        ssnPrinterClaimEntity
                .setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        ssnPrinterClaimEntity.setDomainIndex(1);

        PrinterInfo ssnClaimInfo = new PrinterInfo();
        ssnClaimInfo.setPrinterId("AQAAAAFU_JJZnAAAAAFpVlpF");
        ssnClaimInfo.setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        ssnClaimInfo.setIsInkCapable(true);
        ssnClaimInfo.setIsPrinterRegistered(true);
        ssnClaimInfo.setClaimCode("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        Link self = new Link();
        self.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/SSN_info/HAABKACUO0FVO4BNFF");
        self.setRel("self");

        Link lookup = new Link();
        lookup.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/printer?SN_key=8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        lookup.setRel("sn_key_lookup");

        Link info = new Link();
        info.setHref("http://pie-pod1-vpdev-Pod-lb-557820310.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/info/AQAAAAFU_JJZnAAAAAFpVlpF");
        info.setRel("printer_info");

        List<Link> links = new ArrayList<>();

        links.add(self);
        links.add(lookup);
        links.add(info);
        ssnClaimInfo.setConfigurations(links);
        ssnClaimInfo.setVersion("1.0");

        when(deviceLookUpService.validateSSNCode(Mockito.anyString()))
                .thenReturn(ssnFields);
        when(
                deviceLookUpService.processPrinterCodeData(Mockito
                        .any(PrinterCodeData.class))).thenReturn(
                ssnPrinterClaimEntity);
        when(
                printerCodeResponse.createResponse(
                        Mockito.any(PrinterCodeData.class),
                        Mockito.any(PrinterDataEntity.class))).thenReturn(
                ssnClaimInfo);

        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .post(ROOT_URL + "/v1/SSN_info/" + "HAABKACUO0FVO4BNFF").then()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .and().contentType(MediaType.APPLICATION_JSON).extract()
                .response();
        org.testng.Assert.assertEquals(response.asString(),
                readFile("payload/printer_code_response.txt"));
    }

    @Test
    public void getPrinterClaimDataTest() throws IOException{
        PrinterCodeData printerFields = new PrinterCodeData();
        printerFields.setDomainIndex(1);
        printerFields.setInstantInkFlag(true);
        printerFields.setSerialNumber("O0FVO4BNFF");
        printerFields.setPrinterCode("AJK82LERYX5JD634");


        PrinterDataEntity printerClaimEntity = new PrinterDataEntity();
        printerClaimEntity.setCloudId("AQAAAAFU_JJZnAAAAAFpVlpF");
        printerClaimEntity.setInkCapable(true);
        printerClaimEntity
                .setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        printerClaimEntity.setDomainIndex(1);

        PrinterCodeInfo claimInfo = new PrinterCodeInfo();
        PrinterCodeInfo.PrevPrinterCode pcode = new PrinterCodeInfo.PrevPrinterCode();
        PrinterCodeInfo.CurrentPrinterCode ccode = new PrinterCodeInfo.CurrentPrinterCode();
        pcode.setOwnershipCounter(2);
        pcode.setIsInkCapable(true);
        pcode.setOverrunBit(false);
        claimInfo.setPrevPrinterCode(pcode);
        ccode.setOwnershipCounter(3);
        ccode.setIsInkCapable(true);
        ccode.setOverrunBit(true);
        claimInfo.setCurrentPrinterCode(ccode);
        claimInfo.setCloudId("AQAAAAFU_JJZnAAAAAFpVlpF");
        claimInfo.setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        claimInfo.setIsPrinterRegistered(true);

        Link self = new Link();
        self.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/SSN_info/HAABKACUO0FVO4BNFF");
        self.setRel("self");

        Link lookup = new Link();
        lookup.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/printer?SN_key=8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        lookup.setRel("sn_key_lookup");

        Link info = new Link();
        info.setHref("http://pie-pod1-vpdev-Pod-lb-557820310.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/info/AQAAAAFU_JJZnAAAAAFpVlpF");
        info.setRel("printer_info");

        List<Link> links = new ArrayList<>();

        links.add(self);
        links.add(lookup);
        links.add(info);
        claimInfo.setConfigurations(links);
        claimInfo.setVersion("1.0");

        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenReturn(printerFields);
        when(
                printerCodeResponse.createPrinterCodeResponse(
                        Mockito.any(PrinterCodeData.class),
                        Mockito.any(PrinterDataEntity.class))).thenReturn(claimInfo);

        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ROOT_URL + "/v1/printer_code_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .and().contentType(MediaType.APPLICATION_JSON).extract()
                .response();

        //System.out.println(response.asString());
        org.testng.Assert.assertEquals(response.asString(),
                readFile("payload/responseForGet.txt"));
    }




    @Test
    public void getPrinterClaimDataInvalidPrinterCodeExceptionTest() throws IOException{
        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenThrow(InvalidPrinterCodeException.class);


        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ROOT_URL + "/v1/printer_code_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .extract()
                .response();
        Assert.assertEquals(response.getHeaders().getValue("internal-error-code"),"AVDLS000003");

/*org.testng.Assert.assertEquals(response.asString(),
				readFile("payload/printer_code_response.txt"));*/

    }




    @Test
    public void getPrinterClaimDataDeviceClaimExceptionTest() throws IOException{

        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenThrow(InvalidPrinterCodeException.class);

        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ROOT_URL + "/v1/printer_code_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .extract()
                .response();
        Assert.assertEquals(response.getHeaders().getValue("internal-error-code"),"AVDLS000003");

/*org.testng.Assert.assertEquals(response.asString(),
				readFile("payload/printer_code_response.txt"));*/

    }

    @Test
    public void getPrinterClaimDataInvalidHeaderExceptionTest() throws IOException {


        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenThrow(InvalidHeaderSignatureException.class);


        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ROOT_URL + "/v1/printer_code_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .extract()
                .response();
        Assert.assertEquals(response.getHeaders().getValue("internal-error-code"),"AVDLS000003");

/*org.testng.Assert.assertEquals(response.asString(),
				readFile("payload/printer_code_response.txt"));*/

    }


    @Test
    public void deletePrinterClaimDataTest() throws IOException{

        PrinterCodeData printerFields = new PrinterCodeData();
        printerFields.setDomainIndex(1);
        printerFields.setInstantInkFlag(true);
        printerFields.setSerialNumber("O0FVO4BNFF");
        printerFields.setPrinterCode("AJK82LERYX5JD634");

        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenReturn(printerFields);

        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .delete(ROOT_URL + "/v1/printer_code_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .response();
    }

    @Test
    public void deletePrinterClaimDataDeviceClaimExceptionTest(){

        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenThrow(new InvalidPrinterCodeException());

        Response response=printerCodeResource.deletePrinterData("AJK82LERYX5JD634");
        Assert.assertEquals(response.getStatus(),404);
        Assert.assertEquals(response.getHeaderString("internal-error-code").toString(),"AVDLS000003");

/*org.testng.Assert.assertEquals(response.asString(),
				readFile("payload/printer_code_response.txt"));*/

    }

    @Test
    public void deletePrinterClaimDataInvalidHeaderExceptionTest()  {


        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenThrow(InvalidHeaderSignatureException.class);

       Response response=printerCodeResource.deletePrinterData("AJK82LERYX5JD634");
       Assert.assertEquals(response.getStatus(),404);
        Assert.assertEquals(response.getHeaderString("internal-error-code").toString(),"AVDLS000003");

/*org.testng.Assert.assertEquals(response.asString(),
				readFile("payload/printer_code_response.txt"));*/

    }


    @Test
    public void createPrinterCodeRecord() throws IOException {

        PrinterCodeData printerFields = new PrinterCodeData();
        printerFields.setDomainIndex(1);
        printerFields.setInstantInkFlag(true);
        printerFields.setSerialNumber("O0FVO4BNFF");
        printerFields.setPrinterCode("AJK82LERYX5JD634");

        PrinterDataEntity printerClaimEntity = new PrinterDataEntity();
        printerClaimEntity.setCloudId("AQAAAAFU_JJZnAAAAAFpVlpF");
        printerClaimEntity.setInkCapable(true);
        printerClaimEntity
                .setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        printerClaimEntity.setDomainIndex(1);

        PrinterInfo claimInfo = new PrinterInfo();
        claimInfo.setPrinterId("AQAAAAFU_JJZnAAAAAFpVlpF");
        claimInfo.setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        claimInfo.setClaimCode("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        claimInfo.setIsInkCapable(true);
        claimInfo.setIsPrinterRegistered(true);
        Link self = new Link();
        self.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/SSN_info/HAABKACUO0FVO4BNFF");
        self.setRel("self");

        Link lookup = new Link();
        lookup.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/printer?SN_key=8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        lookup.setRel("sn_key_lookup");

        Link info = new Link();
        info.setHref("http://pie-pod1-vpdev-Pod-lb-557820310.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/info/AQAAAAFU_JJZnAAAAAFpVlpF");
        info.setRel("printer_info");

        List<Link> links = new ArrayList<>();

        links.add(self);
        links.add(lookup);
        links.add(info);
        claimInfo.setConfigurations(links);
        claimInfo.setVersion("1.0");

        when(deviceLookUpService.validatePrinterCode((Mockito.anyString())))
                .thenReturn(printerFields);
        when(
                deviceLookUpService.processPrinterCodeData(Mockito
                        .any(PrinterCodeData.class))).thenReturn(
                printerClaimEntity);
        when(
                printerCodeResponse.createResponse(
                        Mockito.any(PrinterCodeData.class),
                        Mockito.any(PrinterDataEntity.class))).thenReturn(
                claimInfo);

        com.jayway.restassured.response.Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .post(ROOT_URL + "/v1/SSN_info/" + "AJK82LERYX5JD634").then()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .and().contentType(MediaType.APPLICATION_JSON).extract()
                .response();
        org.testng.Assert.assertEquals(response.asString(),
                readFile("payload/printer_code_response.txt"));
    }

    private String readFile(String path) throws IOException {
        InputStream in = PrinterCodeResource.class.getClassLoader()
                .getResourceAsStream(path);
        return IOUtils.toString(in);
    }
}
