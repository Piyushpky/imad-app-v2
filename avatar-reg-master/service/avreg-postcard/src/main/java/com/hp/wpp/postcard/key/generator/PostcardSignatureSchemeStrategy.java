/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.ByteArrayCipherKey;
import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.PostcardSignatureMismatchException;
import com.hp.wpp.postcard.util.PostcardUtility;

/**
 * @author mahammad
 *
 */
public class PostcardSignatureSchemeStrategy {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardSignatureSchemeStrategy.class);
	
	protected byte[] generateSignature(CipherData cipherData, CipherKey cipherKey) throws PostcardNonRetriableException {
		byte[] signature = null;
		Mac mac;
		try {
			byte[] byteArrayCipherKey = ((ByteArrayCipherKey) cipherKey).getCipherKey();
			SecretKeySpec signingKey = new SecretKeySpec(byteArrayCipherKey, PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			signature = mac.doFinal(cipherData.getCipherData());
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new KeyCertificateException("Exception while generating runtime signature",e);
		}
		return signature;
	}
	
	protected byte[] verifySignature(CipherData cipherData, CipherKey cipherKey, byte[] signatureTobeValidated) throws PostcardNonRetriableException {
		byte[] signature = null;
		Mac mac;
		try {
			byte[] byteArrayCipherKey = ((ByteArrayCipherKey) cipherKey).getCipherKey();
			SecretKeySpec signingKey = new SecretKeySpec(byteArrayCipherKey, PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			signature = mac.doFinal(cipherData.getCipherData());
			LOG.debug("Signature generated: {}",  PostcardUtility.returnFormattedText(signature));
			
			if(Base64.encode(signatureTobeValidated).equals(Base64.encode(signature))) {
				throw new PostcardSignatureMismatchException("Signature Validation failed.");
			}
			
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new KeyCertificateException("Exception while validating runtime signature",e);
		}
		return signature;
	}
	
	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}
	
	protected byte[] generateKeySignature(CipherData cipherData, PrivateKey privateKey, String algoName) throws PostcardNonRetriableException {
		try {
	        Signature ss = Signature.getInstance(algoName, PostcardConstants.BOUNCYCASTLE_PROVIDER_NAME); 
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
	
	protected void verifyKeySignature(CipherData cipherData, PublicKey publicKey, byte[] signatureTobeValidated, String algoName) throws PostcardNonRetriableException {
		Signature ss = null;
		try {
			ss = Signature.getInstance(algoName, PostcardConstants.BOUNCYCASTLE_PROVIDER_NAME); 
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
}
