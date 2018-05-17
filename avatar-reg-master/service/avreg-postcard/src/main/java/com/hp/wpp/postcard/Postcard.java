/**
 * 
 */
package com.hp.wpp.postcard;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 *
 * Helps to decrypt and encrypt the postcard.
 * <p>
 * 		1. Parse and validate postcard. Decrypt the postcard.
 * <p>
 * 		2. Encrypt the postcard with application session key of deviceId.
 * <p>
 *
 *
 * @author mahammad
 *
 */
public interface Postcard {

	/**
	 * Parse and validate postcard. 
	 * 		Validates hash and signatures of postcard.
	 * 		Do key negotiation for the first time and generates shared secret.
	 * 		Decrypt the messages with shared secret.
	 * 
	 * @param json formatted postcard
	 * @return PostcardData -- decrypted messages
	 * 
	 *<p><b>Throws the following exceptions</b>
	 *<p><i> UnsupportedPostcardException</i>
	 *<p><i> PostcardMalformedException</i>
	 *<p><i> InvalidPostcardException</i>
	 *<p><i> IntegrityValidationFailureException</i>
	 *<p><i> PostcardNonRetriableException</i>
	 *<p><i> PostcardRetriableException</i>
	 *<p><i> InitiateKeyNegotiationException</i>
	 *<p><i> PostcardNonRetriableException</i>
	 * 
	 */
	public PostcardData validateAndDecryptPostcard(String postcard) throws PostcardNonRetriableException;
	
	/**
	 * Encrypt the content with shared secret and generate postcard.
	 * 
	 * @param PostcardData -- contents to be encrypted
	 * @return json formatted postcard
	 * 
	 *<p><b>Throws the following exceptions</b>
	 *<p><i> UnsupportedPostcardException</i>
	 *<p><i> PostcardMalformedException</i>
	 *<p><i> InvalidPostcardException</i>
	 *<p><i> IntegrityValidationFailureException</i>
	 *<p><i> PostcardNonRetryableException</i>
	 *<p><i> PostcardRetriableException</i>
	 *<p><i> InitiateKeyNegotiationException</i>
	 *<p><i> PostcardNonRetriableException</i>
	 * 
	 */
	public String encryptPostcard(PostcardData postcardData) throws PostcardNonRetriableException;
	
	/**
	 * generates entity key with shared secret for entity id and application id.
	 * 
	 * @param entityId
	 * @param applicationId
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public String generateEntityKey(String cloudId, String entityId, String applicationId) throws PostcardNonRetriableException;
	
	/**
	 * validate entity key for the given entityId and applicationId
	 * 
	 * @param entityId
	 * @param applicationId
	 * @param entityKey -- base64 encoded
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public boolean isValidKey(String cloudId, String entityId, String applicationId, String entityKey) throws PostcardNonRetriableException;
	
	
	/**
	 * Update/Refresh the shared secret
	 * 
	 * @param postcard
	 * @throws PostcardNonRetriableException
	 */
	public void refreshSharedSecret(String postcard) throws PostcardNonRetriableException;
	
}
