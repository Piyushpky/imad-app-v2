package com.hp.wpp.avatar.registration.device.processor;

import com.hp.wpp.avatar.framework.common.config.RegistrationProcessorConfig;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRegistrationDataException;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.registration.device.entities.DeviceAdditionalInfo;
import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.device.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.device.entities.domain.RegistrationDomain;
import com.hp.wpp.avatar.registration.device.repository.DeviceServiceInstanceRepository;
import com.hp.wpp.avatar.registration.device.repository.EntityDeviceRepository;
import com.hp.wpp.avatar.registration.device.repository.HashedEntityIdentifierRepository;
import com.hp.wpp.avatar.registration.device.repository.RegistrationDomainRepository;
import com.hp.wpp.cidgenerator.InvalidCloudIdException;
import com.hp.wpp.cidgenerator.ParsedCloudID;
import com.hp.wpp.cidgenerator.impl.CloudIdGeneratorImpl;
import com.hp.wpp.http.WppHttpClient;
import com.hp.wpp.ssn.SignedSerialNumberValidator;
import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.messages.exception.MessageStreamNonRetriableException;
import com.hp.wpp.stream.messages.exception.MessageStreamRetriableException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;

public class DeviceRegistrationProcessorTest {

	private static final String TEST_DOMAIN = "test_domain";
	private static final String ENTITY_MODEL = "entity-model";
	private static final String ENTITY_REVISION = "entity-revision";
	public static final String ENTITY_NAME = "entity-name";
	public static final String ENTITY_INFO = "entity-info";
	public static final String ENTITY_VERSION_DATE = "01-01-2015";
	public static final String LANGUAGE = "en";
	public static final String ORIGINATOR = "ews";
	public static final String SPEC_VERSION = "1.0";
	public static final String COUNTRY = "india";
	public static final String ENTITY_ADDITIONAL_IDS = "entity-additional-ids";

	@Mock
	private EntityDeviceRepository entityDeviceRepository;
	@Mock
	private HashedEntityIdentifierRepository hashedEntityIdentifierRepository;
	@Mock
	private RegistrationDomainRepository registrationDomainRepository;

	@Mock private DeviceServiceInstanceRepository serviceInstanceRepository;
	
	@Mock private SignedSerialNumberValidator ssnValidator;
	
	@Mock private RegistrationProcessorConfig registrationProcessorConfig; 
	
	@Mock private WppHttpClient wppHttpClient;

	@Mock private EventProducer eventProducer;
	
	@Mock private ThreadPoolExecutor threadPoolExecutor;
	private DeviceRegistrationProcessor deviceRegistration;

	private EntityIdentificationBO entityIdentification;
	
	private CloudIdGeneratorImpl cloudIdGenerator;
	
	private String entityId;

	@BeforeMethod
	public void setUp()  throws Exception{
		MockitoAnnotations.initMocks(this);
		deviceRegistration = new DeviceRegistrationProcessor(wppHttpClient,threadPoolExecutor);
		deviceRegistration.setEntityDeviceRepository(entityDeviceRepository);
		deviceRegistration.setDeviceServiceInstanceRepository(serviceInstanceRepository);
		deviceRegistration.setHashedEntityIdentifierRepository(hashedEntityIdentifierRepository);
		deviceRegistration.setRegistrationDomainRepository(registrationDomainRepository);
		deviceRegistration.setSsnValidator(ssnValidator);
		cloudIdGenerator = new CloudIdGeneratorImpl();
		deviceRegistration.setCloudIdGenerator(cloudIdGenerator);
		deviceRegistration.setPodCode((short)0);
		deviceRegistration.setRegistrationProcessorConfig(registrationProcessorConfig);
		deviceRegistration.setDeRegistrationStreamName("DeleteStream");
		deviceRegistration.setEventProducer(eventProducer);
		//deviceRegistration.setSsnEnabled(true);
		
		entityIdentification=createEntityIdentificationBO(1);
		ServiceInstance serviceInstance = new ServiceInstance();
		serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/printerconfig");
		
		Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance);
		Mockito.when(registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()).thenReturn(true);
		RegistrationDomain domain = new RegistrationDomain();
		domain.setDomainId(1);
		domain.setDomainIndex(0);
		domain.setEntityDomain("test_domain");
		Mockito.when(registrationDomainRepository.findByRegistrationDomain(Mockito.anyString())).thenReturn(domain);
		//Mockito.doNothing().when(threadPoolExecutor).execute(any(DeviceClaimProcessor.class));
	}



	private EntityIdentificationBO createEntityIdentificationBO(int resetCounter)throws Exception {
		EntityDevice device = createEntityDevice(resetCounter);
		
		EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
		BeanUtils.copyProperties(device,entityIdentificationBO);
		BeanUtils.copyProperties(device.getDeviceAdditionalInfo(),entityIdentificationBO);
        
		return entityIdentificationBO;
	}
	
	private EntityIdentificationBO createEntityIdentificationBODuplicateSerial(int resetCounter, String oldEntityId)throws Exception {
		EntityDevice device = createEntityDeviceDuplicateSerialNumber(resetCounter,oldEntityId);
		EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
		BeanUtils.copyProperties(device,entityIdentificationBO);
		BeanUtils.copyProperties(device.getDeviceAdditionalInfo(),entityIdentificationBO);
        
		return entityIdentificationBO;
	}

	private EntityDevice createEntityDevice(int resetCounter) {
		EntityDevice device = new EntityDevice();
		String cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		device.setCloudId(cloudId);
		entityId = UUID.randomUUID().toString().replaceAll("-", "");
		device.setEntityId(entityId);
		device.setEntityDomain(TEST_DOMAIN);
		device.setEntityModel(ENTITY_MODEL);
		device.setEntityName(ENTITY_NAME);
		device.setResetCounter(resetCounter);
		device.setEntityUUID(entityId);
		
		DeviceAdditionalInfo deviceAdditionalInfo = new DeviceAdditionalInfo();
		deviceAdditionalInfo.setCloudId(cloudId);
		deviceAdditionalInfo.setCountryAndRegionName(COUNTRY);
		deviceAdditionalInfo.setEntityAdditionalIds(ENTITY_ADDITIONAL_IDS);
		deviceAdditionalInfo.setEntityInfo(ENTITY_INFO);
		deviceAdditionalInfo.setEntityRevision(ENTITY_REVISION);
		deviceAdditionalInfo.setEntityVersionDate(ENTITY_VERSION_DATE);
		deviceAdditionalInfo.setLanguage(LANGUAGE);
		deviceAdditionalInfo.setOriginator(ORIGINATOR);
		deviceAdditionalInfo.setSpecVersion(SPEC_VERSION);
		
		deviceAdditionalInfo.setDevice(device);
		assertNull(device.getDeviceAdditionalInfo());
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		
		deviceAdditionalInfo = device.getDeviceAdditionalInfo();
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		return device;
	}
	
	private EntityDevice createEntityDeviceDuplicateSerialNumber(int resetCounter, String oldEntityId) {
		EntityDevice device = new EntityDevice();
		String cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		device.setCloudId(cloudId);
		entityId = oldEntityId;
		device.setEntityId(entityId);
		device.setEntityDomain(TEST_DOMAIN);
		device.setEntityModel("New Model");
		device.setEntityName(ENTITY_NAME);
		device.setResetCounter(resetCounter);
		device.setEntityUUID(entityId);
		
		DeviceAdditionalInfo deviceAdditionalInfo = new DeviceAdditionalInfo();
		deviceAdditionalInfo.setCloudId(cloudId);
		deviceAdditionalInfo.setCountryAndRegionName(COUNTRY);
		deviceAdditionalInfo.setEntityAdditionalIds(ENTITY_ADDITIONAL_IDS);
		deviceAdditionalInfo.setEntityInfo(ENTITY_INFO);
		deviceAdditionalInfo.setEntityRevision(ENTITY_REVISION);
		deviceAdditionalInfo.setEntityVersionDate(ENTITY_VERSION_DATE);
		deviceAdditionalInfo.setLanguage(LANGUAGE);
		deviceAdditionalInfo.setOriginator(ORIGINATOR);
		deviceAdditionalInfo.setSpecVersion(SPEC_VERSION);
		
		deviceAdditionalInfo.setDevice(device);
		assertNull(device.getDeviceAdditionalInfo());
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		
		deviceAdditionalInfo = device.getDeviceAdditionalInfo();
		device.setDeviceAdditionalInfo(deviceAdditionalInfo);
		return device;
	}

	@Test
	public void testDeviceRegistrationSuccess() throws Exception {
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentification);
		//Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());

		verifyEntityPrinterType(config.getCloudId());
	}
	

	@Test
	public void testDuplicateSerialNumber() throws Exception {
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentification);
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
		verifyEntityPrinterType(config.getCloudId());
	    
		Mockito.doThrow(DataIntegrityViolationException.class).when(entityDeviceRepository).persistHashedIdentifier((HashedEntityIdentifier) Mockito.anyObject());
		EntityIdentificationBO newEntityIdentification=createEntityIdentificationBODuplicateSerial(1,entityIdentification.getEntityId());
		config = deviceRegistration.registerEntity(newEntityIdentification);
		verifyEntityPrinterType(config.getCloudId());
		
	}

	@Test
	public void testDeviceRegistrationSuccessWithoutSSN() throws Exception {
		//deviceRegistration.setSsnEnabled(false);
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentification);
		//Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
		Mockito.verifyNoMoreInteractions(ssnValidator);

		verifyEntityPrinterType(config.getCloudId());
	}
	
	@Test
	public void testDeviceRegistrationSuccessWithCredentialCheck() throws Exception {
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentification);
		Assert.assertEquals(config.getConfigurations().size(), 3);
		boolean success=false;
		for(LinkBO linkBO:config.getConfigurations()){
			if(linkBO.getRel().equals(ServiceInstanceType.CREDENTIAL_REFRESH.getValue())){
				success=true;
			}
		}
		Assert.assertTrue(success);
	}
	
	@Test
	public void testDeviceRegistrationSuccessWithCredentialCheckAndAppconfigDisabled() throws Exception {
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		Mockito.when(registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()).thenReturn(false);
		
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentification);
		
		Assert.assertEquals(config.getConfigurations().size(), 2);
		
		boolean urlNotExist=true;
		for(LinkBO linkBO:config.getConfigurations()){
			if(linkBO.getRel().equals(ServiceInstanceType.CREDENTIAL_REFRESH.getValue())){
				urlNotExist=false;
			}
		}
		Assert.assertTrue(urlNotExist);
	}
	
	private void verifyEntityPrinterType(String cloudId) throws Exception{
		Assert.assertNotNull(cloudId);
		ParsedCloudID parsedCloudID = cloudIdGenerator.parse(cloudId);
		Assert.assertEquals((int) parsedCloudID.getEntitytype(), EntityType.devices.getValue());
	}
	
	@Test
	public void testDeviceReRegistrationSuccess() throws Exception{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(3);
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityDevice(1));
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentificationBO2);
		Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		Mockito.verify(entityDeviceRepository).save((EntityDevice) Mockito.anyObject());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	@DataProvider(name = "resetCounterInputs")
	public Object[][] resetCounterInputs() {
		return new Object[][]{
				{2,1}, // reg Payload has higher reset counter value
				{1,2} // reg payload has lower reset counter value
		};
	}

	@Test(dataProvider = "resetCounterInputs")
	public void testDeviceReRegistrationReregFlows(int counterFromPayload, int counterFromDB) throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(counterFromPayload);
		//Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityDevice(counterFromDB));
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentificationBO2);

		Mockito.verify(entityDeviceRepository).delete(any(EntityDevice.class));
		Mockito.verify(entityDeviceRepository).save(any(EntityDevice.class));
		//Mockito.verify(threadPoolExecutor).execute(any(Runnable.class));
		Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		Assert.assertNotNull(config.getCloudId());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	@Test(dataProvider = "resetCounterInputs")
	public void testDeviceReRegistrationReregFlowsWithNotificationCallException(int counterFromPayload, int counterFromDB) throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(counterFromPayload);
		//Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		Mockito.doThrow(new MessageStreamNonRetriableException("test")).when(eventProducer).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityDevice(counterFromDB));
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentificationBO2);
		Mockito.verify(entityDeviceRepository).delete(any(EntityDevice.class));
		Mockito.verify(entityDeviceRepository).save(any(EntityDevice.class));
		//Mockito.verify(threadPoolExecutor).execute(any(Runnable.class));
		Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		Assert.assertNotNull(config.getCloudId());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	@Test
	public void testDeviceReRegistrationWithEqualOwnerShipCounter() throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(1);
		Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityDevice(1));
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentificationBO2);
		Mockito.verify(entityDeviceRepository).updateDeviceAdditionalInfo(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),any(String.class));
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}
	
	@Test
	public void testValidGetRegisteredEntity()  throws Exception
	{
		when(entityDeviceRepository.findByCloudId(any(String.class))).thenReturn(createEntityDevice(1));
		RegisteredEntityBO registeredEntity = deviceRegistration.getRegisteredEntity(any(String.class));
		Assert.assertNotNull(registeredEntity);
		Assert.assertEquals(registeredEntity.getEntityDomain(), TEST_DOMAIN);
		Assert.assertEquals(registeredEntity.getEntityId(), entityId);
	}
	
	@Test(expectedExceptions={EntityNotRegisteredException.class})
	public void testInValidGetRegisteredEntity() throws Exception
	{
		when(entityDeviceRepository.findByCloudId(any(String.class))).thenReturn(null);
		deviceRegistration.getRegisteredEntity(any(String.class));
	}
	
	@Test
	public void testCloudIdForDevice() throws InvalidCloudIdException
	{
		short podCode = 1;
		byte entityTypeValue = (byte)EntityType.devices.getValue();
		CloudIdGeneratorImpl cloudIdGenerator = new CloudIdGeneratorImpl();
		String cloudId = cloudIdGenerator.newCloudID(podCode, entityTypeValue);
		ParsedCloudID cloudIdparsed = cloudIdGenerator.parse(cloudId);
		Assert.assertEquals(EntityType.devices.getValue(), cloudIdparsed.getEntitytype());
	}

	@Test(expectedExceptions={InvalidRegistrationDataException.class})
	public void testValidateEntityIdentificationBOWithOutUUID()throws Exception{
		EntityIdentificationBO entityIdentificationBOWithOutUUID = createEntityIdentificationBO(0);
		entityIdentificationBOWithOutUUID.setEntityUUID(null);

		deviceRegistration.validateEntityIdentificationBO(entityIdentificationBOWithOutUUID);
	}

	@Test
	public void testGetEntityInformation() throws Exception{
		String cloudId = "cloudID123";

		int resetCounter = 1;
		EntityDevice entityDevice = createEntityDevice(resetCounter);
		String hashedEntityId=entityId+"123";
		entityDevice.setEntityId(hashedEntityId);

		entityDevice.setEntityUUID(null);
		when(entityDeviceRepository.getDeviceWithDeviceAdditionalInfo(any(String.class))).thenReturn(entityDevice);

		HashedEntityIdentifier entityIdentifier  =new HashedEntityIdentifier(entityId, hashedEntityId);
		when(hashedEntityIdentifierRepository.findByHashedEntityIdentifier(hashedEntityId)).thenReturn(entityIdentifier);

		EntityIdentificationBO entityIdentificationBO = deviceRegistration.getEntityIdentificationBO(cloudId);
		Assert.assertNotNull(entityIdentificationBO);
		Assert.assertEquals(entityIdentificationBO.getEntityId(), entityId);
		Assert.assertEquals(entityIdentificationBO.getEntityDomain(),TEST_DOMAIN);
		Assert.assertEquals(entityIdentificationBO.getEntityModel(),ENTITY_MODEL);
		Assert.assertEquals(entityIdentificationBO.getEntityName(), ENTITY_NAME);
		Assert.assertEquals(entityIdentificationBO.getResetCounter(), resetCounter);
		//Assert.assertEquals(entityIdentificationBO.getEntityUUID(), entityId);
		Assert.assertEquals(entityIdentificationBO.getCountryAndRegionName(), COUNTRY);
		Assert.assertEquals(entityIdentificationBO.getEntityAdditionalIds(), ENTITY_ADDITIONAL_IDS);
		Assert.assertEquals(entityIdentificationBO.getEntityInfo(), ENTITY_INFO);
		Assert.assertEquals(entityIdentificationBO.getEntityRevision(), ENTITY_REVISION);
		Assert.assertEquals(entityIdentificationBO.getEntityVersionDate(), ENTITY_VERSION_DATE);
		Assert.assertEquals(entityIdentificationBO.getLanguage(), LANGUAGE);
		Assert.assertEquals(entityIdentificationBO.getOriginator(), ORIGINATOR);
		Assert.assertEquals(entityIdentificationBO.getSpecVersion(), SPEC_VERSION);
		//Assert.assertNull(entityIdentificationBO.getSSN());
	}

	@Test(expectedExceptions = {EntityNotRegisteredException.class})
	public void testGetEntityInformationWithInvalidCloudId() throws Exception{
		when(entityDeviceRepository.getDeviceWithDeviceAdditionalInfo(any(String.class))).thenReturn(null);
		deviceRegistration.getEntityIdentificationBO("cloudId");
	}

	@Test(dataProvider = "resetCounterInputs")
	public void testDeviceReRegistrationReregFlowsWithMessageRetriableException(int counterFromPayload, int counterFromDB) throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(counterFromPayload);
		//Mockito.doNothing().when(ssnValidator).validateAndPersistSSN(any(String.class), any(String.class));
		Mockito.doThrow(new MessageStreamRetriableException("test")).when(eventProducer).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityDevice(counterFromDB));
		EntityConfigurationBO config = deviceRegistration.registerEntity(entityIdentificationBO2);
		Mockito.verify(entityDeviceRepository).delete(any(EntityDevice.class));
		Mockito.verify(entityDeviceRepository).save(any(EntityDevice.class));
		//Mockito.verify(threadPoolExecutor).execute(any(Runnable.class));
		Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		Assert.assertNotNull(config.getCloudId());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

}
