/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardRSACipherTest extends AbstractTestNGSpringContextTests {
	
	private static final String RSA_ALGORITHM_NAME = "RSA";
	private static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";
	
	private PostcardCipher postcardCipher;
	private RSACipherKey rsaCipherKey;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Security.addProvider(new BouncyCastleProvider());
		postcardCipher = new PostcardRSACipher();
		rsaCipherKey = new RSACipherKey();
	}
	
	@DataProvider(name = "dataProviderDecrypt")
	public Object[][]  dataProviderDecrypt() throws Exception {
		// salt
		String salt = "zHzuTZkoU6Rtm6+wFXyVMrNwkMU5sqTydNebGYkAjY8kiSArg3UIl3xkzQaPZ1pdZaaY5bM8QbJ2hjsMunbq1pt9Tni3O+FSkHTQWQhZ9TtbAXaLOY81/+sFfJiL3wsHgD8Jp90nvPKQ9XfhS6ilxz7I71PHpfYAo/H3mGpPsIg=";
		// key_data
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		byte[] inputValue = encryptWithRSAKeys(Base64.decodeBase64(salt), loadPublicKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_publickey.pem").toURI())))));
		byte[] expectedValue = Base64.decodeBase64(salt);
		
		return new Object[][] { { inputValue, expectedValue } };
	}
	
	@DataProvider(name = "dataProviderDecryptWithDifferentRSAKeys")
	public Object[][]  dataProviderDecryptWithDifferentRSAKeys() throws Exception {
		// salt
		String salt = "zHzuTZkoU6Rtm6+wFXyVMrNwkMU5sqTydNebGYkAjY8kiSArg3UIl3xkzQaPZ1pdZaaY5bM8QbJ2hjsMunbq1pt9Tni3O+FSkHTQWQhZ9TtbAXaLOY81/+sFfJiL3wsHgD8Jp90nvPKQ9XfhS6ilxz7I71PHpfYAo/H3mGpPsIg=";
		// key_data
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		byte[] inputValue = encryptWithRSAKeys(Base64.decodeBase64(salt), loadPublicKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_publickey.pem").toURI())))));
		byte[] expectedValue = Base64.decodeBase64(salt);
		
		return new Object[][] { { inputValue, expectedValue } };
	}
	
	@DataProvider(name = "dataProviderDecryptWithInvalidInput")
	public Object[][]  dataProviderDecryptWithInvalidInput() throws Exception {
		return new Object[][] { { null, new PostcardNonRetriableException() } };
	}
	
	@Test(dataProvider = "dataProviderDecrypt", groups = "postcard")
	public void testDecypt(byte[] input, byte[] expectedValue) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		rsaCipherKey.setPrivateKey(loadPrivateKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_privatekey.pem").toURI())))));
		
		PostcardRSAKEMCipherData cipherData = new PostcardRSAKEMCipherData();
		cipherData.setKeyData(input);
		byte[] actualValue = postcardCipher.decrypt(cipherData, rsaCipherKey);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "dataProviderDecrypt", expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void testDecyptWithDifferentSecurityProvider(byte[] input, byte[] expectedValue) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		rsaCipherKey.setPrivateKey(loadPrivateKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_privatekey.pem").toURI())))));
		
		PostcardRSAKEMCipherData cipherData = new PostcardRSAKEMCipherData();
		cipherData.setKeyData(input);
		
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		
		byte[] actualValue = postcardCipher.decrypt(cipherData, rsaCipherKey);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "dataProviderDecryptWithDifferentRSAKeys", groups = "postcard")
	public void testDecyptWithDifferentRSAKeys(byte[] input, byte[] expectedValue) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		rsaCipherKey.setPrivateKey(loadPrivateKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_privatekey.pem").toURI())))));
		
		PostcardRSAKEMCipherData cipherData = new PostcardRSAKEMCipherData();
		cipherData.setKeyData(input);
		byte[] actualValue = postcardCipher.decrypt(cipherData, rsaCipherKey);
		Assert.assertNotSame(actualValue, expectedValue);
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void testDecryptWithInvalidInput() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		rsaCipherKey.setPrivateKey(loadPrivateKey(new String(Files.readAllBytes(Paths.get(loader.getResource("certs/server/server_privatekey.pem").toURI())))));
		
		PostcardRSAKEMCipherData cipherData = new PostcardRSAKEMCipherData();
		cipherData.setKeyData(null);
		byte[] actualValue = postcardCipher.decrypt(cipherData, rsaCipherKey);
	}
	
	
	
	
	@DataProvider(name = "encrypt")
	public Object[][]  dataProviderEncrypt() throws Exception {
		// input
		String input = "<s>hello world!</s>";
		byte[] inputBytes = input.getBytes();
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] iv = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
		String expectedValue = "DX2SsgVXXRb/McH1c8A1ia0ilNPrjk36IM+wP2VI1pU=";
		
		return new Object[][] { { inputBytes, key, iv, expectedValue } };
	}
	
	@Test(dataProvider = "encrypt", groups = "postcard")
	public void encrypt(byte[] inputBytes, byte[] key, byte[] iv, String expectedValue) throws PostcardNonRetriableException {
		byte[] actualValue = postcardCipher.encrypt(inputBytes, key, iv);
		Assert.assertEquals(actualValue, Base64.decodeBase64(expectedValue));
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void encryptWithInvalidIVAndKey() throws PostcardNonRetriableException {
		// input
		String input = "<s>hello world!</s>";
		byte[] inputBytes = input.getBytes();
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] actualValue = postcardCipher.encrypt(inputBytes, null, null);
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void encryptWithDiffrentLengthIV() throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		// input
		String input = "<s>hello world!</s>";
		byte[] inputBytes = input.getBytes();
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] iv = Base64.decodeBase64("dPFY57hxJuhs25DHym49h92sz65QykQlqRi/2LRQKUYDqs8UArk7TnuHKfLruzn2");
		byte[] actualValue = postcardCipher.encrypt(inputBytes, key, iv);
	}
	
	@Test(dataProvider = "encrypt", expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void encryptWithDifferentSecurityProvider(byte[] inputBytes, byte[] key, byte[] iv, String expectedValue) throws PostcardNonRetriableException {
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		byte[] actualValue = postcardCipher.encrypt(inputBytes, key, iv);
	}
	
	
	
	
	
	@DataProvider(name = "decrypt")
	public Object[][]  dataProviderDecryption() throws Exception {
		// input
		String expectedValue = "<s>hello world!</s>";
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] iv = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
		byte[] inputBytes = Base64.decodeBase64("DX2SsgVXXRb/McH1c8A1ia0ilNPrjk36IM+wP2VI1pU=");
		
		return new Object[][] { { inputBytes, key, iv, expectedValue } };
	}
	
	@Test(dataProvider = "decrypt", groups = "postcard")
	public void decrypt(byte[] inputBytes, byte[] key, byte[] iv, String expectedValue) throws PostcardNonRetriableException {
		byte[] actualValue = postcardCipher.decrypt(inputBytes, key, iv);
		Assert.assertEquals(actualValue, expectedValue.getBytes());
	}
	
	@Test
	public void testEncryptAndDecrypt() throws PostcardNonRetriableException {
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] iv = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
		byte[] inputBytes = Base64.decodeBase64("DX2SsgVXXRb/McH1c8A1ia0ilNPrjk36IM+wP2VI1pU=");
		
		byte[] actualValue = postcardCipher.encrypt(inputBytes, key, iv);
		byte[] decryptedValue = postcardCipher.decrypt(actualValue, key, iv);
		
		Assert.assertEquals(inputBytes, decryptedValue);
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void decryptWithInvalidIV() throws PostcardNonRetriableException {
		// input
		String expectedValue = "<s>hello world!</s>";
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] inputBytes = Base64.decodeBase64("DX2SsgVXXRb/McH1c8A1ia0ilNPrjk36IM+wP2VI1pU=");
		byte[] actualValue = postcardCipher.decrypt(inputBytes, key, null);
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void decryptWithDiffrentLengthIV() throws PostcardNonRetriableException {
		// input
		String expectedValue = "<s>hello world!</s>";
		byte[] key = Base64.decodeBase64("JlUvJMD7cc2AbDvEprm8tg==");
		byte[] iv = Base64.decodeBase64("aeGmTRJ3TNkEXZhT+iTF6w==");
		byte[] inputBytes = Base64.decodeBase64("dPFY57hxJuhs25DHym49h92sz65QykQlqRi/2LRQKUYDqs8UArk7TnuHKfLruzn2");
		byte[] actualValue = postcardCipher.decrypt(inputBytes, key, iv);
	}
	
	@Test(dataProvider = "decrypt", expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void decryptWithDifferentSecurityProvider(byte[] inputBytes, byte[] key, byte[] iv, String expectedValue) throws PostcardNonRetriableException {
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		byte[] actualValue = postcardCipher.decrypt(inputBytes, key, iv);
	}
	
	
	private static PublicKey readPublicKey(File keyFile) throws Exception {
		// read key bytes
		FileInputStream in = new FileInputStream(keyFile);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String publicKey = new String(keyBytes, "UTF-8");
		publicKey = publicKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)","");

		keyBytes = Base64.decodeBase64(publicKey);

		// generate public key
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}
	
	private static byte[] encryptWithRSAKeys(byte[] dataToEncrypt, Key key) throws Exception { 
		byte[] cipherData = null;
		Security.addProvider(new BouncyCastleProvider());
		Cipher aesCipher = Cipher.getInstance(RSA_ALGORITHM_NAME, BOUNCY_CASTLE_PROVIDER_NAME);
		PublicKey publicKey = (PublicKey) key;
		aesCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		cipherData = aesCipher.doFinal(dataToEncrypt);
		return cipherData;
	}
	
	private PrivateKey loadPrivateKey(File file) throws Exception {
		// read key bytes
		try {
			byte[] keyBytes = readTheCipherBytesByKeyType(file, RSA_KEY_TYPE.PRIVATE);

			// generate private key
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
			return keyFactory.generatePrivate(spec);
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw e;
		}
	}
	
	public PrivateKey loadPrivateKey(String key) throws Exception {
		// read key bytes
		try {
			byte[] keyBytes = returnKeyBytes(key, RSA_KEY_TYPE.PRIVATE);
			// generate private key
			PKCS8EncodedKeySpec  keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = getKeyFactoryInstance();
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw e;
		}
	}
	
	private KeyFactory getKeyFactoryInstance() throws NoSuchAlgorithmException {
		return KeyFactory.getInstance(PostcardConstants.RSA_ALGORITHM_NAME);
	}
	
	private byte[] returnKeyBytes(String key, RSA_KEY_TYPE rsaKeyType) {
		if(rsaKeyType == RSA_KEY_TYPE.PRIVATE)
			key = key.replaceAll("(-+BEGIN RSA " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " RSA KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.PUBLIC)
			key = key.replaceAll("(-+BEGIN " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.CERTIFICATE)
			key = key.replaceAll("(-+BEGIN CERTIFICATE-+\\r?\\n?|-+END CERTIFICATE-+\\r?\\n?)","");
		return Base64.decodeBase64(key);
	}
	
	public PublicKey loadPublicKey(String key) throws Exception {
		// read key bytes
		try {
			byte[] keyBytes = returnKeyBytes(key, RSA_KEY_TYPE.PUBLIC);
			// generate private key
			X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = getKeyFactoryInstance();
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw e;
		}
	}

	/**
	 * PUBLIC and PRIVATE keys have below formatted strings at begin and end. This method will remove those strings and returns the cipher key data.
	 * 
	 *  -----BEGIN PRIVATE KEY-----          ******************  -----END PRIVATE KEY-----   >>>  for PrivateKey
	 *  -----BEGIN PUBLIC KEY-----          ******************  -----END PUBLIC KEY-----   >>> for PublicKey
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private byte[] readTheCipherBytesByKeyType(File file, RSA_KEY_TYPE rsaKeyType) throws FileNotFoundException, IOException, UnsupportedEncodingException {
		FileInputStream in = new FileInputStream(file);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String privateKey = new String(keyBytes, "UTF-8");
		if(rsaKeyType == RSA_KEY_TYPE.PRIVATE) {
			privateKey = privateKey.replaceAll("(-+BEGIN RSA " + rsaKeyType + " KEY-+\\r?\\n|-+END RSA +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		} else {
			privateKey = privateKey.replaceAll("(-+BEGIN " + rsaKeyType + " KEY-+\\r?\\n|-+END +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		}

		keyBytes = Base64.decodeBase64(privateKey);
		return keyBytes;
	}
	
	enum RSA_KEY_TYPE {
		PRIVATE, PUBLIC, CERTIFICATE;
	}
	

}
