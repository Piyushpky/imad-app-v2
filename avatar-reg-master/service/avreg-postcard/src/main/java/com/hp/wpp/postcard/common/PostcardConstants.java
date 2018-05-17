/**
 * 
 */
package com.hp.wpp.postcard.common;

/**
 * @author mahammad
 *
 */
public final class PostcardConstants {

	public static final String POSTCARD_PROTOCOL_VERSION = "1.0";
	public static final String POSTCARD_VERSION = "1.0";
	public static final String POSTCARD_MIME_TYPE = "application/vnd.hpi.postcard+json";
	public static final String POSTCARD_SERVICE_INSTRUCTION_CONTENT_TYPE = "application/vnd.hpi.postcard.service_instruction+json";
	public static final String POSTCARD_DEVICE_INSTRUCTION_CONTENT_TYPE = "application/vnd.hpi.postcard.entity_instruction+json";

	public static final String POSTCARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String UNIVERSAL_TIME_ZONE = "GMT";
	
	// Encryption/Encoding algorithm names
	public static final String X_509_CERTIFICATE_TYPE_NAME = "X.509";
	public static final String SHA256_ALGORITHM_NAME = "SHA-256";
	public static final String MASK_GENERATION_FUNCTION_NAME = "MGF1";
	public static final String SHA_256_ALGORITHM_NAME = "SHA-256";
	public static final String BOUNCYCASTLE_PROVIDER_NAME = "BC";
	public static final String RSASSA_PSS_ALGORITHM_NAME = "SHA256withRSAandMGF1";
	public static final String SHA256_WITH_MGF1_ALGORITHM_NAME = "SHA256withRSAandMGF1";
	public static final String RAW_RSASSA_PSS_ALGORITHM_NAME = "RAWRSASSA-PSS";
	public static final int RSASSA_PSS_ALGORITHM_TRAILER_FIELD = 1;
	public static final String RSA_ALGORITHM_NAME = "RSA";
	public static final String HMAC_SHA256 = "HmacSHA256";
	
	// keys with default size(bytes) as per spec
	public static final int POSTCARD_SALT_SIZE = 255;
	public static final int POSTCARD_SECRET_KEY_SIZE = 16;
	public static final int DK_ITERATION_COUNT = 1033;
	public static final int DERIVED_KEY_SIZE = 48;
	public static final int MEK_LENGTH = 16;
	public static final int MAK_LENGTH = 32;
	public static final int POSTCARD_ID_LENGTH = 32;
	public static final int IV_LENGTH = 16;
	// used in integrity check in key negotiation
	public static final int RSASSA_PSS_ALGO_SALT_LENGTH = 16;
	
	public static final String SERVER_PRIVATEKEY_PEM = "certs/server/server_privatekey.pem";
	public static final String SERVER_CERT = "certs/server/server_cert.pem";
	public static final String SERVER_PUBLICKEY_PEM = "certs/server/server_publickey.pem";
	
	public static final String DEVICE_PRIVATEKEY_PEM = "certs/device/device_privatekey.pem";
	public static final String DEVICE_CERT = "certs/device/device_cert.pem";
	public static final String DEVICE_PUBLICKEY_PEM = "certs/device/device_publickey.pem";
	
	public static final String JAMC_PRIVATEKEY_PEM = "certs/jamc/jamc_privatekey.pem";
	public static final String JAMC_CERT = "certs/jamc/jamc_cert.pem";
	public static final String JAMC_PUBLICKEY_PEM = "certs/jamc/jamc_publickey.pem";
	
	public static final String JAVA_KEY_STORE = "certs/server/java_keystore.jks";
	
	public static final String GENERATE_NEW_SECRET_COMMAND = "generate_new_secret";
	public static final String SERVICE_INSTRUCTION_OPTION = "sign_with_entity_private_key";
	
	// hex or default: base64
	public static final String LOG_ENCODING_TYPE = "hex";

}
