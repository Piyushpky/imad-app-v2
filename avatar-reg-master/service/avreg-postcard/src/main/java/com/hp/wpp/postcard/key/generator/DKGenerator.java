/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
public interface DKGenerator {
	
	/**
	 * Generates derived key.
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	public byte[] generateDK(byte[] password, byte[] salt) throws PostcardNonRetriableException;

}
