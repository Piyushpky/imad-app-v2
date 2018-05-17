/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
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

import com.google.common.io.ByteStreams;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class CertificateManagerTest extends AbstractTestNGSpringContextTests {
	
	@InjectMocks
	@Autowired
	private CertificateManager testCertificateManager;
	@Mock
	private PostcardConfig postcardConfig;
	private String serverPublicKeyClassPath = "certs/server/server_publickey.pem";
	private String serverPrivateKeyClassPath = "certs/server/server_privatekey.pem";
	private String serverCertClassPath = "certs/server/server_cert.pem";
	private String javaTrustStore = "certs/server/java_keystore.jks";

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(postcardConfig.getJavaCertsKeyStorePassword()).thenReturn("changeit");
	}

	@DataProvider(name = "dataProviderGeneratePublicKey")
	public Object[][] dataProvider_GeneratePublicKey() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String serverPublicKeyPemData = new String(ByteStreams.toByteArray(loader.getResourceAsStream(serverPublicKeyClassPath)));
		PublicKey publicKey = generatePublicKeyWithBouncyCastle(serverPublicKeyClassPath);
		return new Object[][] { { serverPublicKeyPemData, publicKey } };
	}
	
	@DataProvider(name = "dataProviderGeneratePrivateKey")
	public Object[][] dataProvider_GeneratePrivateKey() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String serverPrivateKeyPemData = new String(ByteStreams.toByteArray(loader.getResourceAsStream(serverPrivateKeyClassPath)));
		PrivateKey privateKey = generatePrivateKeyWithBouncyCastle(serverPrivateKeyClassPath);
		return new Object[][] { { serverPrivateKeyPemData, privateKey } };
	}
	
	@DataProvider(name = "dataProviderGeneratePublicKeyFromCertificate")
	public Object[][] dataProvider_GeneratePublicKeyFromCertificate() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String certPemData = new String(ByteStreams.toByteArray(loader.getResourceAsStream(serverCertClassPath)));
		PublicKey publicKey = generatePublicKeyWithBouncyCastle(serverPublicKeyClassPath);
		return new Object[][] { { certPemData, publicKey } };
	}
	
	@DataProvider(name = "dataProviderGenerateKeypair")
	public Object[][] dataProvider_GenerateKeypair() throws Exception {
		X509Certificate certificate = generateCertificateWithBouncyCastle(serverCertClassPath);
		PrivateKey privateKey = generatePrivateKeyWithBouncyCastle(serverPrivateKeyClassPath);
		PublicKey publicKey = generatePublicKeyWithBouncyCastle(serverPublicKeyClassPath);
		
		return new Object[][] { { certificate.getSerialNumber().toString(16), publicKey, privateKey } };
	}

	@Test(dataProvider = "dataProviderGeneratePublicKey", groups = "postcard")
	public void generatePublicKey(String pemData, PublicKey expectedPublicKey) throws PostcardNonRetriableException {
		PublicKey publicKey = testCertificateManager.generatePublicKey(pemData);
		Assert.assertEquals(((RSAPublicKey) publicKey).getPublicExponent(), ((RSAPublicKey) expectedPublicKey).getPublicExponent());
		Assert.assertEquals(((RSAPublicKey) publicKey).getModulus(), ((RSAPublicKey) expectedPublicKey).getModulus());
	}
	
	@Test(dataProvider = "dataProviderGeneratePrivateKey", groups = "postcard")
	public void generatePrivateKey(String pemData, PrivateKey expectedPrivateKey) throws PostcardNonRetriableException {
		PrivateKey privateKey = testCertificateManager.generatePrivateKey(pemData);
		Assert.assertEquals(((RSAPrivateKey) privateKey).getPrivateExponent(), ((RSAPrivateKey) expectedPrivateKey).getPrivateExponent());
		Assert.assertEquals(((RSAPrivateKey) privateKey).getModulus(), ((RSAPrivateKey) expectedPrivateKey).getModulus());
	}
	
	@Test(dataProvider = "dataProviderGeneratePublicKeyFromCertificate", groups = "postcard")
	public void generatePublicKeyWithCertificate(String pemData, PublicKey expectedPublicKey) throws PostcardNonRetriableException {
		PublicKey publicKey = testCertificateManager.generatePublicKeyFromCertificate(pemData);
		Assert.assertEquals(((RSAPublicKey) publicKey).getPublicExponent(), ((RSAPublicKey) expectedPublicKey).getPublicExponent());
		Assert.assertEquals(((RSAPublicKey) publicKey).getModulus(), ((RSAPublicKey) expectedPublicKey).getModulus());
	}
	
	@Test(dataProvider = "dataProviderGenerateKeypair", groups = "postcard")
	public void generateKeypair(String serialNumber, PublicKey expectedPublicKey, PrivateKey expectedPrivateKey) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String fileLocation = loader.getResource(javaTrustStore).getPath();
		KeyPair keyPair = testCertificateManager.getRsaKeyPair(fileLocation, serialNumber);
		
		Assert.assertEquals(((RSAPublicKey) keyPair.getPublic()).getPublicExponent(), ((RSAPublicKey) expectedPublicKey).getPublicExponent());
		Assert.assertEquals(((RSAPublicKey) keyPair.getPublic()).getModulus(), ((RSAPublicKey) expectedPublicKey).getModulus());
		
		Assert.assertEquals(((RSAPrivateKey) keyPair.getPrivate()).getPrivateExponent(), ((RSAPrivateKey) expectedPrivateKey).getPrivateExponent());
		Assert.assertEquals(((RSAPrivateKey) keyPair.getPrivate()).getModulus(), ((RSAPrivateKey) expectedPrivateKey).getModulus());
		
	}

	private PrivateKey generatePrivateKeyWithBouncyCastle(String filename) throws Exception {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PemObject pemObject = getPemObject(filename);
		byte[] content = pemObject.getContent();
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
		return factory.generatePrivate(privKeySpec);
	}
	
	private X509Certificate generateCertificateWithBouncyCastle(String filename) throws Exception {
		CertificateFactory ctf = CertificateFactory.getInstance("X.509");
		PemObject pemObject = getPemObject(filename);
		byte[] content = pemObject.getContent();
		ByteArrayInputStream bis = new ByteArrayInputStream(content);
        X509Certificate cer = (X509Certificate) ctf.generateCertificate(bis);
        bis.close();
        return cer;
	}

	private PublicKey generatePublicKeyWithBouncyCastle(String filename) throws Exception {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PemObject pemObject = getPemObject(filename);
		byte[] content = pemObject.getContent();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
		return factory.generatePublic(pubKeySpec);
	}
	
	private PemObject getPemObject(String fileName) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(loader.getResource(fileName).getFile())));
		try {
			return pemReader.readPemObject();
		} finally {
			pemReader.close();
		}
	}
}
