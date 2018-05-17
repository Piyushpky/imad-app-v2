package com.hp.wpp.avatar.registration.device.repository;

import com.hp.wpp.avatar.registration.device.entities.DeviceAdditionalInfo;
import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.*;

public class EntityDeviceRepositoryTest extends  BaseRepositoryTest{

	private static final String ENTITY_REVISION = "entity-revision";

	private static final String ENTITY_DOMAIN = "entity-domain";

	private static final String ENTITY_MODEL = "entity-model";
	

	@Autowired
	private BlacklistRulesDao blacklistRulesDao;

	@Autowired
	private EntityDeviceRepository entityDeviceRepository;

	
	@Autowired
	private HashedEntityIdentifierRepository hashedEntityIdentifierRepository; 
	
	private EntityDevice device;
	private String cloudId;
	private String entityId;
	
	
	@BeforeClass
	public void init(){

	}
	
	// checks required for delete, update
	
	@BeforeMethod
	public void setUp() {
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId = UUID.randomUUID().toString().replaceAll("-", "");

		device = createEntityDevice(cloudId, entityId);
		entityDeviceRepository.save(device);
	}
	
	@Test
	public void testVerifyGetEntityServiceRecord() throws Exception{
		
		EntityDevice entityDevice = entityDeviceRepository.findByCloudId(cloudId);
//		assertEquals(entityDevice.getResetCounter(), 1);
		assertEquals(entityDevice.getEntityDomain(), ENTITY_DOMAIN);
		
		EntityDevice entityDevice1 = entityDeviceRepository.findByEntityIdAndEntityModel(entityId, ENTITY_MODEL);
//		assertEquals(entityDevice1.getResetCounter(), 2);
		assertEquals(entityDevice1.getEntityDomain(), ENTITY_DOMAIN);
	}
	
	@Test
	public void testGetServiceAdditionalInfo(){
		DeviceAdditionalInfo deviceAdditionalInfo = entityDeviceRepository.retrieveDeviceAdditionalInfoByCloudId(cloudId);
		assertEquals(deviceAdditionalInfo.getEntityRevision(), ENTITY_REVISION);
	}
	
	@Test
	public void testVerifyPersistEntityServiceWithHashIdentifier(){
		String hashedEntityId=entityId+"123";
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");
		
		EntityDevice device1 = createEntityDevice(cloudId, entityId);
		HashedEntityIdentifier entityIdentifier  =new HashedEntityIdentifier(entityId, hashedEntityId);
		
		entityDeviceRepository.persistHashedIdentifier(entityIdentifier);
		entityDeviceRepository.persistEntityDevices(device1);
		
		EntityDevice entityDevice1 = entityDeviceRepository.findByEntityIdAndEntityModel(entityId, ENTITY_MODEL);
		assertEquals(entityDevice1.getResetCounter(), 2);
		assertEquals(entityDevice1.getEntityDomain(), ENTITY_DOMAIN);
		
		HashedEntityIdentifier hashedEntityIdentifier = hashedEntityIdentifierRepository.findByEntityIdentifier(entityId);
		HashedEntityIdentifier hashedEntityIdentifier1 = hashedEntityIdentifierRepository.findByHashedEntityIdentifier(hashedEntityId);

		assertEquals(hashedEntityIdentifier.getHashedEntityIdentifier(), hashedEntityId);
		assertEquals(hashedEntityIdentifier1.getEntityIdentifier(),entityId);
	}

	@Test
	public void testVerifyPersistEntityMCID(){
		String hashedEntityId=entityId+"123";
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");

		EntityDevice device1 = createEntityDevice(cloudId, entityId);
		entityDeviceRepository.persistEntityDevices(device1);
		assertEquals(device1.getDeviceAdditionalInfo().getEntityMCID(),entityId);
	}

	@Test
	public void testVerifyPersistEmptyEntityMCIDWithHashIdentifier(){
		String hashedEntityId=entityId+"123";
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");
		String entityMCID= null;

		EntityDevice device1 = createEntityDevice(cloudId, entityId);
		device1.getDeviceAdditionalInfo().setEntityMCID(entityMCID);
		entityDeviceRepository.persistEntityDevices(device1);
	}
	
	@Test(expectedExceptions = {DataIntegrityViolationException.class})
	public void testVerifyDuplicatePersistHashIdentifier(){
		String hashedEntityId=entityId+"123";
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");
		String entityMCID= null;
		
		EntityDevice device1 = createEntityDevice(cloudId, entityId);
		HashedEntityIdentifier entityIdentifier  =new HashedEntityIdentifier(entityId, hashedEntityId);
		
		
		entityDeviceRepository.persistHashedIdentifier(entityIdentifier);
		entityDeviceRepository.persistEntityDevices(device1);
		
		EntityDevice entityDevice1 = entityDeviceRepository.findByEntityIdAndEntityModel(entityId, ENTITY_MODEL);
		assertEquals(entityDevice1.getResetCounter(), 2);
		assertEquals(entityDevice1.getEntityDomain(), ENTITY_DOMAIN);
		
		HashedEntityIdentifier hashedEntityIdentifier = hashedEntityIdentifierRepository.findByEntityIdentifier(entityId);
		HashedEntityIdentifier hashedEntityIdentifier1 = hashedEntityIdentifierRepository.findByHashedEntityIdentifier(hashedEntityId);

		assertEquals(hashedEntityIdentifier.getHashedEntityIdentifier(), hashedEntityId);
		assertEquals(hashedEntityIdentifier1.getEntityIdentifier(),entityId);
		
		cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId=  UUID.randomUUID().toString().replaceAll("-", "");
		EntityDevice device2 = createEntityDevice(cloudId, entityId);
		device2.setEntityModel("New-Model");
		entityDeviceRepository.persistHashedIdentifier(entityIdentifier);
	}

	@Test
	public void testUpdateServiceAdditionalInfo(){
		DeviceAdditionalInfo deviceAdditionalInfo= entityDeviceRepository.retrieveDeviceAdditionalInfoByCloudId(cloudId);
		String expectedEntityRevision = ENTITY_REVISION+"1";
		String expectedAdditionalIds = deviceAdditionalInfo.getEntityAdditionalIds()+"1";
		String expectedCountry = "unitedStates";

		if(!expectedEntityRevision.equals(deviceAdditionalInfo.getEntityRevision()))
			entityDeviceRepository.updateDeviceAdditionalInfo(
					deviceAdditionalInfo.getCloudId(), expectedEntityRevision,
					deviceAdditionalInfo.getEntityVersionDate(),
					expectedCountry,
					deviceAdditionalInfo.getLanguage(),
					deviceAdditionalInfo.getSpecVersion(),
					deviceAdditionalInfo.getOriginator(),
					expectedAdditionalIds,
					deviceAdditionalInfo.getEntityInfo(),deviceAdditionalInfo.getEntityMCID());
		
		deviceAdditionalInfo = entityDeviceRepository.retrieveDeviceAdditionalInfoByCloudId(cloudId);
		assertEquals(deviceAdditionalInfo.getEntityRevision(), expectedEntityRevision);
		assertEquals(deviceAdditionalInfo.getEntityAdditionalIds(), expectedAdditionalIds);
		assertEquals(deviceAdditionalInfo.getCountryAndRegionName(), expectedCountry);
	}
	
	@Test
	public void testDeleteEntityService(){
		EntityDevice device = entityDeviceRepository.findByCloudId(cloudId);
		
		assertNotNull(entityDeviceRepository.findByCloudId(cloudId));
		assertNotNull(entityDeviceRepository.retrieveDeviceAdditionalInfoByCloudId(cloudId));
		
		entityDeviceRepository.delete(device.getId());
		
		assertNull(entityDeviceRepository.findByCloudId(cloudId));
		assertNull(entityDeviceRepository.retrieveDeviceAdditionalInfoByCloudId(cloudId));
	}

	@Test
	public void testGetDeviceWithAdditionalInfo()throws Exception{
		EntityDevice deviceDB = entityDeviceRepository.getDeviceWithDeviceAdditionalInfo(cloudId);

		assertNotNull(deviceDB);
		assertNotNull(deviceDB.getDeviceAdditionalInfo());
		assertEquals(deviceDB.getEntityDomain(), ENTITY_DOMAIN);
		assertEquals(deviceDB.getDeviceAdditionalInfo().getEntityRevision(), ENTITY_REVISION);
	}

	private EntityDevice createEntityDevice(String cloudId, String entityId) {
		EntityDevice device = new EntityDevice();
		device.setCloudId(cloudId);
		device.setEntityId(entityId);
		device.setEntityDomain("entity-domain");
		device.setEntityModel(ENTITY_MODEL);
		device.setEntityName("entity-name");
		device.setResetCounter(2);
		device.setEntityUUID(entityId);

		DeviceAdditionalInfo deviceAdditionalInfo = new DeviceAdditionalInfo();
		deviceAdditionalInfo.setCloudId(cloudId);
		deviceAdditionalInfo.setCountryAndRegionName("india");
		deviceAdditionalInfo.setEntityAdditionalIds("entity-additional-ids");
		deviceAdditionalInfo.setEntityInfo("entity-info");
		deviceAdditionalInfo.setEntityRevision(ENTITY_REVISION);
		deviceAdditionalInfo.setEntityVersionDate("01-01-2015");
		deviceAdditionalInfo.setLanguage("en");
		deviceAdditionalInfo.setOriginator("ews");
		deviceAdditionalInfo.setSpecVersion("1.0");
		deviceAdditionalInfo.setEntityMCID(entityId);


		deviceAdditionalInfo.setDevice(device);
		assertNull(device.getDeviceAdditionalInfo());
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		
		deviceAdditionalInfo = device.getDeviceAdditionalInfo();
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		return device;
	}
	
	
}
