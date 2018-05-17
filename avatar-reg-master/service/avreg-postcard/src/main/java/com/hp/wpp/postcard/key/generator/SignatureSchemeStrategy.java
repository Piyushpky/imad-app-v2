/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;

/**
 * @author mahammad
 *
 */
public interface SignatureSchemeStrategy {
	
	public byte[] generateSignature(CipherData cipherData, CipherKey cipherKey, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException ;
	
	public void verifySignature(CipherData cipherData, CipherKey cipherKey, byte[] signatureTobeValidated, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException ;
	
}
