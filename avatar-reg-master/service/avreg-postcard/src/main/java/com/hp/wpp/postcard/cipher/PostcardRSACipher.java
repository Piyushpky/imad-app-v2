package com.hp.wpp.postcard.cipher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.exception.EmptyCipherInputsException;
import com.hp.wpp.postcard.exception.PostcardDecryptFailureException;
import com.hp.wpp.postcard.exception.PostcardEncryptFailureException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

public class PostcardRSACipher implements PostcardCipher {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardRSACipher.class);

	private static final String RSA_ALGORITHM_NAME = "RSA/NONE/NoPadding";

	private static final String AES_CBC_PK7PADDING_TRANSFORMATION_NAME = "AES/CBC/PKCS7Padding";
	
	private static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";
	
	private static final String AES_ALGORITHM_NAME = "AES";
	
	static {
		// any problems in adding BouncyCasthe as Security provider
		// TODO find a way to change the Provider dynamically
		Security.addProvider(new BouncyCastleProvider());
	}

	@Override
	public byte[] decrypt(CipherData cipherData, CipherKey cipherKey) throws PostcardNonRetriableException {
		
		if(cipherData == null || ArrayUtils.isEmpty(cipherData.getCipherData()) || cipherKey == null) {
			throw new EmptyCipherInputsException("Empty CipherData or CipherKey for decryption..");
		}
		
		byte[] dataToDecrypt = cipherData.getCipherData();
		PrivateKey privateKey = ((RSACipherKey) cipherKey).getPrivateKey();
		byte[] result = null;
		try {
			Cipher aesCipher = Cipher.getInstance(RSA_ALGORITHM_NAME, BOUNCY_CASTLE_PROVIDER_NAME);
			aesCipher.init(Cipher.DECRYPT_MODE, privateKey);
			result = aesCipher.doFinal(dataToDecrypt);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new PostcardDecryptFailureException("Exception occured while key decryption",e);
		}
		return result;
	}
	
	@Override
	public byte[] encrypt(byte[] cipherData, CipherKey cipherKey) throws PostcardNonRetriableException {
		
		if(cipherData == null || ArrayUtils.isEmpty(cipherData) || cipherKey == null) {
			throw new EmptyCipherInputsException("Empty CipherData or CipherKey for encryption..");
		}
		
		byte[] dataToDecrypt = cipherData;
		PublicKey privateKey = ((RSACipherKey) cipherKey).getPublicKey();
		byte[] result = null;
		try {
			Cipher aesCipher = Cipher.getInstance(RSA_ALGORITHM_NAME, BOUNCY_CASTLE_PROVIDER_NAME);
			aesCipher.init(Cipher.ENCRYPT_MODE, privateKey);
			result = aesCipher.doFinal(dataToDecrypt);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new PostcardEncryptFailureException("Exception occured while key encryption",e);
		}
		return result;
	}

	@Override
	public byte[] decrypt(byte[] content, byte[] key, byte[] iv) throws PostcardNonRetriableException {
		byte[] decryptedBytes = null;
		Cipher cipher;
		try {
			validateInputs(content, key, iv);
			
			cipher = Cipher.getInstance(AES_CBC_PK7PADDING_TRANSFORMATION_NAME, BOUNCY_CASTLE_PROVIDER_NAME);
			SecretKeySpec skeySpec = new SecretKeySpec(key, AES_ALGORITHM_NAME);
			
			if(!ArrayUtils.isEmpty(iv))
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
			else
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			
			decryptedBytes = cipher.doFinal(content);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw new PostcardDecryptFailureException("Exception while decrypting message content",e);
		}
		return decryptedBytes;
	}

	@Override
	public byte[] encrypt(byte[] content, byte[] key, byte[] iv) throws PostcardNonRetriableException {
		Cipher cipher;
		byte[] encryptedBytes = null;
		try {
			validateInputs(content, key, iv);
			
			cipher = Cipher.getInstance(AES_CBC_PK7PADDING_TRANSFORMATION_NAME, BOUNCY_CASTLE_PROVIDER_NAME);
			SecretKeySpec skeySpec = new SecretKeySpec(key, AES_ALGORITHM_NAME);
			
			if(!ArrayUtils.isEmpty(iv))
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
			else
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			
			encryptedBytes = cipher.doFinal(content);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw new PostcardEncryptFailureException("Exception while encrypting message content",e);
		}
		return encryptedBytes;
	}
	
	private void validateInputs(byte[] content, byte[] key, byte[] iv) throws PostcardNonRetriableException {
		if(ArrayUtils.isEmpty(content) || ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(iv))
			throw new EmptyCipherInputsException("Empty Cipher Content or Keys...");
	}

}
