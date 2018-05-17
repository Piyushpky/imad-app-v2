package com.hp.wpp.postcard.key.generator;

import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.InitiateKeyNegotiationException;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;

public interface SecretGenerator {
	
	/**
	 * Retrieve the postcard secret based on printer_uuid.
	 *						OR 
	 * Starts the key negotiation and generates postcard secret.
	 * 
	 * @param postcardContent
	 * @return
	 * @throws InitiateKeyNegotiation
	 * @throws PostcardNonRetriableException
	 */
	public PostcardEntity retrieveAndStoreSecret(PostcardContent postcardContent, String postcardJson) throws InitiateKeyNegotiationException, PostcardNonRetriableException;
	
	public PostcardEntity generateAndStoreSecret(PostcardContent postcardContent, String postcardJson) throws InitiateKeyNegotiationException, PostcardNonRetriableException;
	
	/**
	 * fetches postcard secret based on deviceId
	 * 
	 * @param entityId
	 * @return
	 * @throws SharedKeyNotFoundException
	 * @throws PostcardNonRetriableException
	 */
	public PostcardEntity getSecret(String entityId) throws PostcardEntityNotFoundException, PostcardNonRetriableException;
	
	/**
	 * 
	 * 
	 * @param entityId
	 * @param applicationId
	 * @return
	 * @throws SharedKeyNotFoundException
	 * @throws PostcardNonRetriableException
	 */
	public String generateEntityKey(String cloudId, String entityId, String applicationId) throws PostcardEntityNotFoundException, PostcardNonRetriableException;
	
}
