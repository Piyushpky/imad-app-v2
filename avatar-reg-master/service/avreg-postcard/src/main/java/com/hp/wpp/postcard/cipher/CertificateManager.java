/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Extension;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
public interface CertificateManager {
	
	/**
	 * Generate RSA Private key with given base64 encoded pem format data
	 * 
	 * @param base64PemData
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public PrivateKey generatePrivateKey(String pemData) throws PostcardNonRetriableException;
	
	/**
	 * Generate RSA Public key with given base64 encoded pem format data
	 * 
	 * @param base64PemData
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public PublicKey generatePublicKey(String pemData) throws PostcardNonRetriableException;
	
	/**
	 * Generate RSA Public key with given base64 encoded certificate data
	 * 
	 * @param certificateData
	 * @return
	 * @throws PostcardNonRetriableException
	 */
	public PublicKey generatePublicKeyFromCertificate(String certificateData) throws PostcardNonRetriableException;
	
	public X509Extension generateCertificate(String certificateData) throws PostcardNonRetriableException;
	
	public KeyPair getRsaKeyPair(String serialNumber) throws PostcardNonRetriableException;
	
	public KeyPair getRsaKeyPair(String keystoreFileLocation, String serialNumber) throws PostcardNonRetriableException;
	
}
