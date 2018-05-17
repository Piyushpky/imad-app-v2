/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

/**
 * @author mahammad
 *
 */
public class SignatureData {
	
	private byte[] data;
	
	private byte[] key;
	
	private String signatureScheme;
	
	private byte[] signatureTobeVerified;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getSignatureTobeVerified() {
		return signatureTobeVerified;
	}

	public void setSignatureTobeVerified(byte[] signatureTobeVerified) {
		this.signatureTobeVerified = signatureTobeVerified;
	}
	
	public String getSignatureScheme() {
		return signatureScheme;
	}

	public void setSignatureScheme(String signatureScheme) {
		this.signatureScheme = signatureScheme;
	}
}
