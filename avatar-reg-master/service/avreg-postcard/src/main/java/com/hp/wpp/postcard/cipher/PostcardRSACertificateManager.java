/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
public class PostcardRSACertificateManager implements CertificateManager {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardRSACertificateManager.class);
	
	@Autowired
	private PostcardConfig postcardConfig;

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.cipher.CertificateManager#generatePrivateKey(byte[])
	 */
	@Override
	public PrivateKey generatePrivateKey(String pemData) throws PostcardNonRetriableException {
		// read key bytes
		try {
			byte[] keyBytes = returnKeyBytes(pemData, RSA_KEY_TYPE.PRIVATE);
			// generate private key
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = getKeyFactoryInstance();
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			throw new KeyCertificateException("Exception occured while generating private key",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.cipher.CertificateManager#generatePublicKey(byte[])
	 */
	@Override
	public PublicKey generatePublicKey(String pemData) throws PostcardNonRetriableException {
		try {
			byte[] keyBytes = returnKeyBytes(pemData, RSA_KEY_TYPE.PUBLIC);
			// generate public key
			X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = getKeyFactoryInstance();
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			throw new KeyCertificateException("Exception occured while generating public key",e);
		}
	}

	@Override
	public PublicKey generatePublicKeyFromCertificate(String certificateData) throws PostcardNonRetriableException {
		X509Certificate cer = (X509Certificate) generateCertificate(certificateData);
		return cer.getPublicKey();
	}
	
	@Override
	public X509Extension generateCertificate(String certificateData) throws PostcardNonRetriableException {
		try {
			CertificateFactory ctf = getCertificateFactoryInstance();
			ByteArrayInputStream bis = new ByteArrayInputStream(returnKeyBytes(certificateData, RSA_KEY_TYPE.CERTIFICATE));
	        X509Certificate cer = (X509Certificate) ctf.generateCertificate(bis);
	        bis.close();
	        return cer;
		} catch (CertificateException | IOException e) {
			throw new KeyCertificateException("Exception occured while generating certificate",e);
		}
	}
	
	enum RSA_KEY_TYPE {
		PRIVATE, PUBLIC, CERTIFICATE;
	}
	
	protected KeyFactory getKeyFactoryInstance() throws NoSuchAlgorithmException, NoSuchProviderException {
		return KeyFactory.getInstance(PostcardConstants.RSA_ALGORITHM_NAME);
	}
	
	protected CertificateFactory getCertificateFactoryInstance() throws CertificateException {
		return CertificateFactory.getInstance(PostcardConstants.X_509_CERTIFICATE_TYPE_NAME);
	}
	
	/**
	 * Removes the RSA key header and footers
	 * 
	 * @param key
	 * @param rsaKeyType
	 * @return
	 */
	private byte[] returnKeyBytes(String key, RSA_KEY_TYPE rsaKeyType) {
		if(rsaKeyType == RSA_KEY_TYPE.PRIVATE)
			key = key.replaceAll("(-+BEGIN RSA " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " RSA KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.PUBLIC)
			key = key.replaceAll("(-+BEGIN " + rsaKeyType + " KEY-+\\r?\\n?|-+END +" + rsaKeyType + " KEY-+\\r?\\n?)","");
		else if(rsaKeyType == RSA_KEY_TYPE.CERTIFICATE)
			key = key.replaceAll("(-+BEGIN CERTIFICATE-+\\r?\\n?|-+END CERTIFICATE-+\\r?\\n?)","");
		return Base64.decodeBase64(key);
	}

	@Override
	public KeyPair getRsaKeyPair(String serialNumber) throws PostcardNonRetriableException {
		String defaultJavaKeyStoreLocaton = System.getenv("JAVA_HOME") + "\\jre\\lib\\security\\cacerts";
		LOG.debug("Java default keystore location: {}", defaultJavaKeyStoreLocaton);
		return getRsaKeyPair(defaultJavaKeyStoreLocaton, serialNumber);
	}

	@Override
	public KeyPair getRsaKeyPair(String keystoreFileLocation, String serialNumber) throws PostcardNonRetriableException {
		try {
			FileInputStream is = new FileInputStream(keystoreFileLocation);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, postcardConfig.getJavaCertsKeyStorePassword().toCharArray());
			return getKeyPair(keystore, serialNumber);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyCertificateException("Exception occured while getting key pair from keystore",e);
		}
	}
	
	private KeyPair getKeyPair(KeyStore keystore, String serialNumber) throws PostcardNonRetriableException {
		try {
			Enumeration<String> aliases = keystore.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				Key key = keystore.getKey(alias, postcardConfig.getJavaCertsKeyStorePassword().toCharArray());
				if (key instanceof PrivateKey) {
					RSAPrivateKey privateKey = (RSAPrivateKey) key;

					X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
					if (serialNumber.equalsIgnoreCase(certificate.getSerialNumber().toString(16))) {
						KeyPair keyPair = new KeyPair(certificate.getPublicKey(), privateKey);
						return keyPair;
					}
				}
			}
			throw new InvalidPostcardException("Unable to find keypair from truststore for serial number=" + serialNumber);
		} catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
			throw new KeyCertificateException("Exception occured while getting key pair from keystore",e);
		}
	}

}
