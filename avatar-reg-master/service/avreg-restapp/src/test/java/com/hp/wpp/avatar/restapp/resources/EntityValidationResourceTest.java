package com.hp.wpp.avatar.restapp.resources;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restapp.security.AuthValidator;
import com.hp.wpp.avatar.restapp.security.ConnectivityAuthValidator;
import com.hp.wpp.avatar.restapp.security.ValidationHelper;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.cidgenerator.impl.CloudIdGeneratorImpl;
import com.hp.wpp.postcard.Postcard;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;

public class EntityValidationResourceTest extends BaseResourceTest {

	private String cloudId;

	private static final String APPLICATION_ID = "svc_connectivity";

	private static final String URL = ROOT_URL + "/v1/entities/validation/application_id/%s/%s";

	private static  final  String CON_URL = ROOT_URL + "/v1/entities/validation";

	private static final String AAA_URL = ROOT_URL + "/v1/entities/validation/application_id/%s";

	//private static final String APPLICATION_KEY = "c3ZjX2Nvbm5lY3Rpdml0eTphdmF0YXI=";

	@Mock
	private Postcard postcard;

	@InjectMocks
	private EntityValidationResource entityValidationResource;

	@Mock
	private AvatarApplicationConfig appConfig;
	
	@Mock
	private EntityRegistrationProcessor entityRegistrationProcessor;

	@Mock
	private ConnectivityAuthValidator connectivityAuthValidator;

	private ValidationHelper validationHelper;
	
	private EntityTypeProcessorMap entityTypeProcessorMap;
	
	private CloudIdGenerator cloudIdGenerator;

	
	@BeforeMethod
	public void setup()	{

		MockitoAnnotations.initMocks(this);
		super.setup();
		
		cloudId = cloudIdGenerator.newCloudID((short)1, (byte)0);
		
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(true);
	}

	@Override
	protected Object getResourceClassToBeTested() {
		Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<EntityType, EntityRegistrationProcessor>();
		entityTypeToProcessorMap.put(EntityType.devices,entityRegistrationProcessor);
		entityTypeToProcessorMap.put(EntityType.services,entityRegistrationProcessor);

		entityTypeProcessorMap = new EntityTypeProcessorMap();
		entityTypeProcessorMap.setEntityTypeToProcessorMap(entityTypeToProcessorMap);

		validationHelper=new ValidationHelper();
		validationHelper.setCloudIdGenerator(cloudIdGenerator);
		validationHelper.setEntityTypeProcessorMap(entityTypeProcessorMap);

		cloudIdGenerator = new CloudIdGeneratorImpl();
		ConnectivityAuthValidator connectivityAuthValidator = new ConnectivityAuthValidator();
		connectivityAuthValidator.setAvatarApplicationConfig(appConfig);
		connectivityAuthValidator.setPostcard(postcard);
		connectivityAuthValidator.setValidationHelper(validationHelper);
		entityValidationResource.setConnectivityAuthValidator(connectivityAuthValidator);


		AuthValidator authValidator=new AuthValidator();
		authValidator.setAvatarApplicationConfig(appConfig);
		authValidator.setPostcard(postcard);
		authValidator.setValidationHelper(validationHelper);
		entityValidationResource.setEntityValidator(authValidator);
		entityValidationResource.setAvatarApplicationConfig(appConfig);
		entityValidationResource.setValidationHelper(validationHelper);
		return entityValidationResource;
	}
	
	@Test
	public void testValidEntityValidationResource() throws Exception {
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		invokeRestAPIAndVerifySuccessResponseCode(String.format(URL, cloudId, APPLICATION_ID), createHttpHeaders(cloudId+":"+cloudId+"_key"));
	}
	
	@Test
	public void testValidateResourceWithCustomHeaderInDevEnvironment() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		invokeRestAPIAndVerifySuccessResponseCode(String.format(URL, cloudId, APPLICATION_ID), createHttpHeaders(cloudId+":"+cloudId+"_key"));
	}

	private RegisteredEntityBO createRegisteredEntityBO() {
		RegisteredEntityBO entityBO = new RegisteredEntityBO();
		entityBO.setCloudId(cloudId);
		entityBO.setEntityDomain("entityDomain");
		entityBO.setEntityId("entityId");
		entityBO.setEntityType(EntityType.services.name());
		return entityBO;
	}
	
	@Test
	public void testValidateResourceWithEntityTypeNotFound() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBOWithoutEntityType());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		invokeRestAPIAndVerifyResponseCode(String.format(URL, cloudId, APPLICATION_ID), createHttpHeaders( cloudId+":"+cloudId+"_key"), Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}
	
	private RegisteredEntityBO createRegisteredEntityBOWithoutEntityType() {
		RegisteredEntityBO entityBO = new RegisteredEntityBO();
		entityBO.setCloudId(cloudId);
		entityBO.setEntityDomain("entityDomain");
		entityBO.setEntityId("entityId");
		return entityBO;
	}
	
	@Test
	public void testWhenInvalidCloudId() throws Exception {
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenThrow(new EntityNotRegisteredException("invalid cloudid"));
		invokeRestAPIAndVerifyResponseCode(String.format(URL, cloudId, APPLICATION_ID), createHttpHeaders( cloudId+":"+cloudId+"_key"), Status.NOT_FOUND.getStatusCode());
	}
	
	@Test
	public void testWhenPostCardCredentialMismatch() throws Exception {
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Mockito.when(appConfig.getAvatarApplicationID()).thenReturn("svc_avatar");
		invokeRestAPIAndVerifyResponseCode(String.format(URL, cloudId, APPLICATION_ID), createHttpHeaders( cloudId+":"+cloudId+"_key"), Status.NOT_FOUND.getStatusCode());
	}
	
	@DataProvider(name = "invalidAuthHeaders")
	public Object[][] getInvalidAuthHeaders(){
		return new Object[][]{
			{ "cloudId", Status.NOT_FOUND.getStatusCode()}, // invalid cloud credentials
			{ cloudId+":"+"cloudId"+"_key", Status.NOT_FOUND.getStatusCode()}// invalid cloud credentials
		};
	}
	
	@Test(dataProvider = "invalidAuthHeaders")
	public void testExceptionFlows(String customAuthorizationValue, int expectedStatusCode) throws Exception{
		Mockito.when(appConfig.getAvatarApplicationID()).thenReturn("svc_avatar");
		invokeRestAPIAndVerifyResponseCode(String.format(URL, "cloudId", APPLICATION_ID), createHttpHeaders( customAuthorizationValue), expectedStatusCode);
	}
	
	
	@DataProvider(name="erroredCloudId")
	public Object[][] getErroredCloudId(){
		return new Object[][]{
				{new CloudIdGeneratorImpl().newCloudID((short)1, (byte)3),Status.INTERNAL_SERVER_ERROR.getStatusCode()}, // Invalid entityType=3
				{"cloudId", Status.NOT_FOUND.getStatusCode()}
		};
	}
	@Test(dataProvider="erroredCloudId")
	public void testCloudIdParsingError(String inputCloudId, int expectedStatusCode) throws EntityRegistrationNonRetriableException{
		String customAuthActualValue = inputCloudId+":"+"cloudId"+"_key";
		invokeRestAPIAndVerifyResponseCode(String.format(URL, inputCloudId , APPLICATION_ID), createHttpHeaders(customAuthActualValue), expectedStatusCode);
	}
	
	@Test
	public void testValidateResourceWithoutCustomHeaderInDevEnvironment() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		invokeRestAPIAndVerifySuccessResponseCode(String.format(URL, cloudId, APPLICATION_ID));
	}

	@Test
	public void testvalidateConnectivityEntityHeader() throws Exception {
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);

		invokeRestAPIAndVerifyResponseCode(String.format(CON_URL), createConnectivityHttpHeaders(cloudId+":"+cloudId+"_key",APPLICATION_ID),200);

	}

	@DataProvider(name = "ConnectivityInCorrectAuthHeaders")
	public Object[][] getInCorrectConnectivityAuthHeaders(){
		return new Object[][]{
				{ "cloudId","application_id", Status.UNAUTHORIZED.getStatusCode()}, // invalid cloud credentials
				{  cloudId + ":"+cloudId+"_key","application_id", Status.INTERNAL_SERVER_ERROR.getStatusCode()},// invalid cloud credentials*/
		};
	}

	@Test
	public void testValidateConnectivityAuthHeader() throws Exception {
		String customAuth =  cloudId + ":"+cloudId+"_key";
		String applicationId = APPLICATION_ID;
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		connectivityAuthValidator.validateConnectivityCustomAuthHeader(customAuth,applicationId);
	}

	@Test(dataProvider = "ConnectivityInCorrectAuthHeaders")
	public void testvalidateConnectivityEntityHeader(String customAuthorizationValue, String applicationHeader, int expectedStatusCode) throws Exception {

		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Mockito.when(postcard.isValidKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		invokeRestAPIAndVerifyResponseCode(String.format(CON_URL), createConnectivityHttpHeaders(customAuthorizationValue,applicationHeader),expectedStatusCode);

	}


	@Test
	public void testValidateAAAResourceWithoutCustomHeaderInDevEnvironment() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Header header1 = new Header("x-hp-cloud-id", cloudId);
		Headers headers = new Headers(header1);
		invokeRestAPIAndVerifySuccessResponseCode(String.format(AAA_URL, APPLICATION_ID),headers);
	}


	@Test
	public void testValidateResourceWithoutCustomHeaderInStageEnvironment() throws Exception{
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		invokeRestAPIAndVerifyResponseCode(String.format(URL, cloudId, APPLICATION_ID),  Status.NOT_FOUND.getStatusCode());
	}

	@Test
	public void testValidateAAAResourceWithoutCustomHeaderInStageEnvironment() throws Exception{
		Mockito.when(entityRegistrationProcessor.getRegisteredEntity(cloudId)).thenReturn(createRegisteredEntityBO());
		Header header1 = new Header("x-hp-cloud-id", cloudId);
		Headers headers = new Headers(header1 );
		invokeRestAPIAndVerifyResponseCode(String.format(AAA_URL, APPLICATION_ID), headers, Status.UNAUTHORIZED.getStatusCode());
	}
	
	
	private Headers createHttpHeaders(String customAuthActualValue) {
		Header header1 = new Header(EntityValidationResource.X_HP_CUSTOM_AUTH, "Basic "+Base64.encodeBase64String(customAuthActualValue.getBytes()));
		Headers headers = new Headers(header1);
		return headers;
	}

	private Headers createConnectivityHttpHeaders(String customAuthActualValue, String  applicationId) {
		Header header1 = new Header(EntityValidationResource.X_HP_CUSTOM_AUTH, "Basic "+Base64.encodeBase64String(customAuthActualValue.getBytes()));
		Header header2 = new Header(EntityValidationResource.X_HP_APPLICATION_ID, applicationId);
		Headers headers = new Headers(header1,header2);
		return headers;
	}

	private void invokeRestAPIAndVerifyResponseCode(String uri, int statusCode) {
		given()
		.post(uri)
		.then()
		.assertThat()
		.statusCode(statusCode);
	}
	
	private void invokeRestAPIAndVerifyResponseCode(String uri, Headers headers, int statusCode) {
		given()
		.headers(headers)
		.when()
		.post(uri)
		.then()
		.assertThat()
		.statusCode(statusCode);
	}
	
	private void invokeRestAPIAndVerifySuccessResponseCode(String uri, Headers headers) {
		given()
		.headers(headers)
		.when()
		.post(uri)
		.then()
		.assertThat()
		.statusCode(Status.OK.getStatusCode()).and().header("x-hp-entity-type", EntityType.services.name());
	}
	
	private void invokeRestAPIAndVerifySuccessResponseCode(String uri) {
		given()
		.when()
		.post(uri)
		.then()
		.assertThat()
		.statusCode(Status.OK.getStatusCode()).and().header("x-hp-entity-type", EntityType.services.name());
	}
	
}
