package com.hp.wpp.avatar.registration.service.processor;

import com.hp.wpp.avatar.framework.common.config.RegistrationProcessorConfig;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.service.entities.ServiceAdditionalInfo;
import com.hp.wpp.avatar.registration.service.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.service.repository.EntityServiceRepository;
import com.hp.wpp.avatar.registration.service.repository.ServiceInstanceRepository;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.cidgenerator.InvalidCloudIdException;
import com.hp.wpp.cidgenerator.ParsedCloudID;
import com.hp.wpp.cidgenerator.impl.CloudIdGeneratorImpl;
import com.hp.wpp.stream.messages.client.EventProducer;
import com.hp.wpp.stream.messages.exception.MessageStreamNonRetriableException;
import org.apache.commons.beanutils.BeanUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;



public class ServiceRegistrationProcessorTest {


	private static final String TEST_DOMAIN = "test_domain";


	private static final String ENTITY_MODEL = "entity-model";

	private static final String ENTITY_REVISION = "entity-revision";

	@Mock private EntityServiceRepository entityServiceRepository;

	@Mock private ServiceInstanceRepository serviceInstanceRepository;
	
	@Mock private RegistrationProcessorConfig registrationProcessorConfig;

	@Mock private EventProducer eventProducer;

	private ServiceRegistrationProcessor serviceRegistration;

	private EntityIdentificationBO entityIdentification;

	private CloudIdGenerator cloudIdGenerator;

	String entityId;

	@BeforeMethod
	public void setUp()  throws Exception{
		MockitoAnnotations.initMocks(this);
		
		
		cloudIdGenerator = new CloudIdGeneratorImpl();
		serviceRegistration = new ServiceRegistrationProcessor();
		serviceRegistration.setEntityServiceRepository(entityServiceRepository);
		serviceRegistration.setServiceInstanceRepository(serviceInstanceRepository);
		serviceRegistration.setCloudIdGenerator(cloudIdGenerator);
		serviceRegistration.setPodCode((short)1);
		serviceRegistration.setRegistrationProcessorConfig(registrationProcessorConfig);
		serviceRegistration.setDeRegistrationStreamName("DeleteStream");
		serviceRegistration.setEventProducer(eventProducer);

		entityIdentification = createEntityIdentificationBO(1);
		ServiceInstance serviceInstance = new ServiceInstance();
		serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/printerconfig");

		Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(),Mockito.anyString())).thenReturn(serviceInstance);
		Mockito.when(registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()).thenReturn(true);
	}

	private EntityIdentificationBO createEntityIdentificationBO(int resetCounter)throws Exception {
		EntityService service = createEntityService(resetCounter);

		EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
		BeanUtils.copyProperties(entityIdentificationBO, service);
		BeanUtils.copyProperties(entityIdentificationBO, service.getServiceAdditionalInfo());

		return entityIdentificationBO;
	}

	private EntityService createEntityService(int resetCounter) {
		EntityService service = new EntityService();
		String cloudId = UUID.randomUUID().toString().replaceAll("-", "");
		entityId = UUID.randomUUID().toString().replaceAll("-", "");
		service.setCloudId(cloudId);
		service.setEntityId(entityId);
		service.setEntityUUID(entityId);
		service.setEntityDomain(TEST_DOMAIN);
		service.setEntityModel(ENTITY_MODEL);
		service.setEntityName("entity-name");
		service.setResetCounter(resetCounter);

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

	@Test
	public void testServiceRegistrationSuccess() throws Exception {
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentification);

		Mockito.verify(entityServiceRepository).persistEntityServices((EntityService)Mockito.anyObject(), (HashedEntityIdentifier)Mockito.anyObject());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
		verifyEntityServiceType(config.getCloudId());
	}
	
	@Test
	public void testServiceRegistrationSuccessWithCredentialcheck() throws Exception {
		
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentification);

		Mockito.verify(entityServiceRepository).persistEntityServices((EntityService)Mockito.anyObject(), (HashedEntityIdentifier)Mockito.anyObject());
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
	public void testServiceRegistrationSuccessWithCredentialcheckAndAppconfigDisabled() throws Exception {
		
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
		Mockito.when(registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()).thenReturn(false);
		
		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentification);

		Mockito.verify(entityServiceRepository).persistEntityServices((EntityService)Mockito.anyObject(), (HashedEntityIdentifier)Mockito.anyObject());
		Assert.assertEquals(config.getConfigurations().size(), 2);
		
		boolean urlNotExist=true;
		for(LinkBO linkBO:config.getConfigurations()){
			if(linkBO.getRel().equals(ServiceInstanceType.CREDENTIAL_REFRESH.getValue())){
				urlNotExist=false;
			}
		}
		Assert.assertTrue(urlNotExist);
		
	}

	private void verifyEntityServiceType(String cloudId) throws Exception{
		ParsedCloudID parsedCloudID = cloudIdGenerator.parse(cloudId);
		Assert.assertEquals((int) parsedCloudID.getEntitytype(), EntityType.services.getValue());
	}

	@Test
	public void testServiceReRegistrationSuccess() throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(3);
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityService(1));

		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentificationBO2);

		Mockito.verify(entityServiceRepository).save((EntityService) Mockito.anyObject());
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
	public void testServiceReRegistrationFlows(int counterFromPayload, int counterFromDB) throws Exception {
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(counterFromPayload);
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityService(counterFromDB));
		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentificationBO2);

		Mockito.verify(entityServiceRepository).delete((Long)anyObject());
		Mockito.verify(entityServiceRepository).save(any(EntityService.class));
		Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		Assert.assertNotNull(config.getCloudId());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	@Test(dataProvider = "resetCounterInputs")
	public void testServiceReRegistrationFlowsWithNotificationCallException(int counterFromPayload, int counterFromDB) throws Exception {
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(counterFromPayload);
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityService(counterFromDB));
		Mockito.doThrow(new MessageStreamNonRetriableException("test")).when(eventProducer).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentificationBO2);

		Mockito.verify(entityServiceRepository).delete((Long)anyObject());
		Mockito.verify(entityServiceRepository).save(any(EntityService.class));
		Mockito.verify(eventProducer, Mockito.times(1)).sendNotification(Mockito.anyString(),new byte[]{ Mockito.anyByte()});
		Assert.assertNotNull(config.getCloudId());
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	@Test
	public void testServiceReRegistrationWithEqualOwnerShipCounter() throws Exception
	{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(1);
		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityService(1));

		EntityConfigurationBO config = serviceRegistration.registerEntity(entityIdentificationBO2);

		Mockito.verify(entityServiceRepository).updateServieAdditionalInfo(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class));
		Assert.assertEquals(config.getSpecVersion(), entityIdentification.getSpecVersion());
	}

	/*@Test(expectedExceptions={InvalidRegistrationDataException.class})
	public void testPrinterReRegistrationWitLessOwnershipCounter() throws Exception{
		EntityIdentificationBO entityIdentificationBO2 = createEntityIdentificationBO(0);

		when(entityServiceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(createEntityService(1));

		serviceRegistration.registerEntity(entityIdentificationBO2);
	}*/

	@Test
	public void testValidGetRegisteredEntity() throws Exception
	{
		when(entityServiceRepository.findByCloudId(any(String.class))).thenReturn(createEntityService(1));

		RegisteredEntityBO registeredEntity = serviceRegistration.getRegisteredEntity(any(String.class));
		Assert.assertNotNull(registeredEntity);
		Assert.assertEquals(registeredEntity.getEntityDomain(), TEST_DOMAIN);
		Assert.assertEquals(registeredEntity.getEntityId(), entityId);
	}



	@Test(expectedExceptions={EntityNotRegisteredException.class})
	public void testInvalidGetRegisteredEntity() throws Exception
	{
		when(entityServiceRepository.findByCloudId(any(String.class))).thenReturn(null);
		serviceRegistration.getRegisteredEntity(any(String.class));
	}

	@Test
		public void testCloudIdForService() throws InvalidCloudIdException
		{
			short podCode = 100;
			byte entityTypeValue = (byte)EntityType.services.getValue();
			CloudIdGeneratorImpl cloudIdGenerator = new CloudIdGeneratorImpl();
			String cloudId = cloudIdGenerator.newCloudID(podCode, entityTypeValue);
			ParsedCloudID cloudIdparsed = cloudIdGenerator.parse(cloudId);
			Assert.assertEquals(EntityType.services.getValue(), cloudIdparsed.getEntitytype());
			
	}


}
