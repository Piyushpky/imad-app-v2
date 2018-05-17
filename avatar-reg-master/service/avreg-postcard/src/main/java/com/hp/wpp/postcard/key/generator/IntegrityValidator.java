/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;


/**
 * @author mahammad
 *
 */
public interface IntegrityValidator {
	
	/**
	 * Validates postcard hash and signatures.
	 * 
	 * @param postcardContent
	 * @param key
	 * 
	 *<p><b>Throws the following checked exceptions</b>
	 *<p><i> PostcardNonRetriableException</i>
	 * 
	 */
	public void validateHashAndSignature(PostcardContent postcardContent, byte[] key, PostcardSignature postcardSignatureTobeValidated) throws PostcardNonRetriableException;
	
	/**
	 * Generates postcard hash and signatures.
	 * 
	 * @param inputContent
	 * 
	 *<p><b>Throws the following checked exceptions</b>
	 *<p><i> PostcardNonRetriableException</i>
	 *
	 */
	public PostcardSignature generateHashAndSignatures(PostcardContent postcardContent, byte[] key) throws PostcardNonRetriableException;
	
	public PostcardSignature generateHashAndSignaturesForKeyNegotiation(PostcardContent postcardContent, ClientType clientType) throws PostcardNonRetriableException;
	
	/**
	 * Validates key signatures during key negotiation.
	 * 
	 * @param cipherData
	 * @param cipherKey
	 * @param signatureTobeValidated
	 * 
	 *<p><b>Throws the following checked exceptions</b>
	 *<p><i> PostcardNonRetriableException</i>
	 * 
	 */
	public void validateKeySignature(CipherData cipherData, CipherKey cipherKey, byte[] signatureTobeValidated, PostcardSignatureScheme postcardSignatureScheme) throws PostcardNonRetriableException;
	
	public byte[] generateKeySignature(CipherData cipherData, CipherKey cipherKey) throws PostcardNonRetriableException;
	
}
