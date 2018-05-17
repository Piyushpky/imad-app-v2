package com.hp.wpp.postcard.cipher;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;


public interface PostcardCipher {
	
	/**
	 * 
	 * 
	 * @param cipherData
	 * @param cipherKey
	 * @return
	 */
	public byte[] decrypt(CipherData cipherData, CipherKey cipherKey) throws PostcardNonRetriableException;
	
	public byte[] encrypt(byte[] cipherData, CipherKey cipherKey) throws PostcardNonRetriableException;
	
	public byte[] decrypt(byte[] content, byte[] key, byte[] iv) throws PostcardNonRetriableException;
	
	public byte[] encrypt(byte[] content, byte[] key, byte[] iv) throws PostcardNonRetriableException;
}
