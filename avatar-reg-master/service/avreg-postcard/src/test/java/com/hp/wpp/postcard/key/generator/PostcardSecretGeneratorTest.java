/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardEnums.KeyAgreementScheme;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.InitiateKeyNegotiationException;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardSecretGeneratorTest extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private SecretGenerator secretGenerator;
	@InjectMocks
	@Autowired
	private KeyNegotiator keyNegotiator;
	@Autowired
	private PostcardDao postcardDao;
	@InjectMocks
	@Autowired
	private PostcardCipher postcardCipher;
	@Mock
	private PostcardConfig postcardConfig;
	private int applicationId = 1;
	byte[] KEY_SECRET_ENCRYPTION_PASSWORD = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg=="); 
	byte[] KEY_SECRET_ENCRYPTION_IV = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(postcardConfig.getSupportedDomain()).thenReturn("certificate_entity");
		String trustStorePath = Thread.currentThread().getContextClassLoader().getResource("certs/server/java_keystore.jks").getPath();
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(trustStorePath);
		
		Mockito.when(postcardConfig.getJavaCertsKeyStorePassword()).thenReturn("changeit");
		Mockito.when(postcardConfig.getSharedSecretEncryptionKey()).thenReturn(KEY_SECRET_ENCRYPTION_PASSWORD);
		Mockito.when(postcardConfig.getSharedSecretEncryptionIV()).thenReturn(KEY_SECRET_ENCRYPTION_IV);
	}
	
	@DataProvider(name = "askWithoutKeyNegotiation")
	public Object[][]  dataProviderRetrieveASK() throws Exception {
		
		String postcardWithoutKeys = new String(Files.readAllBytes(Paths.get(PostcardSecretGeneratorTest.class.getClassLoader().getResource("ask_test/postcard_ask_test.json").toURI())));
		byte[] ask = Base64.decodeBase64("IlhNOFliyq3nDOIRjxhYmA==");
		PostcardContent postcardContent = unmarshall(postcardWithoutKeys);
		
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
		postcardEntity.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardEntity.setSecret(postcardCipher.encrypt(ask, KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		
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
		
		return new Object[][] { { postcardContent, postcardWithoutKeys, ask } };
	}
	
	@DataProvider(name = "dataProviderRetrieveASKWithDiffrentSharedKeyWithoutAsk")
	public Object[][]  dataProviderRetrieveASKWithDiffrentSharedKeyWithoutAsk() throws Exception {
		
		String postcardWithoutKeys = new String(Files.readAllBytes(Paths.get(PostcardSecretGeneratorTest.class.getClassLoader().getResource("ask_test/postcard_ask_test_different_sharedkey.json").toURI())));
		byte[] ask = Base64.decodeBase64("bxExtsF/Web85eD3i6l5PA==");
		PostcardContent postcardContent = unmarshall(postcardWithoutKeys);
		
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
		postcardEntity.setKeyId("KeyId");
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardEntity.setSecret(postcardCipher.encrypt(ask, KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		
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
		
		return new Object[][] { { postcardContent, postcardWithoutKeys, ask } };
	}
	
	@DataProvider(name = "dataProviderRetrieveASKWithDiffrentSharedKeyGenerateAsk")
	public Object[][]  dataProviderRetrieveASKWithDiffrentSharedKeyGenerateAsk() throws Exception {
		
		String postcardWithoutKeys = new String(Files.readAllBytes(Paths.get(PostcardSecretGeneratorTest.class.getClassLoader().getResource("ask_test/postcard_ask_test_different_sharedkey_generate.json").toURI())));
		byte[] ask = new BigInteger("6f1131b6c17f59e6fce5e0f78ba9793c", 16).toByteArray();
		PostcardContent postcardContent = unmarshall(postcardWithoutKeys);
		
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
		postcardEntity.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId()+"1");
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardEntity.setSecret(postcardCipher.encrypt(ask, KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		
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
		
		return new Object[][] { { postcardContent, postcardWithoutKeys, ask } };
	}
	
	@DataProvider(name = "askWithKeyNegotiation")
	public Object[][]  dataProviderRetrieveASKWithKeyNegotiation() throws Exception {
		
		String postcardWithKeys = new String(Files.readAllBytes(Paths.get(PostcardSecretGeneratorTest.class.getClassLoader().getResource("ask_test/postcard_genarte_ask_keynegotiation.json").toURI())));
		byte[] askWithKeys = new BigInteger("6b8b79c2e47a4a2a3bc6a57ca0bad8a3", 16).toByteArray();
		PostcardContent postcardContent = unmarshall(postcardWithKeys);
		
		return new Object[][] { { postcardContent, postcardWithKeys, askWithKeys } };
	}

	@DataProvider(name = "KeyNegotiationWithDuplicateKeyId")
	public Object[][] KeyNegotiationWithDuplicateKeyId() throws Exception {

		String postcardJson = new String(Files.readAllBytes(Paths.get(PostcardSecretGeneratorTest.class.getClassLoader().getResource("postcard_keynegotiation/postcard_keynegotiation_duplicate_keyid.json").toURI())));
		PostcardContent postcardContent = unmarshall(postcardJson);
		return new Object[][] {
			{ postcardContent, postcardJson}
		};

	}
	
	@Test(dataProvider = "askWithoutKeyNegotiation", groups = "postcard", priority = 1)
	public void testRetrieveASKWithoutKeyNegotiation(PostcardContent postcardContent, String postcard, byte[] expectedASK) throws PostcardNonRetriableException {
		PostcardEntity actualASK = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
		Assert.assertEquals(Base64.encodeBase64String(actualASK.getSecret()), Base64.encodeBase64String(expectedASK));
	}
	
	@Test(dataProvider = "askWithKeyNegotiation", groups = "postcard", priority = 2)
	public void testRetrieveASKWithKeyNegotiation(PostcardContent postcardContent, String postcard, byte[] expectedASK) throws PostcardNonRetriableException {
		PostcardEntity actualASK = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
		Assert.assertEquals(Base64.encodeBase64String(actualASK.getSecret()), Base64.encodeBase64String(expectedASK));
	}
	
	@Test(dataProvider = "dataProviderRetrieveASKWithDiffrentSharedKeyWithoutAsk", expectedExceptions = InitiateKeyNegotiationException.class, groups = "postcard")
	public void testRetrieveASKWithoutKeyNegotiationWithDiffrntSharedKey(PostcardContent postcardContent, String postcard, byte[] expectedASK) throws PostcardNonRetriableException {
		postcardContent.getPostcardSignedInfo().setKeyId("keyId1");
		PostcardEntity actualASK = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
		Assert.assertEquals(Base64.encodeBase64String(actualASK.getSecret()), Base64.encodeBase64String(expectedASK));
	}
	
	@Test(dataProvider = "dataProviderRetrieveASKWithDiffrentSharedKeyGenerateAsk", groups = "postcard")
	public void testRetrieveASKWithoutKeyNegotiationWithDiffrntSharedKeyGenerateAsk(PostcardContent postcardContent, String postcard, byte[] expectedASK) throws PostcardNonRetriableException {
		PostcardEntity actualASK = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
		Assert.assertEquals(Base64.encodeBase64String(actualASK.getSecret()), Base64.encodeBase64String(expectedASK));
	}
	
	@Test(groups = "postcard")
	public void testGetAsk() throws PostcardNonRetriableException {
		byte[] ask = Base64.decodeBase64("IlhNOFliyq3nDOIRjxhYmA==");
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId("deviceId-2");
		postcardEntity.setKeyId("keyId");
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardEntity.setSecret(postcardCipher.encrypt(ask, KEY_SECRET_ENCRYPTION_PASSWORD, KEY_SECRET_ENCRYPTION_IV));
		postcardDao.createPostcard(postcardEntity);
		
		PostcardEntity actualASK = secretGenerator.getSecret("deviceId-2");
		Assert.assertEquals(Base64.encodeBase64String(actualASK.getSecret()), Base64.encodeBase64String(ask));
	}
	
	@Test(expectedExceptions = PostcardEntityNotFoundException.class ,groups = "postcard")
	public void testgetAskNonExistingDevice() throws PostcardNonRetriableException {
		PostcardEntity actualASK = secretGenerator.getSecret("deviceId-1");
	}

	@Test(expectedExceptions = PostcardNonRetriableException.class, groups = "postcard", dataProvider = "KeyNegotiationWithDuplicateKeyId")
	public void test_keyNegotiationWithExistingKeyId(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		PostcardEntity postcardEntity = secretGenerator.retrieveAndStoreSecret(postcardContent, postcardJson);
		postcardEntity = secretGenerator.retrieveAndStoreSecret(postcardContent, postcardJson);
	}

	private PostcardContent unmarshall(String jsonText) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonText, PostcardContent.class);
	}

}
