/**
 * 
 */
package com.hp.wpp.postcard.cipher;

/**
 * @author mahammad
 *
 */
public class ByteArrayCipherData extends CipherData {

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.cipher.CipherData#getKeyData()
	 */
	
	private byte[] cipherData;
	
	@Override
	public byte[] getCipherData() {
		return cipherData;
	}
	
	public void setCipherData(byte[] cipherData) {
		this.cipherData = cipherData;
	}

}
