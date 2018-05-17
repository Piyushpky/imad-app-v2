/**
 * 
 */
package com.hp.wpp.postcard.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.PostcardData.Message;
import com.hp.wpp.postcard.cipher.CertificateManager;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.client.PostcardClient;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.common.PostcardEnums.EnvironmentType;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.entities.PostcardRenegotiationInfoEntity;
import com.hp.wpp.postcard.exception.InitiateKeyNegotiationException;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardCompression;
import com.hp.wpp.postcard.json.schema.PostcardEncryption;
import com.hp.wpp.postcard.key.generator.KeyNegotiator;
import com.hp.wpp.postcard.key.generator.SecretGenerator;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardImplTest extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private Postcard postcard;
	@InjectMocks
	@Autowired
	private PostcardClient postcardClient;
	@Autowired
	private PostcardDao postcardDao;
	@Autowired
	private PostcardCipher postcardCipher;
	@Mock
	private PostcardConfig postcardConfig;
	@InjectMocks
	@Autowired
	private KeyNegotiator keyNegotiator;
	@InjectMocks
	@Autowired
	private SecretGenerator secretGenerator;
	@InjectMocks
	@Autowired
	private CertificateManager certificateManager;
	
	// base64 encoded ask
	private String ask = "PHhtbD48bmFtZT50ZXN0PC9uYW1lPjwveG1sPg==";
	String deviceId = "seoupthe3pk";
	String keyId = "key_id-1";
	String applicationId = "1";
	private String javaTrustStore = "certs/server/java_keystore.jks";
	byte[] KEY_SECRET_ENCRYPTION_PASSWORD = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg=="); 
	byte[] KEY_SECRET_ENCRYPTION_IV = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
	
	@BeforeClass
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String fileLocation = loader.getResource(javaTrustStore).getPath();
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(fileLocation);
		Mockito.when(postcardConfig.getJavaCertsKeyStorePassword()).thenReturn("changeit");
		Mockito.when(postcardConfig.getSharedSecretEncryptionKey()).thenReturn(KEY_SECRET_ENCRYPTION_PASSWORD);
		Mockito.when(postcardConfig.getSharedSecretEncryptionIV()).thenReturn(KEY_SECRET_ENCRYPTION_IV);
		Mockito.when(postcardConfig.getSupportedDomain()).thenReturn("certificate_model");
		
		String trustStorePath = Thread.currentThread().getContextClassLoader().getResource("certs/server/java_keystore.jks").getPath();
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(trustStorePath);
	}

	private PostcardEntity populatePostcardEntity(String entityId) throws PostcardNonRetriableException {
		// // populating ask in database.. to check runtime cryptography.. assuming key negotiation already done.
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(entityId);
		postcardEntity.setKeyId(keyId);
		postcardEntity.setSecret(postcardCipher.encrypt(Base64.decodeBase64(ask), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		return postcardEntity;
	}
	
	private PostcardEntity populatePostcardEntity(String entityId, String keyId) throws PostcardNonRetriableException {
		// // populating ask in database.. to check runtime cryptography.. assuming key negotiation already done.
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(entityId);
		postcardEntity.setKeyId(keyId);
		postcardEntity.setSecret(postcardCipher.encrypt(Base64.decodeBase64(ask), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		return postcardEntity;
	}
	
	private void populatePostcardEntityAndInfo(PostcardEntity postcardEntity) {
		
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = new PostcardAdditionalInfoEntity();
		postcardAdditionalInfoEntity.setApplicationId("1");
		postcardAdditionalInfoEntity.setEntityInstruction("Instruction");
		postcardAdditionalInfoEntity.setEntityMessageId("entityMessageId");
		postcardAdditionalInfoEntity.setEntitySeqNum(0);
		postcardAdditionalInfoEntity.setEntitySignatureHash("entitySignatureHash");
		postcardAdditionalInfoEntity.setPostcardEntity(postcardEntity);
		postcardAdditionalInfoEntity.setServiceMessageId("serviceMessageId");
		postcardAdditionalInfoEntity.setServiceSeqNum(0);
		postcardAdditionalInfoEntity.setServiceSignatureHash("serviceSignatureHash");
		postcardDao.updatePostcardAdditionalInfo(postcardAdditionalInfoEntity);
	}
	
	static {
		// any problems in adding BouncyCasthe as Security provider
		// TODO find a way to change the Provider dynamically
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@DataProvider(name = "postcardDecryption")
	public Object[][] dataProviderForValidateAndDecryptPostcardWithoutKeyNegotiation() throws Exception {
		String inputPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_decryption_test.json").toURI())));
		
		PostcardData expectedPostcardData = new PostcardData();
		Message message = expectedPostcardData.new Message();
		message.setContent(Base64.decodeBase64("PHhtbD50ZXN0PC94bWw++"));
		message.setContentType(ContentType.APPLICATION_XML);
		expectedPostcardData.getMessages().add(message);
		
		populatePostcardEntity("seoupthe3pk765");
		
		return new Object[][] { { inputPostcard, expectedPostcardData } };
	}
	
	@DataProvider(name = "postcardDecryptionWithKeyNegotiation")
	public Object[][] dataProviderForValidateAndDecryptPostcardWithKeyNegotiation() throws Exception {
		String inputPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_decryption_keynegotiation.json").toURI())));
		
		PostcardData expectedPostcardData = new PostcardData();
		Message message = expectedPostcardData.new Message();
		message.setContent(Base64.decodeBase64("PHhtbD50ZXN0PC94bWw+"));
		message.setContentType(ContentType.APPLICATION_JSON);
		expectedPostcardData.getMessages().add(message);
		
		/*PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId("seoupthe3pk17");
		postcardEntity.setKeyId(keyId);
		postcardEntity.setSecret(Base64.decodeBase64(ask));
		populatePostcardEntity("seoupthe3pk17");*/
		
		return new Object[][] { { inputPostcard, expectedPostcardData } };
	}
	
	@DataProvider(name = "postcardEncryption")
	public Object[][] dataProviderForEncryptPostcardWithoutKeyNegotiation() throws Exception {
		String expectedPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_test_runtime.json").toURI())));
		PostcardData postcardData = new PostcardData();
		Message message = postcardData.new Message();
		message.setContent(Base64.decodeBase64("PHhtbD50ZXN0PC94bWw+"));
		message.setContentType(ContentType.APPLICATION_XML);
		message.setCompression(PostcardCompression.gzip);
		message.setEncryption(PostcardEncryption.aes_128);
		postcardData.getMessages().add(message);
		Random random = new Random();
		String entityId = deviceId+random.nextInt(999);
		postcardData.setEntityId(entityId);
		postcardData.setApplicationType(ApplicationType.AVATAR_REGISTRATION);
		
		
		populatePostcardEntityAndInfo(populatePostcardEntity(entityId));
		
		return new Object[][] { { postcardData, expectedPostcard } };
	}
	
	@DataProvider(name = "postcard_credential_refresh")
	public Object[][] dataProviderForPostcardCredentialRefresh() throws IOException, URISyntaxException {
		String inputPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_credential_refresh.json").toURI())));
		String expectedPayLoad = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/credential_refresh_payload.json").toURI())));
		
		return new Object[][] { { inputPostcard, expectedPayLoad } };
	}
	
	@DataProvider(name = "postcardEncryptionWithKeyNegotiation")
	public Object[][] dataProviderForEncryptPostcardWithKeyNegotiation() throws IOException, URISyntaxException {
		String expectedPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_test_runtime.json").toURI())));
		PostcardData postcardData = new PostcardData();
		Message message = postcardData.new Message();
		message.setContent(Base64.decodeBase64("PHhtbD50ZXN0PC94bWw+"));
		message.setContentType(ContentType.APPLICATION_JSON);
		message.setCompression(PostcardCompression.gzip);
		message.setEncryption(PostcardEncryption.aes_128);
		postcardData.getMessages().add(message);
		Random random = new Random();
		String entityId = deviceId+random.nextInt(99);
		postcardData.setEntityId(entityId);
		postcardData.setApplicationType(ApplicationType.AVATAR_REGISTRATION);
		
		return new Object[][] { { postcardData, expectedPostcard } };
	}
	
	@DataProvider(name = "postcardDecryption_WithServiceInstructionPostcardResponse")
	public Object[][] dataProviderForValidateAndDecryptPostcard_WithServiceInstructionPostcardResponse() throws Exception {
		String inputPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_test_runtime_with_unknown_keyid.json").toURI())));
		String expectedServiceInstructionPostcard = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("postcard_test/postcard_service_instruction_payload.json").toURI())));
		
		populatePostcardEntity("seoupthe3pk649", keyId+"1");
		
		return new Object[][] { { inputPostcard, expectedServiceInstructionPostcard } };
	}
	
	@Test(dataProvider = "postcardDecryption", groups = "postcard")
	public void testValidateAndDecryptPostcard(String inputPostcard, PostcardData expectedPostcardData) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		PostcardData actualPostcardData = postcard.validateAndDecryptPostcard(inputPostcard);
		Assert.assertNotNull(actualPostcardData);
		Assert.assertEquals(actualPostcardData.getMessages().size(), expectedPostcardData.getMessages().size());
		Assert.assertEquals(Base64.encodeBase64String(actualPostcardData.getMessages().get(0).getContent()), Base64.encodeBase64String(expectedPostcardData.getMessages().get(0).getContent()));
		Assert.assertEquals(actualPostcardData.getMessages().get(0).getContentType(), expectedPostcardData.getMessages().get(0).getContentType());
	}
	
	@Test(dataProvider = "postcardEncryption", groups = "postcard")
	public void testEncryptPostcard(PostcardData inputPostcard, String expectedPostcard) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		String actualValue = postcard.encryptPostcard(inputPostcard);
		System.out.println("@@@@@@@@@@ " + inputPostcard.getEntityId());
		System.out.println("@@@@@@@@@@ " + actualValue);
		// can't check other than not null.. to get the content we have to decrpt again
		Assert.assertNotNull(actualValue);
	}
	
	@Test(dataProvider = "postcardEncryption", groups = "postcard")
	public void testEncryptPostcard_Client(PostcardData inputPostcard, String expectedPostcard) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		String actualValue = postcardClient.encryptPostcard(inputPostcard, Creator.ENTITY);
		System.out.println("@@@@@@@@@@ " + inputPostcard.getEntityId());
		System.out.println("@@@@@@@@@@ " + actualValue);
		// can't check other than not null.. to get the content we have to decrpt again
		Assert.assertNotNull(actualValue);
	}
	
	@Test(expectedExceptions = PostcardJSONCorruptedException.class ,groups = "postcard")
	public void testEncryptPostcardWithMandatoryParamsMissing() throws PostcardNonRetriableException {
		PostcardData inputPostcard = new PostcardData();
		String actualValue = postcard.encryptPostcard(inputPostcard);
	}
	
	@Test(dataProvider = "postcardEncryptionWithKeyNegotiation", groups = "postcard")
	public void testEncryptPostcardWthKeyNegotiation(PostcardData inputPostcard, String expectedPostcard) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		inputPostcard.setEntityId(inputPostcard.getEntityId());
		String actualValue = postcardClient.generatePostcardForKeyNegotiation(inputPostcard, ClientType.JAM, Creator.ENTITY, EnvironmentType.PIE);
		System.out.println("@@@@@@@@@@ " + inputPostcard.getEntityId());
		System.out.println("@@@@@@@@@@ " + actualValue);
		// can't check other than not null.. to get the content we have to decrpt again
		Assert.assertNotNull(actualValue);
	}
	
	@Test(dataProvider = "postcardEncryptionWithKeyNegotiation", groups = "postcard")
	public void testEncryptPostcardWthKeyNegotiation_1_1_Spec(PostcardData inputPostcard, String expectedPostcard) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		inputPostcard.setVersion("1.1");
		inputPostcard.setEntityId(inputPostcard.getEntityId());
		String actualValue = postcardClient.generatePostcardForKeyNegotiation(inputPostcard, ClientType.JAM, Creator.ENTITY, EnvironmentType.PIE);
		System.out.println("@@@@@@@@@@ " + inputPostcard.getEntityId());
		System.out.println("@@@@@@@@@@ " + actualValue);
		// can't check other than not null.. to get the content we have to decrpt again
		Assert.assertNotNull(actualValue);
	}
	
	@Test(dataProvider = "postcardDecryptionWithKeyNegotiation", groups = "postcard")
	public void testValidateAndDecryptPostcardWithKeyNegotiation(String inputPostcard, PostcardData expectedPostcardData) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		PostcardData actualPostcardData = postcard.validateAndDecryptPostcard(inputPostcard);
		Assert.assertNotNull(actualPostcardData);
		Assert.assertEquals(actualPostcardData.getMessages().size(), expectedPostcardData.getMessages().size());
		Assert.assertEquals(Base64.encodeBase64String(actualPostcardData.getMessages().get(0).getContent()), Base64.encodeBase64String(expectedPostcardData.getMessages().get(0).getContent()));
		Assert.assertEquals(actualPostcardData.getMessages().get(0).getContentType(), expectedPostcardData.getMessages().get(0).getContentType());
	}
	
	@Test
	public void testGenerateEntityKey() throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId("JAM_ID_2");
		postcardEntity.setKeyId("83640d83262d414f8432c1b661049518");
		postcardEntity.setSecret(postcardCipher.encrypt(new BigInteger("B17A73A53965E71A7948445732C32CC0", 16).toByteArray(), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		Assert.assertNotNull(postcard.generateEntityKey("CLOUD_ID", "JAM_ID_2", "svc_connectivity"));
		String expectedValue = Base64.encodeBase64String(generateKey(("CLOUD_ID"+"svc_connectivity").getBytes(), new BigInteger("B17A73A53965E71A7948445732C32CC0", 16).toByteArray()));
		Assert.assertEquals(postcard.generateEntityKey("CLOUD_ID", "JAM_ID_2", "svc_connectivity"), expectedValue);
	}
	
	@Test
	public void testIsValidKey_HappyPath() throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId("JAM_ID_3");
		postcardEntity.setKeyId("83640d83262d414f8432c1b661049518");
		postcardEntity.setSecret(postcardCipher.encrypt(new BigInteger("B17A73A53965E71A7948445732C32CC0", 16).toByteArray(), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		String entityKey = Base64.encodeBase64String(generateKey(("CLOUD_ID"+"svc_connectivity12").getBytes(), new BigInteger("B17A73A53965E71A7948445732C32CC0", 16).toByteArray()));
		Assert.assertEquals(postcard.isValidKey("CLOUD_ID", "JAM_ID_3", "svc_connectivity12", entityKey), true);
	}
	
	@Test
	public void testIsValidKey_InvalidEntityKey() throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId("JAM_ID_4");
		postcardEntity.setKeyId("83640d83262d414f8432c1b661049518");
		postcardEntity.setSecret(postcardCipher.encrypt(Base64.decodeBase64(new BigInteger("B17A73A53965E71A7948445732C32CC0", 16).toByteArray()), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		String entityKey = Base64.encodeBase64String(generateKey(("JAM_ID_4"+"svc_connectivity12").getBytes(), postcardEntity.getSecret()));
		Assert.assertEquals(postcard.isValidKey("CLOUD_ID", "JAM_ID_3", "svc_connectivity12", entityKey), false);
	}
	
	@Test(dataProvider = "postcardDecryption_WithServiceInstructionPostcardResponse", groups = "postcard")
	public void testValidateAndDecryptPostcard_WithServiceInstructionPostcardResponse(String inputPostcard, String expectedValue) throws PostcardNonRetriableException {
		try {
			PostcardData postcardData = postcard.validateAndDecryptPostcard(inputPostcard);
		} catch(InitiateKeyNegotiationException ikne) {
			
			PostcardEntity postcardEntity = postcardDao.getPostcard("seoupthe3pk649");
			postcardEntity.setKeyId("key_id-1");
			postcardEntity.setSecret(postcardCipher.encrypt(Base64.decodeBase64(ask), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
			postcardDao.updatePostcard(postcardEntity);

			String serviceInstructionPostcard = ikne.getKeyNegotiationRequestPostcard();
			PostcardData postcardData = postcardClient.validateAndDecryptInstruction(serviceInstructionPostcard);
			String actualValue = new String(postcardData.getMessages().get(0).getContent());
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(dataProvider = "postcard_credential_refresh", groups = "postcard")
	public void testCredentialRefresh(String inputPostcard, String expectedPayloadInDatabase) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		
		String entityId = "seoupthe3pk48";
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(entityId);
		postcardEntity.setKeyId(keyId+1);
		postcardEntity.setSecret(postcardCipher.encrypt(Base64.decodeBase64(ask), KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		
		postcard.refreshSharedSecret(inputPostcard);
		
		postcardEntity = postcardDao.getPostcard(entityId);
		List<PostcardRenegotiationInfoEntity> postcardRenegotiationInfoEntities = postcardDao.getPostcardRenegotiationInfos(postcardEntity, applicationId);
		Assert.assertNotNull(postcardRenegotiationInfoEntities);
		String actualValue = new String(postcardRenegotiationInfoEntities.get(0).getCredentialRefreshInfo());
		System.out.println("Actual Payload: " + actualValue);
		Assert.assertNotNull(actualValue);
		
	}
	
	private byte[] generateKey(byte[] content, byte[] key) {
		byte[] entityKey = null;
		Mac mac;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			entityKey = mac.doFinal(content);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return entityKey;
	}
	
	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}
}
