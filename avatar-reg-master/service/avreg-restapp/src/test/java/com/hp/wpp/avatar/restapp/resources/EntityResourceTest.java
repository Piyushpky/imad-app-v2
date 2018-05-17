package com.hp.wpp.avatar.restapp.resources;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.DeviceBlacklistException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRegistrationDataException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.restapp.DeviceBlacklist.DeviceBlacklist;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restapp.security.PostcardSecurityManager;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.PostcardData.Message;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardCompression;
import com.hp.wpp.postcard.json.schema.PostcardEncryption;
import com.hp.wpp.ssn.exception.InvalidSSNCounterException;
import com.hp.wpp.ssn.exception.InvalidSSNException;
import com.hp.wpp.ssn.exception.InvalidSSNSignatureException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class EntityResourceTest extends BaseResourceTest{
	
    private static final String CLOUD_ID = "seoupthe3pk";
    private static final String ipAddress = "12.34.56.789,192.168.43.194";

	@Mock
    private Postcard postcard;
    
    @InjectMocks
    private EntityResource entityResource;
    
    private PostcardSecurityManager postcardSecurityManager;
    
    @Mock private AvatarApplicationConfig appConfig;
	@Mock
	private DeviceBlacklist deviceBlacklist;
    
    private EntityTypeProcessorMap entityTypeProcessorMap;
    @Mock private EntityRegistrationProcessor entityRegistrationProcessor;

    private static String postCardRegPayload;
	private static String regResponsePostcard;

	static {
		try {
			postCardRegPayload = new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("postcard/valid_postcard.json").toURI())));
			regResponsePostcard = new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("postcard/postcard_test_runtime.json").toURI())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeMethod
	public void setup()	{
		MockitoAnnotations.initMocks(this);
		super.setup();
		EntityConfigurationBO entityConfig  =new EntityConfigurationBO();
		entityConfig.setCloudId(CLOUD_ID);
		entityConfig.setSpecVersion("1.0");
		LinkBO config = new LinkBO();
		config.setHref("https://avatar.hpeprint.com/avatar/connectivityconfig/printers/seoupthe3pk");
		config.setRel("connectivity_config");
		entityConfig.getConfigurations().add(config );
		try {
			Mockito.when(entityRegistrationProcessor.registerEntity((EntityIdentificationBO)Mockito.anyObject())).thenReturn(entityConfig);
		} catch (EntityRegistrationNonRetriableException e) {
		}
		
		Mockito.when(appConfig.getAvatarRegistrationURL()).thenReturn("url");
		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(false);
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(true);
		
	}

	@Override
	protected Object getResourceClassToBeTested() {
		Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<EntityType, EntityRegistrationProcessor>();
		entityTypeToProcessorMap.put(EntityType.devices, entityRegistrationProcessor);
		entityTypeToProcessorMap.put(EntityType.services, entityRegistrationProcessor);
		
		entityTypeProcessorMap = new EntityTypeProcessorMap();
		entityTypeProcessorMap.setEntityTypeToProcessorMap(entityTypeToProcessorMap);
		entityResource.setEntityTypeProcessorMap(entityTypeProcessorMap);
		
		postcardSecurityManager = new PostcardSecurityManager();
		postcardSecurityManager.setPostcard(postcard);
		
		entityResource.setAvatarApplicationConfig(appConfig);
		entityResource.setPostcardSecurityManager(postcardSecurityManager);
		
        return entityResource;
    }
	
	@DataProvider(name="validRegistrationPayloads")
	public Object[][] validRegistrationPayloads() throws Exception{
		return new Object[][]{
				{getValidRegPayload()},
                {new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/device-reg-valid-LanguageChina.json").toURI())))},
				{new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-valid.json").toURI())))},
				{new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-valid-without-classifier.json").toURI())))},
				{new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-valid-without-date.json").toURI())))},
				{new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-valid-without-revision.json").toURI())))}
		};
	}

	private String getValidRegPayload() throws IOException, URISyntaxException {
		return new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/device-registration-valid.json").toURI())));
	}
	
	@Test(dataProvider="validRegistrationPayloads")
	public void testValidEntityRegistration(String regEntityIdentificationPayload) throws Exception{
		invokeRestAPIAndVerifySuccessResponse(regEntityIdentificationPayload);
	}
	
	@DataProvider(name="inValidRegistrationPayloads")
	public Object[][] inValidRegistrationPayloads() throws Exception{
		return new Object[][]{
			    {new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/device-registration-entity-worng-entitytype.json").toURI())))},
				{new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-domain-empty.json").toURI())))},
				{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-version-empty.json").toURI()))))},
				{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-version-null.json").toURI()))))},
				{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-date-wrong-format.json").toURI()))))},
				{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-with-wrong-fieldname.json").toURI()))))},
				{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-entity-classifier-mismatch.json").toURI()))))}
				//{new String(new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/device-registration-invalid-without-additionalIds.json").toURI()))))}
		};
	}
	//TODO need to verify invalid payload without additional-ids for device registrations
	@Test(dataProvider="inValidRegistrationPayloads")
	public void testEntityResourceWithInvalidPayload(String regEntityIdentificationPayload)throws Exception{
		invokeRestAPIAndVerifyFailureResponse(regEntityIdentificationPayload,Status.CONFLICT.getStatusCode());	
	}

	@Test
	public void testRegistrationFlowWithoutUUID()throws Exception{

		try {
			Mockito.doThrow(new InvalidRegistrationDataException("UUID not found")).when(entityRegistrationProcessor).validateEntityIdentificationBO((EntityIdentificationBO) Mockito.anyObject());
		} catch (EntityRegistrationNonRetriableException e) {
		}
		String regEntityIdentificationPayload = new String(Files.readAllBytes(Paths.get(EntityResourceTest.class.getClassLoader().getResource("entity-identification/service-registration-valid.json").toURI())));

		invokeRestAPIAndVerifyFailureResponse(regEntityIdentificationPayload,Status.CONFLICT.getStatusCode());

	}
	
	@Test
	public void testEntityResourceWithEmptyProcessorMap() throws Exception{
		Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<EntityType, EntityRegistrationProcessor>();
		entityTypeProcessorMap = new EntityTypeProcessorMap();
		entityTypeProcessorMap.setEntityTypeToProcessorMap(entityTypeToProcessorMap );
		entityResource.setEntityTypeProcessorMap(entityTypeProcessorMap);
		
		String regEntityIdentificationPayload = getValidRegPayload();
		invokeRestAPIAndVerifyFailureResponse(regEntityIdentificationPayload,Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}
	
	@DataProvider(name="induceSSNExceptions")
	public Object[][] exceptionInput(){
		return new Object[][]{
				{new InvalidRegistrationDataException(new InvalidSSNSignatureException()),Status.CONFLICT.getStatusCode()},
				{new InvalidRegistrationDataException(new InvalidSSNCounterException()), Status.CONFLICT.getStatusCode()},
				{new InvalidRegistrationDataException(new InvalidSSNException()), Status.CONFLICT.getStatusCode()}
		};
	}

	@Test(dataProvider="induceSSNExceptions")
	public void testRegistrationSSNExceptionFlow(Exception induceException,int expectedStatusCode) throws Exception{
		
		String regPayload = getValidRegPayload();
		
		Mockito.when(entityRegistrationProcessor.registerEntity((EntityIdentificationBO)Mockito.anyObject())).thenThrow(induceException);
		invokeRestAPIAndVerifyFailureResponse(regPayload, expectedStatusCode);
		
	}
	
	@Test
	public void testRegistrationPostcardExceptionFlow() throws Exception{
		String regPayload = getValidRegPayload();
		
		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(postcard.validateAndDecryptPostcard(Mockito.anyString())).thenThrow(new PostcardNonRetriableException());
		invokeRestAPIAndVerifyFailureResponse(regPayload, Status.BAD_REQUEST.getStatusCode());
		
	}
	
	public EntityResourceTest invokeRestAPIAndVerifySuccessResponse(String regPayload) {
		given()
	        .contentType(ContentType.JSON)
	        .body(regPayload)
	        .post(ROOT_URL + "/v1/entities")
	        .then()
        .assertThat()
            .statusCode(Status.CREATED.getStatusCode()).and()
            .body("version", equalTo("1.0"))
            .header(EntityResource.LOCATION_HEADER, "url");
		return this;
	}
	
	public EntityResourceTest invokeRestAPIAndVerifySuccessResponse(String regPayload, Headers headers) {
		given()
			.headers(headers)
	        .contentType(ContentType.JSON)
	        .body(regPayload)
	        .post(ROOT_URL + "/v1/entities")
	        .then()
        .assertThat()
            .statusCode(Status.CREATED.getStatusCode()).and()
            .body("version", equalTo("1.0"))
            .header(EntityResource.LOCATION_HEADER, "url");
		return this;
	}
	
	
	private void invokeRestAPIAndVerifyFailureResponse(String regPayload,int statusCode) {
		given()
	        .contentType(ContentType.JSON)
	        .body(regPayload)
	        .post(ROOT_URL + "/v1/entities")
	        .then()
        .assertThat().
            statusCode(statusCode);
	}
	
	private void invokeRestAPIAndVerifyFailureResponse(String regPayload, Headers headers, int statusCode) {
		given()
		.headers(headers)
	        .contentType(ContentType.JSON)
	        .body(regPayload)
	        .post(ROOT_URL + "/v1/entities")
	        .then()
        .assertThat().
            statusCode(statusCode);
	}
	
	@Test
	public void testEntityRegistrationWithPostcard()throws Exception{
		
		whenPostcardEnabled().invokeRestAPIAndVerifySuccessResponse(postCardRegPayload);
	}
	
	private EntityResourceTest whenPostcardEnabled() throws Exception {
		String entityIdentificationPayload = getValidRegPayload();
		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(postcard.validateAndDecryptPostcard(Mockito.anyString())).thenReturn(getMockRegPostcardData(entityIdentificationPayload));
		Mockito.when(postcard.encryptPostcard((PostcardData) Mockito.anyObject())).thenReturn(regResponsePostcard);
		
		return this;
	}
	
	private EntityResourceTest whenPostcardComponentThrowsPostcardException() throws Exception {
		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(postcard.validateAndDecryptPostcard(Mockito.anyString())).thenThrow(new PostcardNonRetriableException("un marshalling exception"));
		
		return this;
	}
	
	private PostcardData getMockRegPostcardData(String regPIPayload) throws Exception {
		PostcardData regRequestPostcardData = new PostcardData();
		regRequestPostcardData.setEntityId(CLOUD_ID);
		regRequestPostcardData.setApplicationType(ApplicationType.AVATAR_REGISTRATION);
		Message message = new PostcardData().new Message();
		message.setCompression(PostcardCompression.gzip);
		message.setEncryption(PostcardEncryption.aes_128);
		message.setContentType(com.hp.wpp.postcard.common.PostcardEnums.ContentType.APPLICATION_JSON);
		message.setContent(regPIPayload.getBytes());
		regRequestPostcardData.getMessages().add(message);
		return regRequestPostcardData;
	}
	
	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithHeaderFalseAndAppconfigTrue() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","false");
		Header header2 = new Header("User-Agent", "A B C D");
		Header header3 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2,header3);

		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(appConfig.isBlacklistEnabled()).thenReturn(true);
		invokeRestAPIAndVerifySuccessResponse(getValidRegPayload(), headers);
	}

	@Test
	public void testSuccessRegistrationProcessWithBlacklistCheck() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","false");
		Header header2 = new Header("User-Agent", "A B C D");
		Header header3 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2,header3);

		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(deviceBlacklist.isBlackListEnabled(Mockito.anyBoolean())).thenReturn(true);
		Mockito.doThrow(DeviceBlacklistException.class).when(deviceBlacklist).isBlacklist((String) Mockito.anyString(),(String) Mockito.anyString());
		invokeRestAPIAndVerifyFailureResponse(getValidRegPayload(), headers,403);
	}

	@Test
	public void testSuccessRegistrationProcessWithEmptyHeaderBlacklistCheck() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","false");
		Header header2 = new Header("User-Agent", " ");
		Header header3 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2,header3);

		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(true);
		Mockito.when(deviceBlacklist.isBlackListEnabled(Mockito.anyBoolean())).thenReturn(true);
		Mockito.doThrow(DeviceBlacklistException.class).when(deviceBlacklist).isBlacklist((String) Mockito.anyString(),(String) Mockito.anyString());
		invokeRestAPIAndVerifySuccessResponse(getValidRegPayload(), headers);
	}


	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithHeaderFalseAndAppconfigFalse() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","false");
		Header header2 = new Header("User-Agent", "A B C D");
		Header header3 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2,header3);

		Mockito.when(appConfig.isBlacklistEnabled()).thenReturn(true);
		invokeRestAPIAndVerifySuccessResponse(getValidRegPayload(), headers);
	}
	
	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithHeaderTrueAndAppconfigTrue() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","true");
		Header header2 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2);

		whenPostcardEnabled().
		invokeRestAPIAndVerifySuccessResponse(postCardRegPayload, headers);
	}
	
	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithHeaderTrueAndAppconfigFalse() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		Header header1 = new Header("With-Postcard","true");
		Header header2 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2);

		whenPostcardEnabled();
		Mockito.when(appConfig.isPostcardEnabled()).thenReturn(false);
		
		invokeRestAPIAndVerifySuccessResponse(postCardRegPayload, headers);
	}
	
	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithoutHeaderAndAppconfigFalse() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		
		invokeRestAPIAndVerifySuccessResponse(getValidRegPayload());
	}
	
	@Test
	public void testSuccessRegistrationProcessInDevEnvironmentWithoutHeaderAndAppconfigTrue() throws Exception{
		Mockito.when(appConfig.isEnvironmentProdOrStage()).thenReturn(false);
		
		whenPostcardEnabled().
		invokeRestAPIAndVerifySuccessResponse(getValidRegPayload());
	}
	
	@Test
	public void testSuccessRegistrationProcessInProdEnvironmentWithHeaderFalseAndAppconfigTrue() throws Exception{
		Header header1 = new Header("With-Postcard","false");
		Header header2 = new Header("X-Forwarded-For", ipAddress);
		Headers headers = new Headers(header1, header2);

		whenPostcardComponentThrowsPostcardException().
		invokeRestAPIAndVerifyFailureResponse(getValidRegPayload(), headers, Status.BAD_REQUEST.getStatusCode());
		
		Mockito.verify(postcard).validateAndDecryptPostcard(Mockito.anyString());
	}
}
