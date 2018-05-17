/**
 * 
 */
package com.hp.wpp.postcard.common;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.postcard.json.schema.PostcardDomain;
import com.netflix.config.DynamicPropertyFactory;

/**
 * @author mahammad
 *
 */
public class PostcardConfig {
	
	private static final String POSTCARD_IS_ENTITY_SEQ_NUM_VALIDATION_REQUIRED = "postcard.is.entity.seq.num.validation.required";
	private static final String POSTCARD_KEYSTORE_LOCATION = "postcard.keystore.location";
	private static final String POSTCARD_KEYSTORE_PASSWORD = "postcard.keystore.password";
	private static final String POSTCARD_SHAREDSECRET_ENCRYPTION_KEY = "postcard.sharedsecret.encryption.key";
	private static final String POSTCARD_SHAREDSECRET_ENCRYPTION_IV = "postcard.sharedsecret.encryption.iv";
	private static final String POSTCARD_SUPPORTED_DOMAIN = "postcard.supported.domain";
	private static final String POSTCARD_IS_ENTITY_CERT_VALIDATION_REQUIRED = "postcard.is.entity.cert.validation.required";
	
	@Autowired
	private DynamicPropertyFactory dynamicPropertyFactory;
	
	public boolean isEntitySeqNumValidationRequired() {
		return dynamicPropertyFactory.getBooleanProperty(POSTCARD_IS_ENTITY_SEQ_NUM_VALIDATION_REQUIRED, true).get();
	}
	
	public String getJavaCertsKeyStoreLocation() {
		return dynamicPropertyFactory.getStringProperty(POSTCARD_KEYSTORE_LOCATION, "").get();
	}
	
	public String getJavaCertsKeyStorePassword() {
		return dynamicPropertyFactory.getStringProperty(POSTCARD_KEYSTORE_PASSWORD, "changeit").get();
	}
	
	public byte[] getSharedSecretEncryptionKey() {
		return Base64.decodeBase64(dynamicPropertyFactory.getStringProperty(POSTCARD_SHAREDSECRET_ENCRYPTION_KEY, "aeGmTRJ3TNkEXZhT+iTF6w==").get());
	}
	
	public byte[] getSharedSecretEncryptionIV() {
		return Base64.decodeBase64(dynamicPropertyFactory.getStringProperty(POSTCARD_SHAREDSECRET_ENCRYPTION_IV, "JlUvJMD7cc2AbDvEprm8tg==").get());
	}
	
	public String getSupportedDomain() {
		return dynamicPropertyFactory.getStringProperty(POSTCARD_SUPPORTED_DOMAIN, PostcardDomain.certificate_model.value()).get();
	}
	
	public boolean isPostcardEntityCertValidationRequired() {
		return dynamicPropertyFactory.getBooleanProperty(POSTCARD_IS_ENTITY_CERT_VALIDATION_REQUIRED, false).get();
	}
}
