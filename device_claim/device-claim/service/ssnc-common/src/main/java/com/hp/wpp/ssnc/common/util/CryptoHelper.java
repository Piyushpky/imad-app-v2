package com.hp.wpp.ssnc.common.util;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.exception.CryptographyException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoHelper {
	private CryptoHelper(){};
	protected static final WPPLogger LOG = WPPLoggerFactory.getLogger(CryptoHelper.class);

	private static final String encryptionAlgorithm = "AES";

	/**
	 * AES encrypt any secure data
	 * 
	 * @param info
	 * @return
	 * @throws CryptographyException
	 */
	public static byte[] aesEncrypt(byte[] info, String key) {
		LOG.debug("Trying to aes encrypt");
		// Generate the secret key specs.
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), encryptionAlgorithm);

		try {
			Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal(info);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("Failed to Encrypt data");
			throw new CryptographyException(e);

		}

	}

	/**
	 * Aes decrypt any crypted data
	 * 
	 * @param cryptedInfo
	 * @return
	 * @throws CryptographyException
	 */
	public static byte[] aesDecrypt(byte[] cryptedInfo, String key){
		LOG.debug("Trying to aes decrypt");

		// Generate the secret key specs.
		SecretKeySpec secretKeySpec;
		secretKeySpec = new SecretKeySpec(key.getBytes(), encryptionAlgorithm);

		try {
			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedInfo = cipher.doFinal(cryptedInfo);
			return decryptedInfo;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("Failed to Decrypt value", e);
			throw new CryptographyException(e);

		}
	}

	public static byte[] aesEncrypts(String info, String key) {
		
		LOG.debug("Trying to aes encrypt");
		// Generate the secret key specs.
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), encryptionAlgorithm);

		try {
			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal((info).getBytes());
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("LogMessage.DATA_ENCRYPTION_FAIL", e);
			throw new CryptographyException(e);
		}
	}

	/**
	 * Aes decrypt any crypted data
	 * 
	 * @param cryptedInfo
	 * @return
	 * @throws CryptographyException
	 */
	public static String aesDecrypts(byte[] cryptedInfo, String key)  {
		LOG.debug("Trying to aes decrypt");
		
		// Generate the secret key specs.
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), encryptionAlgorithm);

		try {
			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedInfo = cipher.doFinal(cryptedInfo);
			return new String(decryptedInfo);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("LogMessage.DATA_DECRYPTION_FAIL", e);
			throw new CryptographyException(e);

		}
	}

	/**
	 * Aes encrypt the input and base64 encode it
	 * 
	 * @param info
	 * @return
	 * @throws CryptographyException
	 */
	public static String aesEncryptBase64(String info, String key)  {
		byte[] encrypted = aesEncrypts(info, key);
		return encodeBase64(encrypted).trim();
	}

	/**
	 * Aes decrypt the input after base64 decoding it
	 * 
	 * @param base64Encrypted
	 * @return
	 * @throws CryptographyException
	 */
	public static String aesDecryptBase64(String base64Encrypted, String key) {
		byte[] encrypted = decodeBase64(base64Encrypted.trim());
		return aesDecrypts(encrypted, key);
	}

	/**
	 * Aes decrypt the input after base64 decoding it
	 * 
	 * @param base64Encrypted
	 * @return
	 * @throws CryptographyException
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * Aes decrypt the input after base64 decoding it
	 * 
	 * @param base64Encrypted
	 * @return
	 * @throws CryptographyException
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

}
