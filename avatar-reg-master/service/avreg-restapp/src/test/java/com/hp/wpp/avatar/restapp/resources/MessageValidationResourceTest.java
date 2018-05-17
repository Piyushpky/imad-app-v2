package com.hp.wpp.avatar.restapp.resources;

import static com.jayway.restassured.RestAssured.given;

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
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.security.MessageValidator;
import com.hp.wpp.avatar.restapp.security.ValidatorBean;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.jayway.restassured.http.ContentType;

public class MessageValidationResourceTest extends BaseResourceTest {

	@InjectMocks
	private MessageValidationResource messageValidationResource;

	@Mock
	private static MessageValidator messageValidator;

	private static String mockCloudID = "mockCloudID";

	private static String mockAppID = "mockAppID";

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		super.setup();
	}

	@Override
	protected Object getResourceClassToBeTested() {
		return messageValidationResource;
	}

	@DataProvider(name = "message")
	public static Object[][] getMessage() throws Exception {

		String jsonRequestMessagePayload = new String(Files.readAllBytes(Paths
				.get(MessageValidationResourceTest.class.getClassLoader()
						.getResource("json/message.json").toURI())));

		return new Object[][] {
		// Happy path
		{ jsonRequestMessagePayload, 204 } };
	}

	@DataProvider(name = "invalid_message")
	public static Object[][] getInvalidMessage() throws Exception {

		String jsonRequestMessagePayload = new String(Files.readAllBytes(Paths
				.get(MessageValidationResourceTest.class.getClassLoader()
						.getResource("json/invalid_message.json").toURI())));

		return new Object[][] {
		// Happy path
		{ jsonRequestMessagePayload, 400 } };
	}

	@Test(dataProvider = "message")
	public void testValidMessage(String messageValidationPayload,
			int responseCode) throws PostcardNonRetriableException,
			EntityRegistrationNonRetriableException {
		Mockito.when(
				messageValidator.validate(Mockito.anyString(),
						Mockito.anyString(),
						(ValidatorBean) Mockito.anyObject())).thenReturn(
				createRegisteredEntityBO());

		given().contentType(ContentType.JSON)
				.body(messageValidationPayload)
				.post(ROOT_URL
						+ "/v1/entities/message_validation/application_id/cloud_id/"
						+ mockAppID + "/" + mockCloudID).then().assertThat()
				.statusCode(responseCode);

	}

	@Test(dataProvider = "invalid_message")
	public void testInvalidInput(String messageValidationPayload,
			int responseCode) throws PostcardNonRetriableException,
			EntityRegistrationNonRetriableException {
		Mockito.when(
				messageValidator.validate(Mockito.anyString(),
						Mockito.anyString(),
						(ValidatorBean) Mockito.anyObject())).thenReturn(
				createRegisteredEntityBO());
		
		given().contentType(ContentType.JSON)
				.body("")
				.post(ROOT_URL
						+ "/v1/entities/message_validation/application_id/cloud_id/"
						+ mockAppID + "/" + mockCloudID).then().assertThat()
				.statusCode(responseCode);

	}

	@Test(dataProvider = "invalid_message")
	public void testValidationFailure(String messageValidationPayload,
			int responseCode) throws PostcardNonRetriableException,
			EntityRegistrationNonRetriableException {

		Mockito.doThrow(EntityValidationException.class)
				.when(messageValidator)
				.validate(Mockito.anyString(), Mockito.anyString(),
						(ValidatorBean) Mockito.anyObject());

		given().contentType(ContentType.JSON)
				.body(messageValidationPayload)
				.post(ROOT_URL
						+ "/v1/entities/message_validation/application_id/cloud_id/"
						+ mockAppID + "/" + mockCloudID).then().assertThat()
				.statusCode(responseCode);

	}

	private RegisteredEntityBO createRegisteredEntityBO() {
		RegisteredEntityBO entityBO = new RegisteredEntityBO();
		entityBO.setCloudId(mockCloudID);
		String entityUUID = UUID.randomUUID().toString().replaceAll("-", "");
		entityBO.setEntityUUID(entityUUID);
		entityBO.setEntityDomain("entityDomain");
		entityBO.setEntityId("entityId");
		entityBO.setEntityType(EntityType.services.name());
		return entityBO;
	}

}
