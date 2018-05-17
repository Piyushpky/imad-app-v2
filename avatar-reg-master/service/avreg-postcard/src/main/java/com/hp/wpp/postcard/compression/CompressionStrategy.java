/**
 * 
 */
package com.hp.wpp.postcard.compression;

import java.io.IOException;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardCompression;

/**
 * @author mahammad
 *
 */
public interface CompressionStrategy {
	
	public byte[] compress(byte[] data, PostcardCompression postcardCompression) throws IOException, PostcardNonRetriableException ;
	
	public byte[] unCompress(byte[] data, PostcardCompression postcardCompression) throws IOException, PostcardNonRetriableException ;
}
