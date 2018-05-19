package com.hp.wpp.ssnclaim.service.printercode;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.hp.wpp.ssnc.common.util.CryptoHelper;
import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeException;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.impl.PrinterCodeValidatorImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.exception.CryptographyException;

public class TestPrinterCode {

	@Mock
	public PrinterResourceServiceConfig printerResourceServiceConfig;
	
	private static final String TEST_DOMAIN_KEY = "testDomainKey";	
	private static final String TEST_DOMAIN_KEY_ENCODE = "dGVzdERvbWFpbktleQ==";
	private static final String TEST_DOMAIN_KEY5 = "ctEXsvtEk1cQHMN2O1TUrdv4m0xAwYhJxLijOp0Wo+4=";
	String Stage="QUVTRU5DUllQVElP";
	String Pie = "QUVTRU5DUllQVElP";

	private PrinterCodeValidatorImpl printerCodeImpl;

	public Map<Integer, String> domainMap;
	public Map<Integer, String> domainMapInvalid;
	public Map<Integer, String> domainMapencodeInvalid;
	
	 

	@BeforeMethod
	public void setUp() throws CryptographyException, UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		printerCodeImpl = new PrinterCodeValidatorImpl();
		printerCodeImpl.setPrinterResourceServiceConfig(printerResourceServiceConfig);
		domainMap = new HashMap<Integer, String>();
		domainMapInvalid = new HashMap<Integer, String>();
		domainMapencodeInvalid = new HashMap<Integer, String>();
		domainMap.put(0, CryptoHelper.aesEncryptBase64(TEST_DOMAIN_KEY,Pie));
		domainMap.put(1,CryptoHelper.aesEncryptBase64(TEST_DOMAIN_KEY,Pie));
		domainMap.put(5, TEST_DOMAIN_KEY5);
		
		domainMapInvalid.put(0, "ctEXsvtEk1cQHMN2O1TUrdv4m0xAwYhJxLijOp0Wo+4=");
		domainMapInvalid.put(1, "ctEXsvtEk1cQHMN2O1TUrdv4m0xAwYhJxLijOp0Wo+4=");
		domainMapInvalid.put(5, TEST_DOMAIN_KEY);
		domainMapencodeInvalid.put(5, CryptoHelper.aesEncryptBase64(TEST_DOMAIN_KEY5,Stage));
		domainMapencodeInvalid.put(1,CryptoHelper.aesEncryptBase64(TEST_DOMAIN_KEY_ENCODE,Pie));
		domainMapencodeInvalid.put(0,CryptoHelper.aesEncryptBase64(TEST_DOMAIN_KEY_ENCODE,Pie));
	}
	@DataProvider(name = "getInputParametersForException")
    Object[][] getInputParametersForException() {
        return new Object[][]{
                {"QUVTRU5DUllQVElP","BQ05GQY9IVULN1VK"}, //domain not decode, but encrypted
                {"ASGESGSECDWSDDGGF", "BQ05GQY9IVULN1VK"}, //domain decoded but not encrypted 
                {"ASGESGSECDWSDDGGF", "HR9KPTTH6561Y162"}, //invalid domainkey not mapped but encrypted 
               };}
	@DataProvider(name = "getInputParametersForExceptionNoMap")
    Object[][] getInputParametersForExceptionNoMap() {
        return new Object[][]{
                {"QUVTRU5DUllQVElP", "8MQV9NIO0123456"}, //invalid printercode and invalid domainmap 
                {"QUVTRU5DUllQVElP","8MQV9NIO0123456"},// invalid printercode not mapped
                {"ASGESGSECDWSDDGGF","BQ05GQY9IVULN1VK"} //invalid printercode not mapped
              
        };}

        @DataProvider(name = "getInputParametersForSucess")
	    Object[][] getInputParametersForSucess() {
	        return new Object[][]{
	                {Pie,"BQ05GQY9IVULN1VK","Y9IVULN1VK",1,2}, //domain  decode, and encrypted and mapped
	                {Stage, "HR9KPTTH6561Y162","TH6561Y162",5,6} ,
	                {Pie,"GQ15F0FMDIW5YZY2","FMDIW5YZY2",1,2}
	        };}
	@Test(dataProvider = "getInputParametersForException",expectedExceptions = InvalidPrinterCodeException.class)
	public void testPrinterCodeforException(String domainKey,String printerCode) throws InvalidPrinterCodeException {
		  Mockito.when(printerResourceServiceConfig.getDomainKeyEncyptedKeyValue()).thenReturn(domainKey);
		PrinterCodeData printerCodeData = new PrinterCodeData();
		printerCodeData = printerCodeImpl.validatePrinterCode(printerCode,
				domainMap);
		
	}
	
	@Test(dataProvider = "getInputParametersForExceptionNoMap",expectedExceptions = InvalidPrinterCodeException.class)
	public void testPrinterCodeforExceptionNoMap(String domainKey,String printerCode) throws InvalidPrinterCodeException {
		  Mockito.when(printerResourceServiceConfig.getDomainKeyEncyptedKeyValue()).thenReturn(domainKey);
		PrinterCodeData printerCodeData = new PrinterCodeData();
		printerCodeData = printerCodeImpl.validatePrinterCode(printerCode,
				domainMapInvalid);
		
	}
	
	@Test(dataProvider = "getInputParametersForSucess")
	public void testPrinterCodewithoutdomainencodenEncyrpted(String domainKey,String printerCode,String serialNumber,int index,int counter) throws InvalidPrinterCodeException, CryptographyException {
		 Mockito.when(printerResourceServiceConfig.getDomainKeyEncyptedKeyValue()).thenReturn(domainKey);
		PrinterCodeData printerCodeData = new PrinterCodeData();
		printerCodeData = printerCodeImpl.validatePrinterCode(printerCode,
				domainMapencodeInvalid);
		Assert.assertEquals(printerCodeData.getDomainIndex(),index);
		Assert.assertEquals(printerCodeData.getSerialNumber(),serialNumber);
		Assert.assertTrue(printerCodeData.getInstantInkFlag());
		Assert.assertEquals(printerCodeData.getIssuanceCounter(),counter);
		Assert.assertEquals(printerCodeData.getOwnership(),2);
		Assert.assertFalse(printerCodeData.getOverrunBit());
		
	}
	
	


	
}
