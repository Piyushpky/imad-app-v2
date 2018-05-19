package com.hp.wpp.ssnc.common.util;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.exception.DeviceClaimRetriableException;
import com.hp.wpp.ssnclaim.exception.SSNNonRetriableException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;


public class Utility {
	private Utility(){};
	private static final String SHA256_ALGORITHM_NAME = "SHA-256";
	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final WPPLogger logger = WPPLoggerFactory
			.getLogger(Utility.class);

	/**
	 * Method to decode using base32
	 * @param header
	 * @return decoded
	 * @throws DeviceClaimRetriableException 
	 */

	public static byte[] decodeBase32(String header)  {
		Base32 decodedOutput = new Base32();
		byte[] decodedData = (byte[]) decodedOutput.decode(header);

		if (decodedData.length != 5) {
			logger.error("Decoded Length should be 5 but the length is :" +decodedData.length);
			throw new DeviceClaimRetriableException("signature header length is not matching");
		}
		return decodedData;
	}
	
	public static byte[] generateSHA256(byte[] content, byte[] key) {
		String hMacAlgorithm = HMAC_SHA256;
		byte[] messageSignatr = null;
		SecretKeySpec signingKey = new SecretKeySpec(key, hMacAlgorithm);
		Mac mac;
		try {
			mac = Mac.getInstance(hMacAlgorithm);
			mac.init(signingKey);
			messageSignatr = mac.doFinal(content);
		} catch (Exception e) {
			logger.error("Exception while generating HMAC signature generation: "+e.getMessage());
			throw new SSNNonRetriableException("Generate HMAC Signature failed");
		}		
		return messageSignatr;
	}
	/**
	 * 
	 * returns the base64 encode value of hash256 serialnumber.
	 * 
	 * @param content
	 * @return
	 */
	public static String generateSHA256AndBase64(byte[] content) {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
			hash = md.digest(content);
		} catch (Exception e) {
			logger.error("Exception while generating SHA-256 hash: "+ e);
			throw new SSNNonRetriableException("Generate SHA256 failed");
		}

		return new String(Base64.encodeBase64(hash));
	}
}
