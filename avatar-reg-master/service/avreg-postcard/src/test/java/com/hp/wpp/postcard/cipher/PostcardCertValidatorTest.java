/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.impl.PostcardImplTest;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardCertValidatorTest extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private CertificateManager certificateManager;
	@Mock
	private PostcardConfig postcardConfig;
	@InjectMocks
	@Autowired
	private PostcardCertValidator postcardCertValidator;
	
	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(postcardConfig.getJavaCertsKeyStorePassword()).thenReturn("changeit");
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/ca_truststore").toURI()).toAbsolutePath().toString());
	}
	
	@Test(groups = "postcard")
	public void testVerifyWithRootCA() throws Exception {
		// certificate passing here is signed by root CA
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/server_root_ca_trusted_cert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
	
	@Test(groups = "postcard")
	public void testVerifyWithSelfSignedCA() throws Exception {
		// certificate passing here is signed by root CA
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/server_selfsigned_cert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
	
	@Test(groups = "postcard", expectedExceptions = { PostcardNonRetriableException.class })
	public void testVerifyWithExpiredCerts() throws Exception {
		// certificate passing here is signed by root CA
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/test_cert_pie.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
	
	@Test(groups = "postcard", expectedExceptions = { PostcardNonRetriableException.class })
	public void testVerifyWithNonExistingCASignedCert() throws Exception {
		// certificate passing here is signed by root CA
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/ca_truststore_test").toURI()).toAbsolutePath().toString());
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/server_root_ca_trusted_cert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
	
	@Test(groups = "postcard")
	public void testVerifyWithPrinterCerts() throws Exception {
		// certificate passing here is signed by root CA
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/printer_certs/ca_truststore").toURI()).toAbsolutePath().toString());
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/printer_certs/postcard_printer_cert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
	
	@Test(groups = "postcard")
	public void testVerifyWithJamCerts() throws Exception {
		// certificate passing here is signed by root CA
		Mockito.when(postcardConfig.getJavaCertsKeyStoreLocation()).thenReturn(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/jam_certs/ca_truststore").toURI()).toAbsolutePath().toString());
		String pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/jam_certs/devCert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
		
		pemCertificate = new String(Files.readAllBytes(Paths.get(PostcardImplTest.class.getClassLoader().getResource("certs/ca_validation/jam_certs/stagingCert.pem").toURI())));
		postcardCertValidator.verify(pemCertificate);
	}
}
