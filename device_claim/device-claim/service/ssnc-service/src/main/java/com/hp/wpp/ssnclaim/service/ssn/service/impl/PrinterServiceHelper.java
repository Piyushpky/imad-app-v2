package com.hp.wpp.ssnclaim.service.ssn.service.impl;

import com.hp.wpp.ssnc.common.util.Utility;
import com.hp.wpp.ssnclaim.exception.DeviceClaimRetriableException;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFieldsEnum;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNValidatorData;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;


public class PrinterServiceHelper {

	private static final String upperCaseRegEx = "[A-Z0-9]*";
	private static final String SECRETKEY = "morpheus";
	public static SSNValidatorData parseSSN(String ssn)  {
		if (ssn == null || ssn.trim().length() == 0) {
			throw new DeviceClaimRetriableException("ssn null or empty");
		}
		String[] splitSSN=new String[2];
			if(ssn.length() != 18)
			{
				throw new DeviceClaimRetriableException("ssn length not as expected");	
			}
			splitSSN[0] = ssn.substring(0, 8);
			splitSSN[1] =ssn.substring(8, ssn.length());
			if(!splitSSN[1].matches(upperCaseRegEx))
			{
			throw new DeviceClaimRetriableException("ssn contains lower case characters");
			}
		SSNValidatorData validatorData=new SSNValidatorData();
		validatorData.setHeader(Utility.decodeBase32(splitSSN[0]));
		validatorData.setSerialNumber(splitSSN[1]);
		return validatorData;
	}
	
	
	 public String createSNKey(String serialNumber,int domainIndex) {
	        String ssnWithDomainIndex = serialNumber + domainIndex;
	        byte[] ssnWithDomainIndexinBytes = ssnWithDomainIndex.getBytes(Charset.forName("UTF-8"));
	        byte[] keyInBytes = SECRETKEY.getBytes(Charset.forName("UTF-8"));
	        byte[] snKey = Utility.generateSHA256(ssnWithDomainIndexinBytes, keyInBytes);
	        return new String(Base64.encodeBase64URLSafe(snKey));
	    }
	
	public static SSNFields createSSNFields(SSNValidatorData validatorData) {
		SSNFields ssnEntity = new SSNFields();
		ssnEntity.setSerialNumber(validatorData.getSerialNumber());
		SSNFieldsEnum.VERSION.populate(validatorData.getHeader(), ssnEntity);
		SSNFieldsEnum.REGISTRATION_DOMAIN_INDEX.populate(validatorData.getHeader(), ssnEntity);
		SSNFieldsEnum.OVERRUN_BIT.populate(validatorData.getHeader(), ssnEntity);
		SSNFieldsEnum.ISSUANCE_COUNTER.populate(validatorData.getHeader(), ssnEntity);
		SSNFieldsEnum.INSTANT_INK_BIT.populate(validatorData.getHeader(), ssnEntity);
		
		validatorData.setDomainIndex(ssnEntity.getDomainIndex());
		return ssnEntity;
	}
	public static byte[] generateSignatureFragments(SSNValidatorData validatorData, byte[] domainkey) {
		byte[] replacedFragments = replaceSigFragmentsWithZeros(validatorData.getHeader());
		byte[] ssnWithZeros = createHeader(replacedFragments, validatorData.getSerialNumber().getBytes());
		byte[] messageSignature = Utility.generateSHA256(ssnWithZeros, domainkey);
		return getActualFragments(messageSignature);
	}
	public  static byte[] replaceSigFragmentsWithZeros(byte[] decodedFromBase32) {
		byte[] copyOfHeader = new byte[5];
		System.arraycopy(decodedFromBase32, 0, copyOfHeader, 0, decodedFromBase32.length);
		copyOfHeader[0] = (byte) (decodedFromBase32[0] & 0xE0);
		copyOfHeader[2] = (byte) (decodedFromBase32[2] & 0xC0);
		copyOfHeader[4] = (byte) (decodedFromBase32[4] & 0xF0);
		return copyOfHeader;
	}
	public static byte[] createHeader(byte[] signature, byte[] serialNumber) {
		byte[] result = new byte[signature.length + serialNumber.length];
		System.arraycopy(signature, 0, result, 0, signature.length);
		System.arraycopy(serialNumber, 0, result, signature.length, serialNumber.length);
		return result;
	}
	public static byte[] getActualFragments(byte[] decodeHmac) {

		return new byte[] { 
				(byte) (decodeHmac[0] & 0x1F),
				(byte) (decodeHmac[1] & 0x3F), 
				(byte) (decodeHmac[2] & 0x0F) 
				};
	}
	// Method to extract the expected_fragments
	public static byte[] getExpectedFragments(byte[] header) {
			return new byte[] {
					(byte) (header[0] & 0x1F),
					(byte) (header[2] & 0x3F),
					(byte) (header[4] & 0x0F) 
					};
		}
}
