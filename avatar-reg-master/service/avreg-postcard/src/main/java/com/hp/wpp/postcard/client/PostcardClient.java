/**
 * 
 */
package com.hp.wpp.postcard.client;

import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.common.PostcardEnums.EnvironmentType;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
public interface PostcardClient {
	
	public String encryptPostcard(PostcardData postcardData, Creator creator) throws PostcardNonRetriableException;
	
	/**
	 * Generates postcard with key negotiation material. This will be used by client to generate first time postcard request. Service will never use this.
	 * 
	 * @param postcardData
	 * @return
	 *<p><b>Throws the following exceptions</b>
	 *<p><i> PostcardRetriableException</i>
	 *<p><i> PostcardNonRetriableException</i>
	 */
	public String generatePostcardForKeyNegotiation(PostcardData postcardData, ClientType clientType, Creator creator, EnvironmentType environmentType) throws PostcardNonRetriableException;
	
	/**
	 * 
	 * 
	 * @param postcard
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public PostcardData validateAndDecryptInstruction(String postcard) throws PostcardNonRetriableException;

}
