package com.hp.wpp.avatar.restapp.resources;

import com.hp.wpp.avatar.framework.exceptions.DeviceBlacklistException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.DeviceBlacklist.DeviceBlacklist;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.security.AuthValidator;
import com.hp.wpp.avatar.restapp.security.ValidationHelper;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jayway.restassured.RestAssured.given;

public class ResetCredentialResourceTest extends BaseResourceTest {

	private static final String URL = "/v1/entities/credentials/%s";
	private static final String id = "seaouthpk3";
	@Mock
	private Postcard postcard;

	@Mock
	private AuthValidator entitySecurityManager;

	@Mock
	private AvatarApplicationConfig appConfig;

	@Mock
	private DeviceBlacklist deviceBlacklist;

	@Mock
	private ValidationHelper validationHelper;

	@InjectMocks
	private ResetCredentialResource resetCredentialResource;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		super.setup();
	}

	@Override
	protected Object getResourceClassToBeTested() {
		return resetCredentialResource;
	}

	@DataProvider(name = "validPostcardPayloads")
	public Object[][] validPostcardPayloads() throws Exception {
		return new Object[][] { { new String(Files.readAllBytes(Paths
				.get(EntityResourceTest.class.getClassLoader()
						.getResource("json/reset_credential_payload.json")
						.toURI()))) } };
	}

	@Test( dataProvider = "validPostcardPayloads")
	public void testSuccessRegistrationProcessWithBlacklistCheck(String postcardPayload) throws Exception{
		Header header2 = new Header("User-Agent", "A B C D");
		Headers headers = new Headers( header2);
		Mockito.when(deviceBlacklist.isBlackListEnabled(Mockito.anyBoolean())).thenReturn(true);
		Mockito.doThrow(DeviceBlacklistException.class).when(deviceBlacklist).isBlacklist((String) Mockito.anyString(),(String) Mockito.anyString());
		invokeRestAPIAndVerifyFailureResponseForBlacklist(postcardPayload, headers,403);
	}

	@Test( dataProvider = "validPostcardPayloads")
	public void testSuccessRegistrationProcessWithEmptyHeaderBlacklistCheck(String postcardPayload) throws Exception{
		Header header2 = new Header("User-Agent", " ");
		Headers headers = new Headers(header2);
		Mockito.when(deviceBlacklist.isBlackListEnabled(Mockito.anyBoolean())).thenReturn(true);
		Mockito.doThrow(DeviceBlacklistException.class).when(deviceBlacklist).isBlacklist((String) Mockito.anyString(),(String) Mockito.anyString());
		invokeRestAPIAndVerifySuccessResponseForBlacklist(postcardPayload, headers);
	}

	@Test(dataProvider = "validPostcardPayloads")
	public void testValidPostcardPayload(String postcardPayload) {

		invokeRestAPIAndVerifySuccessResponse(postcardPayload);
	}

	@DataProvider(name = "invalidPostcardPayloads")
	public Object[][] invalidPostcardPayloads() throws Exception {
		return new Object[][] { { new String("") } };
	}

	@Test(dataProvider = "invalidPostcardPayloads")
	public void testInvalidPostcardPayload(String postcardPayload) {
		invokeRestAPIAndVerifyFailureResponse(postcardPayload, 400);
	}

	@DataProvider(name = "inducePostcardExceptions")
	public Object[][] exceptionInput() throws Exception {
		return new Object[][] { { new PostcardEntityNotFoundException(), 404 },
				{ new PostcardNonRetriableException(), 400 },
				{ new RuntimeException(), 503 } };
	}

	@Test(dataProvider = "inducePostcardExceptions")
	public void testPostcardExceptions(Exception induceException,
			int expectedStatusCode) throws Exception {
		RegisteredEntityBO registeredEntityBO = new RegisteredEntityBO();
		Mockito.when(validationHelper.validateCloudId(Mockito.anyString()))
				.thenReturn(registeredEntityBO);
		Mockito.doThrow(induceException).when(postcard)
				.refreshSharedSecret(Mockito.anyString());
		String postcardPayload = new String(Files.readAllBytes(Paths
				.get(EntityResourceTest.class.getClassLoader()
						.getResource("json/reset_credential_payload.json")
						.toURI())));
		invokeRestAPIAndVerifyFailureResponse(postcardPayload,
				expectedStatusCode);
	}

	@DataProvider(name = "induceCPIDExceptions")
	public Object[][] induceCPIDException() {
		return new Object[][] { {
				new EntityValidationException("CloudId Exception"), 404 } };
	}

	@Test(dataProvider = "induceCPIDExceptions")
	public void testInvalidCPIDExceptions(Exception induceException,
			int statusCode) throws Exception {
		Mockito.doThrow(induceException).when(validationHelper)
				.validateCloudId(Mockito.anyString());
		Mockito.doNothing().when(postcard)
				.refreshSharedSecret(Mockito.anyString());
		String postcardPayload = new String(Files.readAllBytes(Paths
				.get(EntityResourceTest.class.getClassLoader()
						.getResource("json/reset_credential_payload.json")
						.toURI())));
		invokeRestAPIAndVerifyFailureResponse(postcardPayload, statusCode);
	}

	public void invokeRestAPIAndVerifySuccessResponse(String postcardPayload) {
		given().contentType(MediaType.APPLICATION_JSON).body(postcardPayload)
				.post(ROOT_URL + String.format(URL, id)).then().assertThat()
				.statusCode(200);
	}

	public void invokeRestAPIAndVerifySuccessResponseForBlacklist(String postcardPayload, Headers headers) {
		given().headers(headers).contentType(MediaType.APPLICATION_JSON).body(postcardPayload)
				.post(ROOT_URL + String.format(URL, id)).then().assertThat()
				.statusCode(200);
	}

	public void invokeRestAPIAndVerifyFailureResponseForBlacklist(String postcardPayload,
													  Headers headers,int statusCode) {
		given().headers(headers).contentType(MediaType.APPLICATION_JSON).body(postcardPayload)
				.post(ROOT_URL + String.format(URL, id)).then().assertThat()
				.statusCode(statusCode);
	}

	public void invokeRestAPIAndVerifyFailureResponse(String postcardPayload,
			int statusCode) {
		given().contentType(MediaType.APPLICATION_JSON).body(postcardPayload)
				.post(ROOT_URL + String.format(URL, id)).then().assertThat()
				.statusCode(statusCode);
	}
}