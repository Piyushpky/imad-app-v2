package com.hp.wpp.avatar.restapp.security;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.cidgenerator.impl.CloudIdGeneratorImpl;
import com.hp.wpp.postcard.Postcard;

public class ValidationHelperTest {

	private String cloudId;

	@InjectMocks
	private ValidationHelper validationHelper;

	@Mock
	private EntityRegistrationProcessor entityRegistrationProcessor;

	@Mock
	private Postcard postcard;

	private EntityTypeProcessorMap entityTypeProcessorMap;

	private CloudIdGenerator cloudIdGenerator;

	public static final String ENTITY_NAME = "entity-name";
	public static final String ENTITY_INFO = "entity-info";
	public static final String ENTITY_VERSION_DATE = "01-01-2015";
	public static final String LANGUAGE = "en";
	public static final String ORIGINATOR = "ews";
	public static final String SPEC_VERSION = "1.0";
	public static final String COUNTRY = "india";
	public static final String ENTITY_ADDITIONAL_IDS = "entity-additional-ids";

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		cloudIdGenerator = new CloudIdGeneratorImpl();

		Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<EntityType, EntityRegistrationProcessor>();
		entityTypeToProcessorMap.put(EntityType.devices,
				entityRegistrationProcessor);
		entityTypeToProcessorMap.put(EntityType.services,
				entityRegistrationProcessor);

		entityTypeProcessorMap = new EntityTypeProcessorMap();
		entityTypeProcessorMap
				.setEntityTypeToProcessorMap(entityTypeToProcessorMap);

		validationHelper.setCloudIdGenerator(cloudIdGenerator);
		validationHelper.setEntityTypeProcessorMap(entityTypeProcessorMap);
		cloudId = cloudIdGenerator.newCloudID((short) 1, (byte) 0);
		;
	}

	@Test
	public void testValidCloudId() throws Exception {

		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId))
				.thenReturn(createRegisteredEntityBO());

		RegisteredEntityBO entityBO = validationHelper.validateCloudId(cloudId);

		assertEquals(entityBO.getEntityType(), EntityType.services.name());
	}

	@Test(expectedExceptions = { EntityRegistrationNonRetriableException.class })
	public void testInvalidcloudIdWithInvalidEntityType() throws Exception {
		cloudId = cloudIdGenerator.newCloudID((short) 1, (byte) 3); // Invalid
																	// entitytype
																	// =3
		validationHelper.validateCloudId(cloudId);
	}

	@Test(expectedExceptions = { EntityRegistrationNonRetriableException.class })
	public void testNotRegisteredCloudId() throws Exception {
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId))
				.thenThrow(
						new EntityNotRegisteredException("invalid cloudId: "
								+ cloudId));
		validationHelper.validateCloudId(cloudId);
	}

	private RegisteredEntityBO createRegisteredEntityBO() {
		RegisteredEntityBO entityBO = new RegisteredEntityBO();
		entityBO.setCloudId(cloudId);
		entityBO.setEntityDomain("entityDomain");
		entityBO.setEntityId("entityId");
		entityBO.setEntityType(EntityType.services.name());
		return entityBO;
	}

}
