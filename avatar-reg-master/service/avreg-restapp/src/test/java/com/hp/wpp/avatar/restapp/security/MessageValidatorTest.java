package com.hp.wpp.avatar.restapp.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRequestException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

public class MessageValidatorTest {

	@InjectMocks
	private MessageValidator messageValidator;

	@Mock
	private Postcard postcard;

	@Mock
	private ValidationHelper validationHelper;

	private static String mockCloudID = "adgy738fadsy638faygy798k";
	private static String mockAppID = "svc_smartcloudprint_connector_gallery";
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

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@DataProvider(name = "message")
	public static Object[][] getMessage() throws Exception {

		String jsonRequestMessagePayload = new String(Files.readAllBytes(Paths
				.get(MessageValidatorTest.class.getClassLoader()
						.getResource("json/message.json").toURI())));

		return new Object[][] {
		// Happy path
		{ jsonRequestMessagePayload } };
	}

	@DataProvider(name = "invalid_message")
	public static Object[][] getInvalidMessage() throws Exception {

		String jsonRequestMessagePayload = new String(Files.readAllBytes(Paths
				.get(MessageValidatorTest.class.getClassLoader()
						.getResource("json/invalid_message.json").toURI())));

		return new Object[][] {

		{ jsonRequestMessagePayload } };
	}
	
	@DataProvider(name = "invalid_signature_message")
	public static Object[][] getInvalidSignatureMessage() throws Exception {

		String jsonRequestMessagePayload = new String(Files.readAllBytes(Paths
				.get(MessageValidatorTest.class.getClassLoader()
						.getResource("json/invalid_signature_message.json").toURI())));

		return new Object[][] {

		{ jsonRequestMessagePayload } };
	}

	@Test(dataProvider = "message")
	public void testValidateMessage(String payload)
			throws EntityRegistrationNonRetriableException,
			PostcardEntityNotFoundException, PostcardNonRetriableException {
		ValidatorBean validatorBean = new ValidatorBean();
		validatorBean.setMessgae(payload);
		Mockito.when(validationHelper.validateCloudId(Mockito.anyString()))
				.thenReturn(createRegisteredEntityBO());
		Mockito.when(
				postcard.generateEntityKey(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).thenReturn(
				"secret");
		messageValidator.validate(mockCloudID, mockAppID, validatorBean);
	}
	
	@Test(dataProvider = "message",expectedExceptions = { InvalidRequestException.class })
	public void testCloudIdUrlAndPayloadMismatch(String payload)
			throws EntityRegistrationNonRetriableException,
			PostcardEntityNotFoundException, PostcardNonRetriableException {
		ValidatorBean validatorBean = new ValidatorBean();
		validatorBean.setMessgae(payload);
		Mockito.when(validationHelper.validateCloudId(Mockito.anyString()))
				.thenReturn(createRegisteredEntityBO());
		Mockito.when(
				postcard.generateEntityKey(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).thenReturn(
				"secret");
		messageValidator.validate("cid", mockAppID, validatorBean);
	}

	@Test(dataProvider = "invalid_message", expectedExceptions = { InvalidRequestException.class })
	public void testInvalidRequestMessage(String payload)
			throws EntityRegistrationNonRetriableException,
			PostcardEntityNotFoundException, PostcardNonRetriableException {
		ValidatorBean validatorBean = new ValidatorBean();
		validatorBean.setMessgae(payload);
		Mockito.when(validationHelper.validateCloudId(Mockito.anyString()))
				.thenReturn(createRegisteredEntityBO());
		Mockito.when(
				postcard.generateEntityKey(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).thenReturn(
				"secret");
		messageValidator.validate(mockCloudID, mockAppID,validatorBean);
	}
	
	@Test(dataProvider = "invalid_signature_message", expectedExceptions = { EntityValidationException.class })
	public void testValidationFailure(String payload)
			throws EntityRegistrationNonRetriableException,
			PostcardEntityNotFoundException, PostcardNonRetriableException {
		ValidatorBean validatorBean = new ValidatorBean();
		validatorBean.setMessgae(payload);
		Mockito.when(validationHelper.validateCloudId(Mockito.anyString()))
				.thenReturn(createRegisteredEntityBO());
		Mockito.when(
				postcard.generateEntityKey(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).thenReturn(
				"secret");
		messageValidator.validate(mockCloudID, mockAppID,validatorBean);
	}

	private RegisteredEntityBO createRegisteredEntityBO() {
		RegisteredEntityBO entityBO = new RegisteredEntityBO();
		entityBO.setCloudId(mockCloudID);
		String entityUUID = UUID.randomUUID().toString().replaceAll("-", "");
		entityBO.setEntityUUID(entityUUID);
		entityBO.setEntityDomain("entityDomain");
		entityBO.setEntityId("entityId");
		entityBO.setEntityType(EntityType.devices.name());
		return entityBO;
	}
}
