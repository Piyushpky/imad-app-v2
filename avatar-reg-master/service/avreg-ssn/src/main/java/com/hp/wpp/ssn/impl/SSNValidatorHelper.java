package com.hp.wpp.ssn.impl;

import com.hp.wpp.ssn.data.SSNFields;
import com.hp.wpp.ssn.data.SSNValidatorData;
import com.hp.wpp.ssn.entities.SSNEntity;
import com.hp.wpp.ssn.exception.InvalidSSNException;
import com.hp.wpp.ssn.util.Utility;

public class SSNValidatorHelper {

	
	
	private static final String upperCaseRegEx = "[A-Z0-9]*";

	public static SSNValidatorData parseSSN(String ssn) throws InvalidSSNException {

		if (ssn == null || ssn.trim().length() == 0) {
			throw new InvalidSSNException("ssn null or empty");
		}
		ssn = ssn.replaceAll("-| |_", ""); 
		String[] splitSSN=new String[2];
			if(ssn.length() != 18)
			{
				throw new InvalidSSNException("ssn length not as expected");	
			}
			splitSSN[0] = ssn.substring(0, 8);
			splitSSN[1] =ssn.substring(8, ssn.length());
			if(!splitSSN[1].matches(upperCaseRegEx))
			{
			throw new InvalidSSNException("ssn contains lower case characters");
			}
		SSNValidatorData validatorData=new SSNValidatorData();
		validatorData.setHeader(Utility.decodeBase32(splitSSN[0]));
		validatorData.setSerialNumber(splitSSN[1]);
		return validatorData;
	}
	
	
	public static SSNEntity createSSNEntity(SSNValidatorData validatorData) {
		SSNEntity ssnEntity = new SSNEntity();
		ssnEntity.setSerialNumber(validatorData.getSerialNumber());
		SSNFields.VERSION.populate(validatorData.getHeader(), ssnEntity);
		SSNFields.REGISTRATION_DOMAIN_INDEX.populate(validatorData.getHeader(), ssnEntity);
		SSNFields.OVERRUN_BIT.populate(validatorData.getHeader(), ssnEntity);
		SSNFields.ISSUANCE_COUNTER.populate(validatorData.getHeader(), ssnEntity);
		SSNFields.INSTANT_INK_BIT.populate(validatorData.getHeader(), ssnEntity);
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
