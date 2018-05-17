package com.hp.wpp.avatar.registration.device.processor;

import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.registration.device.repository.DeviceServiceInstanceRepository;
import com.hp.wpp.avatar.registration.device.repository.EntityDeviceRepository;
import com.hp.wpp.http.WppHttpClient;
import com.hp.wpp.ssn.SignedSerialNumberValidator;
import junit.framework.Assert;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.concurrent.ThreadPoolExecutor;

@ContextConfiguration(locations = "/applicationContext-podDevice-test.xml")
public class IntegrationTest extends AbstractTestNGSpringContextTests {
	
	private static final String TEST_DOMAIN = "test-domain";
	private static final String ENTITY_MODEL = "entity-model";
	private static final String ENTITY_REVISION = "entity-revision";
	private DeviceRegistrationProcessor deviceRegistration;
	
	@Mock private WppHttpClient wppHttpClient;
	
	@Mock private ThreadPoolExecutor threadPoolExecutor;
	@Autowired
	private EntityDeviceRepository entityDeviceRepository;
	@Autowired
	private DeviceServiceInstanceRepository serviceInstanceRepository;
	@Autowired 
	private SignedSerialNumberValidator ssnValidator;
	
//	@Test
	public void testDevice() throws Exception{
	
		deviceRegistration = new DeviceRegistrationProcessor(wppHttpClient,threadPoolExecutor);
		deviceRegistration.setEntityDeviceRepository(entityDeviceRepository);
		deviceRegistration.setDeviceServiceInstanceRepository(serviceInstanceRepository);
		deviceRegistration.setSsnValidator(ssnValidator);
		EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
		EntityConfigurationBO config = null;
		entityIdentificationBO.setCountryAndRegionName("india");
		entityIdentificationBO.setEntityAdditionalIds("hello12");
		entityIdentificationBO.setEntityDomain(TEST_DOMAIN);
		entityIdentificationBO.setEntityId("PA12345678");
		entityIdentificationBO.setEntityInfo("bull");
		entityIdentificationBO.setEntityModel(ENTITY_MODEL);
		entityIdentificationBO.setEntityName("my_name");
		entityIdentificationBO.setEntityRevision(ENTITY_REVISION);
		entityIdentificationBO.setEntityVersionDate("01-01-2015");
		entityIdentificationBO.setLanguage("en");
		entityIdentificationBO.setOriginator("wpp");
		entityIdentificationBO.setResetCounter(0);
		entityIdentificationBO.setSpecVersion("1.0");
		//entityIdentificationBO.setSSN("EQADEABF-PA12345678");
		entityIdentificationBO.setEntityUUID("uuid");
		config = deviceRegistration.registerEntity(entityIdentificationBO);
		Assert.assertEquals("1.0",config.getSpecVersion());
	}
	

}
