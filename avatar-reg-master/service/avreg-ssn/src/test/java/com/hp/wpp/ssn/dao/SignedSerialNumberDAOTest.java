package com.hp.wpp.ssn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hp.wpp.ssn.entities.SSNEntity;
import com.hp.wpp.ssn.util.Utility;

@ContextConfiguration(locations = "/applicationContext-ssn-test.xml")
public class SignedSerialNumberDAOTest extends AbstractTestNGSpringContextTests{

	@Autowired
	SignedSerialNumberDAO dao;
	SSNEntity ssn = null;
//	String serialNumber = "ABCDEFGHIW";
	String serialNumber = "AXCVBNMLKH";
	int domainIndex = 0;
	
	@Test
	public void testCreateAndRetrieveSSN()throws Exception{
		Assert.assertNull(dao.getSSN(serialNumber,domainIndex));
		
		ssn=new SSNEntity();
		String expectedVersion = "1.0";
		
		ssn.setSerialNumber(serialNumber);
		ssn.setOverrunBit(false);
		ssn.setIssuanceCounter(1);
		ssn.setDomainIndex(domainIndex);
		ssn.setInstantInkFlag(true);
		ssn.setVersion(expectedVersion);
		
		dao.createSSN(ssn);	
		
		SSNEntity ssnDB= dao.getSSN(ssn.getSerialNumber(),ssn.getDomainIndex());
		Assert.assertEquals(ssnDB.getVersion(), expectedVersion);
		Assert.assertEquals(ssnDB.getDomainIndex(), domainIndex);
	}
	
	@Test
	public void testUpdateSSN() throws Exception{
		String hashedSeriaNumber = Utility.generateSHA256AndBase64(serialNumber.getBytes());
		System.out.println("serialnumber: "+hashedSeriaNumber+ ", di: "+domainIndex);
		SSNEntity ssn= dao.getSSN(hashedSeriaNumber,domainIndex);
		boolean inkFlag=true;
		ssn.setInstantInkFlag(inkFlag);
		
		dao.updateSSN(ssn);
		
		SSNEntity ssnDB= dao.getSSN(ssn.getSerialNumber(),ssn.getDomainIndex());
		
		Assert.assertTrue(ssnDB.getInstantInkFlag());
	}	
}
