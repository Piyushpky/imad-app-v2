package com.hp.wpp.ssn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "/applicationContext-ssn.xml")
public class SSNComponentTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private SignedSerialNumberValidator signedSerialNumber;
	
	

	@Test(enabled=false)
     public void testEndToEnd() throws Exception {
	
		String validSSN="HQACAAK7-ZXCVBNMLKJ";
		signedSerialNumber.validateAndPersistSSN(validSSN);
		
			
		}
	  

	@Test(enabled=false)
     public void testValidateAndPersistSSNInvalidSSN() throws Exception {
	 
		String invalidSignature = "HQACAAK7-ZXCVBNML";
		signedSerialNumber.validateAndPersistSSN(invalidSignature);
		
			
		}
	
	@Test(enabled=false)
    public void testValidateAndPersistSSNInvalidSSNCounter() throws Exception {
	 
		String invalidIssuanceCounter = "GIADMAJX-ZXCVBNMLKJ";
		signedSerialNumber.validateAndPersistSSN(invalidIssuanceCounter);
		
			
		}
	
	@Test(enabled=false)
    public void testValidateAndPersistSSNValidSSNCounter() throws Exception {
	 
		String validIssuanceCounter = "GEABUALY-ZXCVBNMLKJ";
		signedSerialNumber.validateAndPersistSSN(validIssuanceCounter);
		
			
		}
	
	
	  
	  
	  
	  }

