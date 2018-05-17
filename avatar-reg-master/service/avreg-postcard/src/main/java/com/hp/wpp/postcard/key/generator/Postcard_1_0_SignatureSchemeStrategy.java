/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.cipher.RSACipherKey;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;

/**
 * @author mahammad
 *
 */
@Component
public class Postcard_1_0_SignatureSchemeStrategy extends PostcardSignatureSchemeStrategy implements SignatureSchemeStrategy {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(Postcard_1_0_SignatureSchemeStrategy.class);
	
	@Override
	public byte[] generateSignature(CipherData cipherData, CipherKey cipherKey, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException {
		PrivateKey privateKey = null;
		switch (postcardSignatureScheme) {
			case sha256_with_rsa_and_mfg1:
				privateKey = ((RSACipherKey) cipherKey).getPrivateKey();
				return generateKeySignature(cipherData, privateKey, PostcardConstants.SHA256_WITH_MGF1_ALGORITHM_NAME);
			case hmac_sha256:
				return generateSignature(cipherData, cipherKey);
	
			default:
				throw new UnsupportedPostcardException("Unsupported postcard signature scheme received="+postcardSignatureScheme+", for the requested postcard version");
		}
	}

	@Override
	public void verifySignature(CipherData cipherData, CipherKey cipherKey, byte[] signatureTobeValidated, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException {
		PublicKey publicKey = null;
		switch (postcardSignatureScheme) {
			case sha256_with_rsa_and_mfg1:
				publicKey = ((RSACipherKey) cipherKey).getPublicKey();
				verifyKeySignature(cipherData, publicKey, signatureTobeValidated, PostcardConstants.SHA256_WITH_MGF1_ALGORITHM_NAME);
				break;
			case hmac_sha256:
				verifySignature(cipherData, cipherKey, signatureTobeValidated);
				break;
	
			default:
				throw new UnsupportedPostcardException("Unsupported postcard signature scheme received="+postcardSignatureScheme+", for the requested postcard version");
		}
	}
}
