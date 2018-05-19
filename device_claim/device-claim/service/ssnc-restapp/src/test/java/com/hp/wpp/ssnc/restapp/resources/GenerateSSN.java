package com.hp.wpp.ssnc.restapp.resources;

/**
 * @author dream
 *
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class GenerateSSN {

	private static final int ISSUANCE_COUNT =215;

	private static final String HMAC_SHA256 = "HmacSHA256";

	private static final String key = "testDomainKey";
	
	private static final int domainIndex=1;

	public static void main(String args[]) throws Exception, Exception {

		//generateSSN(6 , 780 , 500);
		
		generateSSN(1 , domainIndex , true, ISSUANCE_COUNT, true);
	
	}

	private static void generateSSN(int version, int domainIndex,boolean overrun, int issuanceCount,boolean ink) {
		byte[] ssn = new byte[5];

		ssn[0] = createVersionFragment(version, 0);
		createDIFragment(domainIndex, 0, ssn);
		createIssuanceCounterFragement(overrun, issuanceCount, ink, (byte) 0, ssn);

		String serialNumber = "SA15092560";
		
		
		byte[] sn = serialNumber.getBytes();
		byte[] Finalresult = createMergedBytes(ssn, sn);
		// System.out.println("" + result.length);
		byte[] generateKey = key.getBytes();
		byte[] hmacResult = generateMessageSignature(Finalresult, generateKey);

		ssn[0] = (byte) (ssn[0] | (hmacResult[0] & 0x1F));
		ssn[2] = (byte) (ssn[2] | (hmacResult[1] & 0x3F));
		ssn[4] = (byte) (ssn[4] | (hmacResult[2] & 0x0F));
		
        Base32 encodedOutput = new Base32();
        String encodedData =  encodedOutput.encodeAsString(ssn);
       
        System.out.println("Encode as String " + encodedOutput.encodeAsString(ssn));
       encodedData = encodedData + "-" + serialNumber;
        
       System.out.println("SSN is :" + encodedData);
		//display(ssn);
		
       String splitResult = split_SSN(encodedData);
       System.out.println("" + splitResult);
       byte[] outPut = decode_Base32(splitResult);
      
       byte[] expected_fragments = getExpectedFragments(outPut);
       
       
       byte[] replacedFrag = replaceSigFragments(outPut);
       
       byte[] mergedWithSerialNumber = createMergedBytes(replacedFrag,sn);
      System.out.println(" " + mergedWithSerialNumber.length);
       
       
       byte[] decodedHmac = generateMessageSignature(mergedWithSerialNumber,generateKey);
       
       
       
       byte[] actualFrag = getActualFragments(decodedHmac);
       
       
       
       
       
       if (Arrays.equals(actualFrag,expected_fragments))
       {
    	   System.out.println("Awesome");
       }
       else 
       {
    	   System.out.println(" Cobras ");
       }
	}
	
	private static byte[] getExpectedFragments(byte[] base32Decoded)
	{
		   
		 byte[] expected_fragments = new byte[] {(byte) (base32Decoded[0] & 0x1F),(byte) (base32Decoded[2] & 0x3F) ,(byte) (base32Decoded[4] & 0x0F)};  
		 return expected_fragments;
	}
		
	private static byte[] getActualFragments(byte[] decodeHmac)
	{
		   
		 byte[] actualFrag = new byte[] {(byte) (decodeHmac[0] & 0x1F),(byte) (decodeHmac[1] & 0x3F),(byte) (decodeHmac[2] & 0x0F)};  
		 return actualFrag;
	}

	   
	   private static byte[] replaceSigFragments(byte[] decodedFromBase32)
	   {
		   decodedFromBase32[0] = (byte) (decodedFromBase32[0] & 0xE0);
		   decodedFromBase32[2] = (byte) (decodedFromBase32[2] & 0xC0); 
		   decodedFromBase32[4] = (byte) (decodedFromBase32[4] & 0xF0);
		   return decodedFromBase32;
		   
	   }
	
	private static String split_SSN(String input) {
		String[] temp;

		temp = input.split("\\s+|-");

		return temp[0];

	}

	private static byte[] decode_Base32(String input) {
		Base32 decodedOutput = new Base32();
		byte[] decodedData = decodedOutput.decode(input);
		return decodedData;

	}
	public static byte[] generateMessageSignature(byte[] content, byte[] key) {
		String hMacAlgorithm = HMAC_SHA256;
		byte[] messageSignatr = null;
		SecretKeySpec signingKey = new SecretKeySpec(key, hMacAlgorithm);
		Mac mac;
		try {
			mac = Mac.getInstance(hMacAlgorithm);
			mac.init(signingKey);
			messageSignatr = mac.doFinal(content);
		} catch (NoSuchAlgorithmException e) {

		} catch (InvalidKeyException e) {

		}
		return messageSignatr;
	}
	
	public static String encode(byte[] input)
	{
		return null;
		
	}

	public static byte[] createMergedBytes(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static byte[] createFragments(byte[] hmacResult) {

		hmacResult[0] = (byte) (hmacResult[0] & 0x1F);
		hmacResult[1] = (byte) (hmacResult[1] & 0x3F);
		hmacResult[2] = (byte) (hmacResult[2] & 0x0F);

		return hmacResult;

	}

	public static byte createVersionFragment(int v, int frag1)

	{

		int header = 0;
		Byte output = (byte) ((v << 5 | header) & 0xff);

		return output;

	}

	public static void createDIFragment(int di, int frag2, byte[] ssn) {
		int temp = di << 6;
		ssn[1] = (byte) (temp >> 8 & 0xFF);
		ssn[2] = (byte) ((temp | frag2) & 0xFF);

	}

	public static void createIssuanceCounterFragement(boolean overrun,
			int count, boolean ink, byte frag3, byte[] ssn) {

		byte run = (byte) ((overrun) ? 1 : 0 & 0xFF);
		run = (byte) (run << 7);
		int count_result = count << 5;
		byte[] result = new byte[] { (byte) ((count_result >> 8) & 0xff),
				(byte) ((count_result) & 0xff) };
		ssn[3] = (byte) (result[0] | run);

		byte ink_bit = (byte) ((ink) ? 1 : 0 & 0xFF);
		ink_bit = (byte) (ink_bit << 4);
		frag3 = 0;
		ink_bit = (byte) (ink_bit | frag3);
		ssn[4] = (byte) (result[1] | ink_bit);

	}

	

	public static void display(byte[] input) {

		for (int b = (input.length - 1); b >= 0; b--) {
			System.out.println(" ");
			for (int i = 0; i <= 7; i++) {
				System.out.print(input[b] & 1);
				input[b] = (byte) (input[b] >> 1);
			}
		}
	}

}
