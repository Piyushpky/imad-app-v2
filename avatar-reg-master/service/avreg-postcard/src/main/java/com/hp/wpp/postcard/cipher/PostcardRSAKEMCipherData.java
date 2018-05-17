/**
 * 
 */
package com.hp.wpp.postcard.cipher;

/**
 * @author mahammad
 *
 */
public class PostcardRSAKEMCipherData extends CipherData {

	private byte[] keyData;
	
	private String serverPublicKeyId;
	
	private String entityGeneratedSignature;
	
	private String entityPemCertificate;

	/**
	 * @return the serverPublicKeyId
	 */
	public String getServerPublicKeyId() {
		return serverPublicKeyId;
	}

	/**
	 * @param serverPublicKeyId the serverPublicKeyId to set
	 */
	public void setServerPublicKeyId(String serverPublicKeyId) {
		this.serverPublicKeyId = serverPublicKeyId;
	}

	/**
	 * @return the deviceSignature
	 */
	public String getEntityGeneratedSignature() {
		return entityGeneratedSignature;
	}

	/**
	 * @param entityGeneratedSignature the deviceSignature to set
	 */
	public void setEntityGeneratedSignature(String entityGeneratedSignature) {
		this.entityGeneratedSignature = entityGeneratedSignature;
	}

	/**
	 * @return the devicePemCertificate
	 */
	public String getEntityPemCertificate() {
		return entityPemCertificate;
	}

	/**
	 * @param entityPemCertificate the devicePemCertificate to set
	 */
	public void setEntityPemCertificate(String entityPemCertificate) {
		this.entityPemCertificate = entityPemCertificate;
	}

	public byte[] getCipherData() {
		return keyData;
	}

	public void setKeyData(byte[] data) {
		this.keyData = data;
	}

}
