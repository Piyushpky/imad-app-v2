/**
 * 
 */
package com.hp.wpp.postcard.service;

import java.util.List;

import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardMessage;

/**
 * @author mahammad
 *
 */
public interface PostcardCryptoService {
	
	public PostcardData decryptAndDecompressMessageContents(String postcardVersion, List<PostcardMessage> messages, byte[] dk) throws PostcardNonRetriableException;
	
	public PostcardData decryptAndDecompressInstruction(String postcardVersion, PostcardMessage message, byte[] dk) throws PostcardNonRetriableException;
	
	public List<PostcardMessage> encryptAndCompressMessageContents(String postcardVersion, PostcardData postcardData, byte[] dk) throws PostcardNonRetriableException;
	
}
