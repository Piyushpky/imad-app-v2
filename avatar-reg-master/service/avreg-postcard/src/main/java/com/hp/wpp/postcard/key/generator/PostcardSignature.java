/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

/**
 * @author mahammad
 *
 */
public class PostcardSignature {
	
	private String hash;
	
	private String signature;
	
	private String signatureScheme;

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureScheme() {
		return signatureScheme;
	}

	public void setSignatureScheme(String signatureScheme) {
		this.signatureScheme = signatureScheme;
	}

}
