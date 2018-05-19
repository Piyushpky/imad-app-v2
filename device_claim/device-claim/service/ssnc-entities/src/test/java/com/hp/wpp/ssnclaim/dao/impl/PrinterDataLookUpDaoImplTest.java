package com.hp.wpp.ssnclaim.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hp.wpp.mock.dynamodb.http.DynamoDBHttpMockService;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;

@ContextConfiguration(locations =
        {
                "classpath:/dynamodb-mock-applicationContext-test.xml"
        })
public class PrinterDataLookUpDaoImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DynamoDBHttpMockService dynamoDBHttpMockService;

    @Autowired
    private PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;
    /*   @Autowired
       private AmazonDynamoDB dynamoDB ;
     */
    @Autowired
    private DynamoDBMapper dynamoDBMapper;


    @BeforeClass
    public void setup() {
        dynamoDBHttpMockService.start();
        // dynamoDB = DynamoDBEmbedded.create();
        //Create tables as many as required
        List<Class> classes = new ArrayList<>();
        classes.add(PrinterDataEntity.class);

        dynamoDBHttpMockService.createTable(classes);
        //   CreateTableRequest createTableRequest=dynamoDBMapper.generateCreateTableRequest(PrinterDataEntity.class);
        //	dynamoDB.createTable(createTableRequest);
    }

    @AfterClass
    public void teardown() {
        //Stop mock service
        dynamoDBHttpMockService.stop();
    }

    @Test
    public void createPrinterDataLookUp() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String cloudId = "AQAAAAFUgHLcnwAAAAGR58ve";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, cloudId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        assert (actual.getCloudId().equals(printerClaimEntity.getCloudId()));
    }

    @Test
    public void getPrinterDataLookUpEntity() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String printerId = "AQAAAAFUgHLcnwAAAAGR58ve";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        assert (printerClaimEntity.getCloudId().equals("AQAAAAFUgHLcnwAAAAGR58ve"));

    }

    @Test
    public void getPrinterDataLookUpEntityByCloudId() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String printerId = "AQAAAAFUgHLcnwAAAAGR58ve";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByCloudID(printerId);
        assert (actual.getSnKey().equals(snKey));
    }

    @Test
    public void getPrinterDataLookUpEntityClaimedPrinter() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String printerId = null;

        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        assert (actual.getIsRegistered().equals(false));
    }

    @Test
    public void getPrinterDataLookUpEntityByCloudIdForAlreadyRegistredPrinter() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8M";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String printerId = "AQAAAAFUgHLcnwAAAAGR58ve";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, printerId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByCloudID(printerId);
        assert (actual.getSnKey().equals(snKey));
        assert (actual.getIsRegistered().equals(true));
    }

    @Test
    public void deletePrinterDataLookUp() {
        String snKey = "qPn3QgJPKNN3xrk9EmGYhAno3feJyGGd-U6p1j6Dm8N";
        int domainIndex = 0;
        Boolean isInkCapable = false;
        String cloudId = "AQAAAAFUgHLcnwAAAAGR58vf";
        PrinterDataEntity printerClaimEntity = new PrinterDataEntity(snKey, domainIndex, isInkCapable, cloudId);
        printerDataLookUpDaoImpl.createPrinterDataLookUp(printerClaimEntity);
        PrinterDataEntity actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        assert (actual.getCloudId().equals(printerClaimEntity.getCloudId()));
        printerDataLookUpDaoImpl.deletePrinterDataLookUpEntity(actual);
        actual = printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(snKey);
        assert (actual == null);
    }
}