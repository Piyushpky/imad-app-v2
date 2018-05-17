package com.hp.wpp.avatar.registration.service.repository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.service.entities.ServiceAdditionalInfo;

public class EntityServiceRepositoryTest extends  BaseRepositoryTest{

	private static final String ENTITY_REVISION = "entity-revision";

	private static final String ENTITY_DOMAIN = "entity-domain";

	private static final String ENTITY_MODEL = "entity-model";
	
	@Autowired
	private EntityServiceRepository entityServiceRepository;
	
	@Autowired
	private HashedEntityIdentifierRepository hashedEntityIdentifierRepository; 
	
	private EntityService service;
	private String cloudId;
	private String entityId;
	
	
	@BeforeClass
	public void init(){
//		org.hsqldb.util.DatabaseManagerSwing.main(new String[] { "--url","jdbc:hsqldb:mem:pod_service", "--noexit" });
	}
	
	// checks required for delete, update
	
	@BeforeMethod
	public void setUp() {
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId = UUID.randomUUID().toString().replaceAll("-", "");

		service = createEntityService(cloudId, entityId);
		entityServiceRepository.save(service);
	}
	
	@Test
	public void testVerifyGetEntityServiceRecord() throws Exception{
		
		EntityService entityService = entityServiceRepository.findByCloudId(cloudId);
		assertEquals(entityService.getResetCounter(), 2);
		assertEquals(entityService.getEntityDomain(), ENTITY_DOMAIN);
		
		EntityService entityService1 = entityServiceRepository.findByEntityIdAndEntityModel(entityId, ENTITY_MODEL);
		assertEquals(entityService1.getResetCounter(), 2);
		assertEquals(entityService1.getEntityDomain(), ENTITY_DOMAIN);
	}
	
	@Test
	public void testGetServiceAdditionalInfo(){
		ServiceAdditionalInfo serviceAdditionalInfo = entityServiceRepository.retrieveServiceAdditionalInfoByCloudId(cloudId);
		assertEquals(serviceAdditionalInfo.getEntityRevision(), ENTITY_REVISION);
	}
	
	@Test
	public void testVerifyPersistEntityServiceWithHashIdentifier(){
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");
		
		EntityService service1 = createEntityService(cloudId, entityId);
		HashedEntityIdentifier entityIdentifier  =new HashedEntityIdentifier(entityId, entityId+"123");
		
		entityServiceRepository.persistEntityServices(service1 , entityIdentifier);
		
		EntityService entityService1 = entityServiceRepository.findByEntityIdAndEntityModel(entityId, ENTITY_MODEL);
		assertEquals(entityService1.getResetCounter(), 2);
		assertEquals(entityService1.getEntityDomain(), ENTITY_DOMAIN);
		
		HashedEntityIdentifier hashedEntityIdentifier = hashedEntityIdentifierRepository.findByEntityIdentifier(entityId);
		assertEquals(hashedEntityIdentifier.getHashedEntityIdentifier(), entityId+"123");
	}
	
	@Test
	public void testUpdateServiceAdditionalInfo(){
		ServiceAdditionalInfo serviceAdditionalInfo= entityServiceRepository.retrieveServiceAdditionalInfoByCloudId(cloudId);
		String expectedEntityRevision = ENTITY_REVISION+"1";
		String expectedAdditionalIds = serviceAdditionalInfo.getEntityAdditionalIds()+"1";
		String expectedCountry = "unitedStates";

		if(!expectedEntityRevision.equals(serviceAdditionalInfo.getEntityRevision()))
			entityServiceRepository.updateServieAdditionalInfo(
					serviceAdditionalInfo.getCloudId(), expectedEntityRevision,
					serviceAdditionalInfo.getEntityVersionDate(),
					expectedCountry,
					serviceAdditionalInfo.getLanguage(),
					serviceAdditionalInfo.getSpecVersion(),
					serviceAdditionalInfo.getOriginator(),
					expectedAdditionalIds,
					serviceAdditionalInfo.getEntityInfo());
		
		serviceAdditionalInfo = entityServiceRepository.retrieveServiceAdditionalInfoByCloudId(cloudId);
		assertEquals(serviceAdditionalInfo.getEntityRevision(), expectedEntityRevision);
		assertEquals(serviceAdditionalInfo.getEntityAdditionalIds(), expectedAdditionalIds);
		assertEquals(serviceAdditionalInfo.getCountryAndRegionName(), expectedCountry);
	}
	
	@Test
	public void testDeleteEntityService(){
		service = entityServiceRepository.findByCloudId(cloudId);
		
		assertNotNull(entityServiceRepository.findByCloudId(cloudId));
		assertNotNull(entityServiceRepository.retrieveServiceAdditionalInfoByCloudId(cloudId));
		
		entityServiceRepository.delete(service.getEntityServiceId());
		
		assertNull(entityServiceRepository.findByCloudId(cloudId));
		assertNull(entityServiceRepository.retrieveServiceAdditionalInfoByCloudId(cloudId));
	}

	private EntityService createEntityService(String cloudId, String entityId) {
		EntityService service = new EntityService();
		
		
		service.setCloudId(cloudId);
		service.setEntityDomain(ENTITY_DOMAIN);
		service.setEntityId(entityId);
		service.setEntityModel(ENTITY_MODEL);
		service.setEntityName("entity-name");
		service.setResetCounter(2);
		service.setEntityUUID(entityId);
		
		ServiceAdditionalInfo serviceAdditionalInfo = new ServiceAdditionalInfo();
		serviceAdditionalInfo.setCloudId(cloudId);
		serviceAdditionalInfo.setCountryAndRegionName("india");
		serviceAdditionalInfo.setEntityAdditionalIds("entity-additional-ids");
		serviceAdditionalInfo.setEntityInfo("entity-info");
		serviceAdditionalInfo.setEntityRevision(ENTITY_REVISION);
		serviceAdditionalInfo.setEntityVersionDate("01-01-2015");
		serviceAdditionalInfo.setLanguage("en");
		serviceAdditionalInfo.setOriginator("ews");
		serviceAdditionalInfo.setSpecVersion("1.0");
		
		serviceAdditionalInfo.setEntityService(service);
		assertNull(service.getServiceAdditionalInfo());
		service.setServiceAdditionalInfo(serviceAdditionalInfo);
		
		serviceAdditionalInfo = service.getServiceAdditionalInfo();
		service.setServiceAdditionalInfo(serviceAdditionalInfo);
		
		return service;
	}
	
	
}
