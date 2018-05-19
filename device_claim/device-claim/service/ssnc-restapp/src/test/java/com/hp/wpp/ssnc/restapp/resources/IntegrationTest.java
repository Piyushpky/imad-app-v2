
package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.ssnclaim.service.ssn.service.impl.DeviceLookUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.hp.wpp.ssnclaim.exception.DeviceClaimRetriableException;

@ContextConfiguration(locations = "/dac-applicationContext-test.xml")
public class IntegrationTest extends AbstractTestNGSpringContextTests{

	@Autowired
    DeviceLookUpServiceImpl deviceLookUpServiceImpl;
	
//  @Test
  public void dynamodbTest() throws DeviceClaimRetriableException {
	  
	  deviceLookUpServiceImpl.validateSSNCode("EEACXALZ-SN11111768");
  }
}

