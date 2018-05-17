/**
 * 
 */
package com.hp.wpp.postcard.cipher;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.CertificateManager;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.exception.InvalidPostcardCertificateException;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * This guy validates the certificate received in the postcard against the root
 * CA
 * 
 * @author mahammad
 *
 */
public class PostcardCertValidator {

	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(PostcardCertValidator.class);

	@Autowired
	private CertificateManager certificateManager;
	@Autowired
	private PostcardConfig postcardConfig;

	public void verify(String pemCertificate)
			throws PostcardNonRetriableException {
		boolean isValidated = false;
		X509Certificate certificate = (X509Certificate) certificateManager
				.generateCertificate(pemCertificate);
		String issuerDNDetails = certificate.getIssuerDN().getName();
		LOG.debug("Issuer domain name details: {}", issuerDNDetails);
		String subjectDNDetails = certificate.getSubjectDN().getName();
		LOG.debug("Subject domain name details: {}", subjectDNDetails);

		try {

			if (issuerDNDetails.equalsIgnoreCase(subjectDNDetails)) {
				LOG.debug("certificate is selfsigned..");
				verifyWithCA(certificate, certificate);
				checkValidity(certificate);
				isValidated = true;
			} else {
				LOG.debug("certificate is signed CA, now trying to verify with CA in our system.");
				try (FileInputStream is = new FileInputStream(
						postcardConfig.getJavaCertsKeyStoreLocation())) {
					KeyStore keystore = KeyStore.getInstance(KeyStore
							.getDefaultType());
					keystore.load(is, postcardConfig
							.getJavaCertsKeyStorePassword().toCharArray());

					Enumeration<String> aliases = keystore.aliases();
					while (aliases.hasMoreElements()) {
						String alias = aliases.nextElement();
						X509Certificate caCertificate = (X509Certificate) keystore
								.getCertificate(alias);
						if (caCertificate.getSubjectDN().equals(
								certificate.getIssuerDN())) {
							LOG.debug("Found CA signer.. Now CA verification and valifity check is going to happen..");
							verifyWithCA(certificate, caCertificate);
							checkValidity(certificate);
							isValidated = true;
							break;
						}
					}
				} catch (KeyStoreException | NoSuchAlgorithmException
						| CertificateException | IOException e) {
					throw new KeyCertificateException(
							"Exception occured while getting key pair from keystore",
							e);
				}
			}
			if (!isValidated) {
				throw new InvalidPostcardCertificateException(
						"Certificate received in the request is not valid. Either cerificate validation against CA failed or certificate is expired.");
			}
		} catch (Exception e) {
			throw new InvalidPostcardCertificateException(e.getMessage()+". CNDetails="+subjectDNDetails+";");
		}
	}

	private void verifyWithCA(X509Certificate clientCert,
			X509Certificate rootCertificate)
			throws PostcardNonRetriableException {
		try {
			clientCert.verify(rootCertificate.getPublicKey());
			LOG.debug("Certificate is valid after verification with root/selfsigned CA.");
		} catch (InvalidKeyException | CertificateException
				| NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			throw new KeyCertificateException(
					"Exception occured while verifying the certificate with root CA certificate",
					e);
		}
	}

	private void checkValidity(X509Certificate certificate)
			throws PostcardNonRetriableException {
		try {
			certificate.checkValidity();
			LOG.debug("Certificate is valid after validity check.");
		} catch (CertificateExpiredException | CertificateNotYetValidException e) {
			throw new KeyCertificateException(
					"Exception occured while verifying validity of the certificate",
					e);
		}
	}
}
