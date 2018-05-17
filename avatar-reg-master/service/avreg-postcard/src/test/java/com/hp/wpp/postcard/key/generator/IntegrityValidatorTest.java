/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.io.ByteStreams;
import com.hp.wpp.postcard.cipher.ByteArrayCipherData;
import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.cipher.RSACipherKey;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.exception.PostcardHashMismatchException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.PostcardSignatureMismatchException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.version.factory.PostcardVersionStrategyFactory;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
@PrepareForTest(PostcardUtility.class)
public class IntegrityValidatorTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private IntegrityValidator integrityValidator;
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private PostcardVersionStrategyFactory postcardVersionFactory;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@DataProvider(name = "dataProviderForGenerateHashAndSignatures")
	public Object[][] dataProviderForGenerateHashAndSignatures() throws Exception {
		String inputJson = new String(Files.readAllBytes(Paths.get(IntegrityValidatorTest.class.getClassLoader().getResource("postcard_integrity_validation/postcard_hash_signature_generation_check.json").toURI())));
		PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(inputJson);
		PostcardSignature postcardSignature = new PostcardSignature();
		// base64 encoded signature
		byte[] key = Base64.decodeBase64("TjSXGv5huFPfEhmbE+Am+8UP6KyT2qrlaYFoyUiaPzA=");
		String signature = "U+gHn4YtglCp+lg6rM3pafUFWiLRaoVubYLiM8Y2rgk=";
		postcardSignature.setSignature(signature);
		
		return new Object[][] { { postcardContent, key, postcardSignature } };
	}
	
	@DataProvider(name = "dataProviderForGenerateHashAndSignaturesForKeyNegotiation")
	public Object[][] dataProviderForGenerateHashAndSignaturesForKeyNegotiation() throws Exception {
		String inputJson = new String(Files.readAllBytes(Paths.get(IntegrityValidatorTest.class.getClassLoader().getResource("postcard_integrity_validation/postcard_hash_signature_generation_check.json").toURI())));
		PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(inputJson);
		return new Object[][] { { postcardContent } };
	}
	
	@DataProvider(name = "dataProviderForValidateHashAndSignature")
	public Object[][] dataProviderForValidateHashAndSignature() throws Exception {
		String inputJson = new String(Files.readAllBytes(Paths.get(IntegrityValidatorTest.class.getClassLoader().getResource("postcard_integrity_validation/postcard_signature_validation_check.json").toURI())));
		PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(inputJson);
		PostcardSignature postcardSignature = new PostcardSignature();
		byte[] hash = generateHash(canonize(postcardParser.serialize(postcardContent.getPostcardSignedInfo())));
		// base64 encoded signature
		byte[] key = Base64.decodeBase64("TjSXGv5huFPfEhmbE+Am+8UP6KyT2qrlaYFoyUiaPzA=");
		String signature = "GC5vEx5NS9+Opp/Ok+4QW1ZUXkN5w11/NE/5ix2jLDs=";
		postcardSignature.setHash(Base64.encodeBase64String(hash));
		postcardSignature.setSignature(signature);
		postcardSignature.setSignatureScheme(PostcardSignatureScheme.hmac_sha256.name());
		
		return new Object[][] { { postcardContent, key, postcardSignature } };
	}
	
	@DataProvider(name = "dataProviderForValidateHashAndSignatureFailures")
	public Object[][] dataProviderForValidateHashAndSignatureFailures() throws Exception {
		String inputJson = new String(Files.readAllBytes(Paths.get(IntegrityValidatorTest.class.getClassLoader().getResource("postcard_integrity_validation/postcard_signature_validation_check.json").toURI())));
		PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(inputJson);
		PostcardSignature postcardWithInvalidSignature = new PostcardSignature();
		// base64 encoded signature
		byte[] key = Base64.decodeBase64("TjSXGv5huFPfEhmbE+Am+8UP6KyT2qrlaYFoyUiaPzA=");
		String signature = "GC5vEx5NS9+Opp/Ok+4QW1ZUXkN5w11/NE5ix2jLDs=";
		postcardWithInvalidSignature.setHash("VOQ99/DkJNk9n8bZtn0geMj8+GmdtMxYgZGVOXkP0P4=");
		postcardWithInvalidSignature.setSignature(signature);
		
		PostcardSignature postcardWithInvalidHash = new PostcardSignature();
		postcardWithInvalidHash.setHash("VOQ99/DkJNk9n8bZtn0gej8+GmdtMxYgZGVOXkP0P4=");
		postcardWithInvalidHash.setSignature("GC5vEx5NS9+Opp/Ok+4QW1ZUXkN5w11/NE5ix2jLDs=");
		
		return new Object[][] { { postcardContent, key, postcardWithInvalidSignature },
				{ postcardContent, key, postcardWithInvalidHash } };
	}
	
	@DataProvider(name = "dataProviderForKeySignatureValidation")
	public Object[][] dataProviderForKeySignatureValidation() throws Exception {
		byte[] data = "data".getBytes();
		byte[] signedData = generateRSSPSSSignedData(data);	
		ByteArrayCipherData cipherData = new ByteArrayCipherData();
		cipherData.setCipherData(data);
		
		RSACipherKey cipherKey = new RSACipherKey();
		cipherKey.setPublicKey(loadPublicKey(new File(IntegrityValidatorTest.class.getClassLoader().getResource("certs/device/device_publickey.pem").getPath())));
		
		return new Object[][] { { cipherData, cipherKey, signedData } };
	}
	
	@Test(dataProvider = "dataProviderForGenerateHashAndSignatures", groups = "postcard")
	public void testGenerateHashAndSignatures(PostcardContent postcardContent, byte[] key, PostcardSignature expectedSignature) throws PostcardNonRetriableException {
		PostcardSignature actualSignature = integrityValidator.generateHashAndSignatures(postcardContent, key);
		Assert.assertEquals(actualSignature.getSignature(), expectedSignature.getSignature());
	}
	
	@Test(dataProvider = "dataProviderForGenerateHashAndSignaturesForKeyNegotiation", groups = "postcard")
	public void testGenerateHashAndSignaturesForKeyNegotiation(PostcardContent postcardContent) throws PostcardNonRetriableException {
		PostcardSignature actualSignature = integrityValidator.generateHashAndSignaturesForKeyNegotiation(postcardContent, ClientType.JAM);
		String input = postcardParser.serialize(postcardContent.getPostcardSignedInfo());
		input = canonize(input);
		String expectedHash = Base64.encodeBase64String(generateHash(input));
		Assert.assertEquals(actualSignature.getHash(), expectedHash);
		
		boolean isExpectedSignatureVerified = validateKeySignatureForKeyNegotiation(Base64.decodeBase64(expectedHash), Base64.decodeBase64(actualSignature.getSignature()));
		Assert.assertEquals(true, isExpectedSignatureVerified);
	}
	
	@Test(dataProvider = "dataProviderForValidateHashAndSignature", groups = "postcard")
	public void testValidateHashAndSignature(PostcardContent postcardContent, byte[] key, PostcardSignature postcardSignatureTobeValidated) throws PostcardNonRetriableException {
		integrityValidator.validateHashAndSignature(postcardContent, key, postcardSignatureTobeValidated);
	}
	
	@Test(dataProvider = "dataProviderForValidateHashAndSignatureFailures", expectedExceptions = { PostcardHashMismatchException.class }, groups = "postcard")
	public void testValidateHashAndSignatureWithFailures(PostcardContent postcardContent, byte[] key, PostcardSignature postcardSignatureTobeValidated) throws PostcardNonRetriableException {
		integrityValidator.validateHashAndSignature(postcardContent, key, postcardSignatureTobeValidated);
	}
	
//	@Test(dataProvider = "dataProviderForValidateHashAndSignatureFailures", expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void testValidateHashAndSignatureWithInvalidCipherAlgo(PostcardContent postcardContent, byte[] key, PostcardSignature postcardSignatureTobeValidated) throws NoSuchAlgorithmException, PostcardNonRetriableException {
		PowerMockito.spy(PostcardUtility.class);
		PowerMockito.when(PostcardUtility.generateSHA256Hash(((byte[])Mockito.any()), Mockito.anyInt())).thenThrow(new PostcardNonRetriableException());
		integrityValidator.validateHashAndSignature(postcardContent, key, postcardSignatureTobeValidated);
	}
	
//	@Test(dataProvider = "dataProviderForGenerateHashAndSignatures", expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void testGenerateHashAndSignaturesWithInvalidAlgo(PostcardContent postcardContent, byte[] key, PostcardSignature expectedSignature) throws PostcardNonRetriableException {
		Mockito.when(postcardVersionFactory.getSignatureSchemeStrategy(Mockito.anyString())).thenThrow(new PostcardNonRetriableException());
		PostcardSignature actualSignature = integrityValidator.generateHashAndSignatures(postcardContent, key);
		Assert.assertEquals(actualSignature.getSignature(), expectedSignature.getSignature());
	}
	
	@Test(dataProvider = "dataProviderForKeySignatureValidation", groups = "postcard")
	public void validateKeySignature(CipherData cipherData, CipherKey cipherKey, byte[] signature) throws PostcardNonRetriableException {
		integrityValidator.validateKeySignature(cipherData, cipherKey, signature, PostcardSignatureScheme.sha256_with_rsa_and_mfg1);
	}
	
	@Test(dataProvider = "dataProviderForKeySignatureValidation", expectedExceptions = PostcardSignatureMismatchException.class, groups = "postcard")
	public void validateKeySignatureWithInvalidSignature(CipherData cipherData, CipherKey cipherKey, byte[] signature) throws PostcardNonRetriableException {
		signature = "abcd1".getBytes();
		integrityValidator.validateKeySignature(cipherData, cipherKey, signature, PostcardSignatureScheme.sha256_with_rsa_and_mfg1);
	}
	
	@Test(dataProvider = "dataProviderForKeySignatureValidation", expectedExceptions = PostcardNonRetriableException.class, groups = "postcard")
	public void validateKeySignatureWithInvalidSecurityProvider(CipherData cipherData, CipherKey cipherKey, byte[] signature) throws PostcardNonRetriableException {
		signature = "abcd1".getBytes();
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		integrityValidator.validateKeySignature(cipherData, cipherKey, signature, PostcardSignatureScheme.hmac_sha256);
	}
	
	@Test(dataProvider = "dataProviderForKeySignatureValidation", expectedExceptions = PostcardNonRetriableException.class, groups = "postcard")
	public void validateKeySignatureWithInvalidRSAKey(CipherData cipherData, CipherKey cipherKey, byte[] signature) throws PostcardNonRetriableException {
		signature = "abcd1".getBytes();
		RSACipherKey cipherKey1 = new RSACipherKey();
		cipherKey1.setPublicKey(loadPublicKey(new File(IntegrityValidatorTest.class.getClassLoader().getResource("certs/device/device_privatekey.pem").getPath())));
		integrityValidator.validateKeySignature(cipherData, cipherKey, signature, PostcardSignatureScheme.hmac_sha256);
	}
	
	private boolean validateKeySignatureForKeyNegotiation(byte[] hash, byte[] signatureTobeValidated) throws PostcardNonRetriableException {
		Security.addProvider(new BouncyCastleProvider());
		Signature ss;
		try {
			PublicKey publicKey = loadDevicePublicKey();
			ss = Signature.getInstance("SHA256withRSAandMGF1", "BC"); 
	        PSSParameterSpec spec = new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 16, 1); 
			ss.setParameter(spec);
			ss.initVerify(publicKey);
			ss.update(hash);
			boolean isKeySignatureVerified = ss.verify(signatureTobeValidated);
			return isKeySignatureVerified;
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException
				| InvalidAlgorithmParameterException | SignatureException e) {
		}
		return false;

	}
	
	private PublicKey loadDevicePublicKey() throws PostcardNonRetriableException {
		// read key bytes
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			byte[] keyBytes = returnKeyBytes(new String(ByteStreams.toByteArray(loader.getResourceAsStream("certs/jamc/jamc_publickey.pem"))), RSA_KEY_TYPE.PUBLIC);
			// generate private key
			X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = getKeyFactoryInstance();
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			e.printStackTrace();
			throw new PostcardNonRetriableException();
		}
	}
	
	private byte[] returnKeyBytes(String key, RSA_KEY_TYPE rsaKeyType) {
		if(rsaKeyType == RSA_KEY_TYPE.PRIVATE)
			key = key.replaceAll("(-+BEGIN RSA " + rsaKeyType + " KEY-+\\r?\\n?|-+END RSA +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.PUBLIC)
			key = key.replaceAll("(-+BEGIN " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.CERTIFICATE)
			key = key.replaceAll("(-+BEGIN CERTIFICATE-+\\r?\\n?|-+END CERTIFICATE-+\\r?\\n?)","");
		return Base64.decodeBase64(key);
	}
	
	enum RSA_KEY_TYPE {
		PRIVATE, PUBLIC, CERTIFICATE;
	}
	
	private byte[] generateHash(String input) throws PostcardNonRetriableException {
		byte[] hash = null;
		MessageDigest md;
		try {
			md = getMessageDigestInstance();
			hash = md.digest(input.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash;
	}

	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected MessageDigest getMessageDigestInstance() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(PostcardConstants.SHA256_ALGORITHM_NAME);
	}
	
	private String canonize(String input) {
		return input.replaceAll("[\":,\\r\\n\\t {}\\[\\]\\']", "");
	}
	
	private static byte[] generateRSSPSSSignedData(byte[] data) throws Exception {
		byte[] decoded = readTheCipherBytesByKeyType(new File(IntegrityValidatorTest.class.getClassLoader().getResource("certs/device/device_privatekey.pem").getPath()), RSA_KEY_TYPE.PRIVATE); 
		Security.addProvider(new BouncyCastleProvider());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC"); 
        PrivateKey key = kf.generatePrivate(keySpec);

        Signature ss = Signature.getInstance("RSASSA-PSS", "BC"); 
        PSSParameterSpec spec1 = new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 16, 1); 
        ss.setParameter(spec1);
        ss.initSign(key);
        ss.update(data); 
        return ss.sign();
	}
	
	private static PublicKey loadPublicKey(File file) {
		// read key bytes
		try {
			byte[] keyBytes = readTheCipherBytesByKeyType(file, RSA_KEY_TYPE.PUBLIC);

			// generate private key
			 X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keyBytes);
		     return KeyFactory.getInstance("RSA").generatePublic(keySpec);
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected static KeyFactory getKeyFactoryInstance() throws NoSuchAlgorithmException {
		return KeyFactory.getInstance("RSA");
	}

	private static byte[] readTheCipherBytesByKeyType(File file, RSA_KEY_TYPE rsaKeyType) throws FileNotFoundException, IOException, UnsupportedEncodingException {
		FileInputStream in = new FileInputStream(file);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String privateKey = new String(keyBytes, "UTF-8");
		
		if(rsaKeyType == RSA_KEY_TYPE.PRIVATE)
			privateKey = privateKey.replaceAll("(-+BEGIN RSA " + rsaKeyType + " KEY-+\\r?\\n?|-+END RSA +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.PUBLIC)
			privateKey = privateKey.replaceAll("(-+BEGIN " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		
		keyBytes = Base64.decodeBase64(privateKey);
		return keyBytes;
	}
}
