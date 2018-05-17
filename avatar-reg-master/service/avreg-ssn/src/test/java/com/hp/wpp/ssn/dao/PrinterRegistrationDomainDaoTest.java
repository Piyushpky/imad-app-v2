package com.hp.wpp.ssn.dao;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.ssn.entities.PrinterRegistrationDomain;
import com.hp.wpp.ssn.entities.PrinterRegistrationDomainKey;

@ContextConfiguration(locations = "/applicationContext-ssn-test.xml")
public class PrinterRegistrationDomainDaoTest extends AbstractTestNGSpringContextTests{

	
	@Autowired
	EmbeddedHSQLDao hsqlDao;
	
	@Autowired
	PrinterRegistrationDomainDAO registrationDomainDao;
	
	@BeforeMethod
	public void init() throws Exception{
		
		PrinterRegistrationDomain registrationDomain = new PrinterRegistrationDomain();
		registrationDomain.setRegistrationDomain("test-domain");
		registrationDomain.setSsnIndex(0);
		PrinterRegistrationDomainKey domainKey = new PrinterRegistrationDomainKey();
		domainKey.setRegistrationDomainKey("testDomainKey");
		registrationDomain.setDomainKey(domainKey );
		hsqlDao.createPrinterRegistrationDomain(registrationDomain);
	}
	
	@Test
	public void testRetrieveRegistrationDomainBySSNIndex() throws Exception{
		Exception expectedException = new NoResultException();
		String actualKey = registrationDomainDao.getPrinterRegistrationDomainKey(0);
		Assert.assertEquals(actualKey, "testDomainKey");
		
		try{
		registrationDomainDao.getPrinterRegistrationDomainKey(4);
		}catch (Exception actualException) {
			Assert.assertEquals(actualException.getClass().getName(),
					expectedException.getClass().getName());
		}
	}
	
	@Test
	public void testRetrieveRegistrationDomainByDomainName() throws Exception{
		Assert.assertEquals(registrationDomainDao.getPrinterRegistrationDomainKey("test-domain"), "testDomainKey");

		Assert.assertNull(registrationDomainDao.getPrinterRegistrationDomainKey("hello"));
	}
}
