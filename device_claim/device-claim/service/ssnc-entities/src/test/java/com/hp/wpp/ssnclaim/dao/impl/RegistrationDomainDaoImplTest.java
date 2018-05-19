package com.hp.wpp.ssnclaim.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.hp.wpp.mock.dynamodb.http.DynamoDBHttpMockService;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;
import org.mockito.Mockito;

@ContextConfiguration(locations =
{
    "classpath:/dynamodb-mock-applicationContext-test.xml"
})
public class RegistrationDomainDaoImplTest  extends AbstractTestNGSpringContextTests{

 @Autowired
    private DynamoDBHttpMockService dynamoDBHttpMockService;
 
 @Autowired
    private RegistrationDomainDaoImpl registrationDomainDaoImpl;
 /*   @Autowired 
    private AmazonDynamoDB dynamoDB ;
  */  @Autowired 
    private  DynamoDBMapper dynamoDBMapper;

   
	 @BeforeClass
    public void setup() {
		  dynamoDBHttpMockService.start();
	      List<Class> classes = new ArrayList<>();
	        classes.add(RegistrationDomainEntity.class);	        
	        dynamoDBHttpMockService.createTable(classes);
	    
    }
	
	@AfterClass
    public void teardown(){
       //Stop mock service
        dynamoDBHttpMockService.stop();
    } 


  @Test
  public void createPrinterDataLookUp() {
	  		
			RegistrationDomainEntity registrationDomainEntity = new RegistrationDomainEntity();
			registrationDomainEntity.setDomainIndex(0);
			registrationDomainEntity.setDomainKey("domain_key1");
			registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
			registrationDomainEntity = new RegistrationDomainEntity();
			registrationDomainEntity.setDomainIndex(1);
			registrationDomainEntity.setDomainKey("domain_key2");
			registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
			
			List<RegistrationDomainEntity> actual=registrationDomainDaoImpl.getRegistrationDomainEntity();
			int size=actual.size();
			Assert.assertEquals(size, 2);
			Assert.assertEquals((actual.get(0).getDomainIndex()),1);
  }

  @Test
  public void getPrinterDataLookUpEntity() {
	  RegistrationDomainEntity registrationDomainEntity = new RegistrationDomainEntity();
		registrationDomainEntity.setDomainIndex(1);
		registrationDomainEntity.setDomainKey("domain_key2");
		registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
		
		DynamoDBMapper dynamoDBMapperNew = Mockito.mock(DynamoDBMapper.class);		
		Mockito.when(dynamoDBMapperNew.load(Mockito.eq(RegistrationDomainEntity.class),Mockito.anyString())).thenReturn(registrationDomainEntity);
		 assert(registrationDomainEntity.getDomainKey().equals("domain_key2"));
  }
}
