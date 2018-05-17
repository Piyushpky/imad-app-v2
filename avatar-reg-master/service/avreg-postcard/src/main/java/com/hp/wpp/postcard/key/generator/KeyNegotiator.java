/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.common.PostcardEnums.EnvironmentType;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardContent;

/**
 * @author mahammad
 *
 */
public interface KeyNegotiator {
	
	/**
	 * Validate keys and Generate Salt with keys in the postcard request with key negotiation.
	 * SHA-256 of slat = ASK
	 * 
	 * @param postcardContent
	 * @return
	 * 
	 *<p><b>Throws the following checked exceptions</b>
	 *<p><i> UnsupportedPostcardException</i>
	 *<p><i> PostcardNonRetriableException</i>
	 * 
	 */
	public byte[] validateKeysAndGenerateSalt(PostcardContent postcardContent, String postcardJson) throws UnsupportedPostcardException, PostcardNonRetriableException;
	
	public PostcardContent generateKeysAndSignatures(String postcardVersion, byte[] salt, ClientType clientType, EnvironmentType environmentType) throws UnsupportedPostcardException, PostcardNonRetriableException;

}
