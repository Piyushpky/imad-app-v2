/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.postcard.cipher.CertificateManager;
import com.hp.wpp.postcard.cipher.PostcardCertValidator;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.exception.PostcardHashMismatchException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardDomain;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.json.schema.PostcardKey.KeyAgreement;
import com.hp.wpp.postcard.json.schema.PostcardKey.KeyAgreement.RsaKem;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class RSAKeyNegotiatorTest extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private KeyNegotiator keyNegotiator;
	@InjectMocks
	@Autowired
	private CertificateManager certificateManager;
	@Mock
	private PostcardConfig postcardConfig;
	@Mock
	private PostcardCertValidator postcardCertValidator;
	
	private String javaTrustStore = "certs/server/java_keystore.jks";
	byte[] KEY_SECRET_ENCRYPTION_PASSWORD = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg=="); 
	byte[] KEY_SECRET_ENCRYPTION_IV = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@BeforeMethod
	public void setUp() throws PostcardNonRetriableException {
		MockitoAnnotations.initMocks(this);
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String fileLocation = loader.getResource(javaTrustStore).getPath();
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(fileLocation);
		Mockito.when(postcardConfig.getJavaCertsKeyStorePassword()).thenReturn("changeit");
		Mockito.when(postcardConfig.getSharedSecretEncryptionKey()).thenReturn(KEY_SECRET_ENCRYPTION_PASSWORD);
		Mockito.when(postcardConfig.getSharedSecretEncryptionIV()).thenReturn(KEY_SECRET_ENCRYPTION_IV);
		Mockito.when(postcardConfig.getSupportedDomain()).thenReturn("certificate_model");
		Mockito.when(postcardConfig.isPostcardEntityCertValidationRequired()).thenReturn(true);
		
		Mockito.doNothing().when(postcardCertValidator).verify(Mockito.anyString());
	}
	
	@DataProvider(name = "saltGenerator")
	public Object[][]  dataProviderSaltGenerator() throws Exception {
		// salt
		byte[] salt = Base64.decodeBase64("NzzZ+wmbKHHcxjmsMYixDBBvfg21oa1HUll+dVgHutNHMOJl8TyqWex5b3jsLINdtNGOD+Qq5DpCo8oGe/ruD9AaIVfcHtO8kNvvwZSRiM6IeY2ylgNlbKwDq4mvCRrkU+xGurydTcA0oc4JTkAv34b5Ia2uUk8EBph0f18HM0TmR5/6hReDQPWGDKPkVcaqfqzuJVTMgTe4u6Yi1V7Ho7PBtOewh7HrJfJl60SyeIyVW6oCcpYdf59S8UjCzJaHd23OmLozaErNY2PxAbksKcE0WJk3PdGSjOPJsrqn3ssEvSDUpmc9tSa7FSDQoWsAThEYa5i33RDDgpk9tbiVcA==");
		// keyData
		String keyData = "e/O7OY4lFZwH/oHhpKiB5Rfxyjh2qv0eoybX3L47j2IQ/yEGvHJ0xkVId78Aa9NvT6RoSWya2gY0gJ74RiWvRCA2jaWbhjSlXOuP8vSB880c19rOUUVc1iRdy1DBwvUOkKuXmZOeMVgQVcKoFcBYz0RoItLPbO41CaKBWywpagRkWZmUhiiLQ4uIByqR64pjItytLvz7y6Si63OIQ8myeTjBId0PqZXr4eK4n7cm8f0WpM9jhn7UFYUFVB/uNmQhQXZx6HLl5q/YmN3MIRb2+5YcqQNlvzfxORyg5qctybHc/ITjZALQNV/340MH3KQ0V/Eh0jXj6WCEivbMb8e4og==";
		
		return new Object[][] { { getPostcardForKeyNegotiation(keyData), salt } };
	}
	
	@DataProvider(name = "saltGeneratorWithKeyNegotiation")
	public Object[][]  dataProviderSaltGeneratorWithKeyNegotiation() throws Exception {
		String jsonString = new String(Files.readAllBytes(Paths.get(RSAKeyNegotiatorTest.class.getClassLoader().getResource("postcard_keynegotiation/postcard_keynegotiation_Success.json").toURI())));
		PostcardContent postcardContent = unMarshal(jsonString);
		// salt
		byte[] salt = Base64.decodeBase64("vPMrh4SpGPeib21PvzAlt5egG9Ce7IXlW5DBJOkjh3PN6n38ATfodDWINdpuTXveKioheutqZ/OPyZzVyFtx6XiLZm7Ibty4+xsO3eyC9X81GRmv1JkhahA27WCLc2tVhhvgPwTH6jF9oBPzlQLrC2wDYwImMlHxYTY8sP5EwdE0VeRVRoNdHtrWNT/DIwGiNEZ8ZVSsnDbbG5fkL6y6gbMBc+EDfXLtQyr2elqgIH8oycUT8xCnSyrvRLTyC1jGp2PhWBF82u19XiNCPeEluZiQT2c+WVw0zIb+lV3iasepcbzSLh+bLmKO7B/WWp5+BSCUY5JfOSGW7aLpTW18");
		
		return new Object[][] { { postcardContent, jsonString, salt } };
	}
	
	@DataProvider(name = "dataProviderSaltGeneratorWithKeyNegotiationWithInvalidSignature")
	public Object[][]  dataProviderSaltGeneratorWithKeyNegotiationWithInvalidSignature() throws Exception {
		String jsonString = new String(Files.readAllBytes(Paths.get(RSAKeyNegotiatorTest.class.getClassLoader().getResource("postcard_keynegotiation/postcard_keynegotiation_Failure.json").toURI())));
		PostcardContent postcardContent = unMarshal(jsonString);
		// salt
		byte[] salt = Base64.decodeBase64("KkOI0XjEVRmLz9bgdAK9znc4QJhsNnliCjrArRs7rOxWsyLJQgd5G2EWZ6EMx3ARJQZ1YAA/7N3uWclyHLKOOY8C2hcRgU99PzUvObyuYNshmW54SPYD6cRPpN+iOUofHF5Y5yRTxNvFihy8p9N1kowH975Vfm2flX8tK/N1IoyZOGdQf5vMfQgtJZpWpBFYHC4WhT1+/KkhiNlY+cHZhXUgC9xoyfgNEOQTGjWM9oKbboTTmD6thVplNqDC36sOdObT8uoBLIBZ9OiSFCxtsWa+RqsD1h2Z52J+dQE5vl8tFJsOVZQrnFa5mbOmIqjo7gRO36BlUCqi3qZ1i68n/A==");
		
		return new Object[][] { { postcardContent, jsonString, salt } };
	}
	
	@DataProvider(name = "dataProviderSaltGeneratorWithKeyNegotiationWithUnsupportedDomain")
	public Object[][]  dataProviderSaltGeneratorWithKeyNegotiationWithUnsupportedDomain() throws Exception {
		String jsonString = new String(Files.readAllBytes(Paths.get(RSAKeyNegotiatorTest.class.getClassLoader().getResource("postcard_keynegotiation/postcard_keynegotiation_unsupporteddomain.json").toURI())));
		PostcardContent postcardContent = unMarshal(jsonString);
		// salt
		byte[] salt = Base64.decodeBase64("KkOI0XjEVRmLz9bgdAK9znc4QJhsNnliCjrArRs7rOxWsyLJQgd5G2EWZ6EMx3ARJQZ1YAA/7N3uWclyHLKOOY8C2hcRgU99PzUvObyuYNshmW54SPYD6cRPpN+iOUofHF5Y5yRTxNvFihy8p9N1kowH975Vfm2flX8tK/N1IoyZOGdQf5vMfQgtJZpWpBFYHC4WhT1+/KkhiNlY+cHZhXUgC9xoyfgNEOQTGjWM9oKbboTTmD6thVplNqDC36sOdObT8uoBLIBZ9OiSFCxtsWa+RqsD1h2Z52J+dQE5vl8tFJsOVZQrnFa5mbOmIqjo7gRO36BlUCqi3qZ1i68n/A==");
		
		return new Object[][] { { postcardContent, salt } };
	}
	
	@Test(dataProvider = "saltGenerator", expectedExceptions = PostcardNonRetriableException.class, groups = "postcard")
	public void validateKeysAndGenerateSaltWithInvalidSecurityProvider(PostcardContent postcardContent, byte[] expectedValue) throws PostcardNonRetriableException {
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		byte[] actualValue = keyNegotiator.validateKeysAndGenerateSalt(postcardContent, null);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "saltGenerator", expectedExceptions = UnsupportedPostcardException.class, groups = "postcard")
	public void validateKeysAndGenerateSaltWithDifferentDomain(PostcardContent postcardContent, byte[] expectedValue) throws PostcardNonRetriableException {
		postcardContent.getPostcardSignedInfo().getKeys().get(0).setDomain(PostcardDomain.certificate_device);
		byte[] actualValue = keyNegotiator.validateKeysAndGenerateSalt(postcardContent, null);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "saltGeneratorWithKeyNegotiation", groups = "postcard")
	public void validateKeysAndGenerateSaltWithKeyNegotiation(PostcardContent postcardContent, String postcardJson, byte[] expectedValue) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		byte[] actualValue = keyNegotiator.validateKeysAndGenerateSalt(postcardContent, postcardJson);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "dataProviderSaltGeneratorWithKeyNegotiationWithInvalidSignature", expectedExceptions = PostcardHashMismatchException.class , groups = "postcard")
	public void validateKeysAndGenerateSaltWithKeyNegotiationWithInvalidHash(PostcardContent postcardContent, String postcardJson, byte[] expectedValue) throws PostcardNonRetriableException {
		byte[] actualValue = keyNegotiator.validateKeysAndGenerateSalt(postcardContent, postcardJson);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	private PostcardContent unMarshal(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (PostcardContent) mapper.readValue(jsonString, PostcardContent.class);
	}
	
	private PostcardContent getPostcardForKeyNegotiation(String keyData) {
		PostcardContent postcardContent = new PostcardContent();
		PostcardContent.PostcardSignedInfo signedInfo = new PostcardContent.PostcardSignedInfo();
		signedInfo.getKeys();
		
		PostcardKey keyType = new PostcardKey();
		KeyAgreement keyAgreement = new KeyAgreement();
		RsaKem rsaKem = new RsaKem();
		rsaKem.setKeyData(keyData);
		rsaKem.setServerPublicKeyId("8aee9ae803ad31ae");
		keyAgreement.setRsaKem(rsaKem);
		keyType.setKeyAgreement(keyAgreement);
		keyType.setDomain(PostcardDomain.certificate_device);
		signedInfo.getKeys().add(keyType);
		postcardContent.setPostcardSignedInfo(signedInfo);
		return postcardContent;
	}
}
