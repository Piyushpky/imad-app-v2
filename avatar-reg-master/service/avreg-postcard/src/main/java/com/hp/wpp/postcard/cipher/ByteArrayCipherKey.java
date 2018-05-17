/**
 * 
 */
package com.hp.wpp.postcard.cipher;

/**
 * @author mahammad
 *
 */
public class ByteArrayCipherKey implements CipherKey {
	
	private byte[] cipherKey;

	public byte[] getCipherKey() {
		return cipherKey;
	}

	public void setCipherKey(byte[] cipherKey) {
		this.cipherKey = cipherKey;
	}
}
