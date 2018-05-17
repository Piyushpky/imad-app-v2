/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import javax.crypto.Mac;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.ByteArrayCipherData;
import com.hp.wpp.postcard.cipher.ByteArrayCipherKey;
import com.hp.wpp.postcard.cipher.CertificateManager;
import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.cipher.RSACipherKey;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardHashMismatchException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.PostcardSignatureMismatchException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.version.factory.PostcardVersionStrategyFactory;

/**
 * @author mahammad
 *
 */
public class RSAIntegrityValidator implements IntegrityValidator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RSAIntegrityValidator.class);
	
	@Autowired
	private CertificateManager certificateManager;
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private PostcardVersionStrategyFactory postcardVersionFactory;
	
	static {
		// any problems in adding BouncyCasthe as Security provider
		// TODO find a way to change the Provider dynamically
		Security.addProvider(new BouncyCastleProvider());
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.key.generator.IntegrityValidator#validateHashAndSignature(com.hp.wpp.postcard.json.schemas.PostcardContent, byte[])
	 */
	@Override
	public void validateHashAndSignature(PostcardContent postcardContent, byte[] key, PostcardSignature postcardSignatureTobeValidated) throws PostcardNonRetriableException {
		
		String signedInfo = postcardParser.serialize(postcardContent.getPostcardSignedInfo());
		LOG.trace("serialized signedinfo: {}", signedInfo);
		String canonizedSignedInfo = PostcardUtility.canonize(signedInfo);
		LOG.trace("canonized signed info: {}", canonizedSignedInfo);
		
		byte[] hash = PostcardUtility.generateSHA256Hash(canonizedSignedInfo.getBytes(), -1);
		String base64Hash = Base64.encodeBase64String(hash);
		LOG.debug("hash generated: {}",  PostcardUtility.returnFormattedText(hash));
		
		if(!base64Hash.equals(postcardSignatureTobeValidated.getHash())) {
			throw new PostcardHashMismatchException("Hash validation failed.");
		}
		
		ByteArrayCipherData byteArrayCipherData = new ByteArrayCipherData();
		byteArrayCipherData.setCipherData(hash);
		ByteArrayCipherKey byteArrayCipherKey = new ByteArrayCipherKey();
		byteArrayCipherKey.setCipherKey(key);
		
		postcardVersionFactory.getSignatureSchemeStrategy(postcardContent.getPostcardSignedInfo().getVersion())
				.verifySignature(byteArrayCipherData, byteArrayCipherKey,
						Base64.decodeBase64(postcardSignatureTobeValidated.getSignature()),
						PostcardSignatureScheme.fromValue(postcardSignatureTobeValidated.getSignatureScheme()));
		
	}

	@Override
	public PostcardSignature generateHashAndSignatures(PostcardContent postcardContent, byte[] key) throws PostcardNonRetriableException {
		
		String signedInfo = postcardParser.serialize(postcardContent.getPostcardSignedInfo());
		LOG.trace("serialized signedinfo: {}", signedInfo);
		String canonizedSignedInfo = PostcardUtility.canonize(signedInfo);
		LOG.trace("canonized signed info: {}", canonizedSignedInfo);
		
		byte[] hash = PostcardUtility.generateSHA256Hash(canonizedSignedInfo.getBytes(), -1);
		LOG.debug("hash generated: {}", PostcardUtility.returnFormattedText(hash));
		
		ByteArrayCipherData byteArrayCipherData = new ByteArrayCipherData();
		byteArrayCipherData.setCipherData(hash);
		ByteArrayCipherKey byteArrayCipherKey = new ByteArrayCipherKey();
		byteArrayCipherKey.setCipherKey(key);
		
		byte[] signature = postcardVersionFactory.getSignatureSchemeStrategy(postcardContent.getPostcardSignedInfo().getVersion()).generateSignature(byteArrayCipherData, byteArrayCipherKey, PostcardSignatureScheme.hmac_sha256);
		LOG.debug("Signature generated: {}", PostcardUtility.returnFormattedText(signature));
		
		PostcardSignature postcardSignature = new PostcardSignature();
		postcardSignature.setHash(Base64.encodeBase64String(hash));
		postcardSignature.setSignature(Base64.encodeBase64String(signature));
		if("1.1".equals(postcardContent.getPostcardSignedInfo().getVersion())) {
			postcardSignature.setSignatureScheme(PostcardSignatureScheme.hmac_sha256.name());
		} 
		return postcardSignature;
	}
	
	@Override
	public PostcardSignature generateHashAndSignaturesForKeyNegotiation(PostcardContent postcardContent, ClientType clientType) throws PostcardNonRetriableException {
		String signedInfo = postcardParser.serialize(postcardContent.getPostcardSignedInfo());
		LOG.trace("serialized signedinfo: {}", signedInfo);
		String canonizedSignedInfo = PostcardUtility.canonize(signedInfo);
		LOG.trace("canonized signed info: {}", canonizedSignedInfo);
		
		byte[] hash = PostcardUtility.generateSHA256Hash(canonizedSignedInfo.getBytes(), -1);
		LOG.debug("hash generated: {}", PostcardUtility.returnFormattedText(hash));
		
		ByteArrayCipherData cipherData = new ByteArrayCipherData();
		cipherData.setCipherData(hash);
		
		RSACipherKey cipherKey = new RSACipherKey();
		try {
			String clientPrivateKey = new String(clientType.getPrivateKey());
			cipherKey.setPrivateKey(certificateManager.generatePrivateKey(clientPrivateKey));
		} catch (IOException e) {
			throw new KeyCertificateException("Exception occured while genearting private key",e);
		}
		
		PostcardSignature postcardSignature = new PostcardSignature();
		
		// TODO move this condition logic to some common place.
		PostcardSignatureScheme postcardSignatureScheme = null;
		if("1.0".equals(postcardContent.getPostcardSignedInfo().getVersion())) {
			postcardSignatureScheme = PostcardSignatureScheme.sha256_with_rsa_and_mfg1;
		} else if("1.1".equals(postcardContent.getPostcardSignedInfo().getVersion())) {
			postcardSignatureScheme = PostcardSignatureScheme.raw_rsassa_pss;
			postcardSignature.setSignatureScheme(PostcardSignatureScheme.raw_rsassa_pss.name());
		} else {
			throw new UnsupportedPostcardException("Unsupported postcard version received="+ postcardContent.getPostcardSignedInfo().getVersion());
		}
		
		byte[] signature = postcardVersionFactory.getSignatureSchemeStrategy(postcardContent.getPostcardSignedInfo().getVersion()).generateSignature(cipherData, cipherKey, postcardSignatureScheme);
		LOG.debug("Signature generated: {}", PostcardUtility.returnFormattedText(signature));
		
		postcardSignature.setHash(Base64.encodeBase64String(hash));
		postcardSignature.setSignature(Base64.encodeBase64String(signature));
		return postcardSignature;
	}
	
	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}

	@Override
	public void validateKeySignature(CipherData cipherData, CipherKey cipherKey, byte[] signatureTobeValidated, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException {
		PublicKey publicKey = ((RSACipherKey) cipherKey).getPublicKey();
		Signature ss = null;
		try {
			switch (postcardSignatureScheme) {
				case sha256_with_rsa_and_mfg1:
					ss = Signature.getInstance(PostcardConstants.RSASSA_PSS_ALGORITHM_NAME, PostcardConstants.BOUNCYCASTLE_PROVIDER_NAME);
					break;
				case raw_rsassa_pss:
					ss = Signature.getInstance(PostcardConstants.RAW_RSASSA_PSS_ALGORITHM_NAME, PostcardConstants.BOUNCYCASTLE_PROVIDER_NAME);
				default:
					throw new UnsupportedPostcardException("Unsupported signatue scheme received="+ postcardSignatureScheme);
			}
			
			PSSParameterSpec spec = new PSSParameterSpec(PostcardConstants.SHA_256_ALGORITHM_NAME, PostcardConstants.MASK_GENERATION_FUNCTION_NAME, new MGF1ParameterSpec(PostcardConstants.SHA_256_ALGORITHM_NAME), PostcardConstants.RSASSA_PSS_ALGO_SALT_LENGTH, PostcardConstants.RSASSA_PSS_ALGORITHM_TRAILER_FIELD);
			ss.setParameter(spec);
			ss.initVerify(publicKey);
			LOG.trace("Key negotiation data: {}", PostcardUtility.returnFormattedText(cipherData.getCipherData()));
			ss.update(cipherData.getCipherData());
			LOG.trace("Key negotiation signature: {}", PostcardUtility.returnFormattedText(signatureTobeValidated));
			boolean isKeySignatureVerified = ss.verify(signatureTobeValidated);
			if(!isKeySignatureVerified) {
				throw new PostcardSignatureMismatchException("Key signature validation failed.Signature received="+PostcardUtility.returnFormattedText(signatureTobeValidated));
			}
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | InvalidAlgorithmParameterException | SignatureException e) {
			throw new KeyCertificateException("Exception occured while validating key signatures",e);
		}
		
	}

	@Override
	public byte[] generateKeySignature(CipherData cipherData, CipherKey cipherKey) throws PostcardNonRetriableException {
		try {
			PrivateKey privateKey = ((RSACipherKey) cipherKey).getPrivateKey();
	        Signature ss = Signature.getInstance(PostcardConstants.RSASSA_PSS_ALGORITHM_NAME, PostcardConstants.BOUNCYCASTLE_PROVIDER_NAME); 
	        PSSParameterSpec spec1 = new PSSParameterSpec(PostcardConstants.SHA_256_ALGORITHM_NAME, PostcardConstants.MASK_GENERATION_FUNCTION_NAME, new MGF1ParameterSpec(PostcardConstants.SHA_256_ALGORITHM_NAME), PostcardConstants.RSASSA_PSS_ALGO_SALT_LENGTH, PostcardConstants.RSASSA_PSS_ALGORITHM_TRAILER_FIELD); 
	        ss.setParameter(spec1);
	        ss.initSign(privateKey);
	        LOG.trace("Key negotiation data for signing: {}", PostcardUtility.returnFormattedText(cipherData.getCipherData()));
	        ss.update(cipherData.getCipherData()); 
	        byte[] signature = ss.sign();
	        LOG.trace("Key negotiation signature: {}", PostcardUtility.returnFormattedText(signature));
	        return signature;
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | InvalidAlgorithmParameterException | SignatureException e) {
			throw new KeyCertificateException("Exception occured while generating key signatures",e);
		} 
		
	}
}
