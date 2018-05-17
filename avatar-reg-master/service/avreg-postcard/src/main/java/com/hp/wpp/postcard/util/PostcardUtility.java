/**
 * 
 */
package com.hp.wpp.postcard.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardDomain;

/**
 * @author mahammad
 *
 */
public class PostcardUtility {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardUtility.class);
	
	public static byte[] generateRandomBytes(int size) {
		byte[] r = new byte[size];
		SecureRandom random = new SecureRandom();
		random.nextBytes(r);
		return r;
	}
	
	public static byte[] generateSHA256Hash(byte[] content, int size) throws PostcardNonRetriableException {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(PostcardConstants.SHA256_ALGORITHM_NAME);
			hash = md.digest(content);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyCertificateException("Exception occured while generating hash",e);
		}
		//// copy the hash of specified length.. ASK is of 16 bytes  
		if(size > 0) 
			return Arrays.copyOf(hash, size);
		else
			return hash;
	}
	
	public static String returnFormattedText(byte[] b) {
		String encodingType = PostcardConstants.LOG_ENCODING_TYPE;
		switch (encodingType) {
			case PostcardConstants.LOG_ENCODING_TYPE:
				return Hex.encodeHexString(b);
			default:
				return Base64.encodeBase64String(b);
		}
	}
	
	/**
	 * canonize the json string
	 * replaceAll("[\":,\\r\\n\\t {}\\[\\]\\']", "")
	 * 
	 * @param signedInfo
	 * @return
	 */
	public static String canonize(String input) {
		return input.replaceAll("[\":,\\r\\n\\t {}\\[\\]\\']", "");
	}
	
	public static Date parsePostcardDate(String timestamp) {
		LOG.debug("timestamp received in postcard: {}", timestamp);
		Date date = new Date();
		if(StringUtils.isBlank(timestamp)) {
			LOG.debug("Empty timestamp received in postcard, returning current date.");
			return date;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(PostcardConstants.POSTCARD_DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone(PostcardConstants.UNIVERSAL_TIME_ZONE));
		try {
			date = sdf.parse(timestamp);
		} catch (ParseException e) {
			LOG.debug("ParseException while parsing postcard timestamp, returning current date: {}", e.getMessage());
		}
		return date;
	}
	
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(PostcardConstants.POSTCARD_DATE_FORMAT);
		return sdf.format(date);
	}
	
	public static boolean isSupportedDomain(PostcardDomain postcardDomain) {
		if (postcardDomain == PostcardDomain.certificate_entity || postcardDomain == PostcardDomain.certificate_model) {
			return true;
		}
		return false;
	}
}
