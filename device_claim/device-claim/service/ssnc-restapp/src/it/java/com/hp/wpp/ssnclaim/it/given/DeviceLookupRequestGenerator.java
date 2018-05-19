package com.hp.wpp.ssnclaim.it.given;

import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sundharb on 9/29/2015.
 */
@Component
public class DeviceLookupRequestGenerator {

    private String claimCode;

    private String printNotificationJsonString;

    @Autowired
    private PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;

    @Autowired
    private RegistrationDomainDaoImpl registrationDomainDaoImpl;



    @Given("^I have a valid ssn \"(.*?)\"$")
    public void setValidSSN(String claimCode){
        this.claimCode = claimCode;
    }
    @Given("^I have an invalid printerCode \"(.*?)\"$")
    public void setinValidPrinterCode(String claimCode){
        this.claimCode = claimCode;
    }

    @Given("^I have an invalid printer id$")
    public String getInValidPrinterId(){
        return "invalidPrinter123";
    }

    @Given("The printer with details \"(.*?)\", \"(.*?)\" and \"(.*?)\" is registered$")
    public void createPrinterDataLookUp(String snKey, String printerId, String inkFlag) {
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, 1, Boolean.parseBoolean(inkFlag), printerId);
        printerClaimEntity.setOverRunBit(false);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);

    }

    @Given("The registration of printer with cloud Id \"(.*?)\" and serial number \"(.*?)\" is notified to device lookup")
    public void createPrinterNotification(String cloudId, String serialNum) {
        StringBuilder notificationBuilder = new StringBuilder();
        notificationBuilder.append("{ \"version\": \"1.0\",\"cloud_id\":\""+cloudId+"\",\"entity_id\":\""+serialNum+"\",\"domain_index\":1 }");
        this.printNotificationJsonString = notificationBuilder.toString();
    }

    @Then("cleanup the printer with \"(.*?)\"")
    public void cleanUpData(String snKey){
        printerDataLookUpDaoImpl.deletePrinterDataLookUpEntity(snKey);
    }

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    public String getPrintNotificationJsonString() {
        return printNotificationJsonString;
    }

    public void setPrintNotificationJsonString(String printNotificationJsonString) {
        this.printNotificationJsonString = printNotificationJsonString;
    }
}
