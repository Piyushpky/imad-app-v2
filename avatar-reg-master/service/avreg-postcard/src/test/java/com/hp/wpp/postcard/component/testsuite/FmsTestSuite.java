/**
 * 
 */
package com.hp.wpp.postcard.component.testsuite;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.key.generator.IntegrityValidatorTest;

/**
 * Test suite to test postcard received by firmware
 * 
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class FmsTestSuite extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private Postcard postcard;
	@Mock
	private PostcardConfig postcardConfig;
	
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
	
	@DataProvider(name = "dataProvidersForFmsTestSuite")
	public Object[][] dataProvidersForFmsTestSuite() throws Exception {
		String fms_test_case_input_1 = new String(Files.readAllBytes(Paths.get(IntegrityValidatorTest.class.getClassLoader().getResource("client_test_suites/fms_test_case_input_1.json").toURI())));
		
		return new Object[][] { { fms_test_case_input_1 } };
	}
	
	@Test(dataProvider = "dataProvidersForFmsTestSuite", groups = "postcard")
	public void fmsComponentTestCase1(String postcardJson) throws PostcardNonRetriableException {
		PostcardData postcardData = postcard.validateAndDecryptPostcard(postcardJson);
		Assert.assertNotNull(postcardData);
	}
}
