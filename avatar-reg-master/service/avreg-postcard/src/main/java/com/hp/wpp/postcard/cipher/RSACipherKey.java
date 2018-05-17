/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author mahammad
 *
 */
public class RSACipherKey implements CipherKey {
	
	private PrivateKey privateKey;
	
	private PublicKey publicKey;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return the privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

}
