package com.hp.wpp.ssnclaim.service.printercode.impl;

import com.hp.wpp.ssnc.common.util.PrinterCodeBase32;
import com.hp.wpp.ssnc.common.util.Utility;
import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeException;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeFields;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeValidatorData;

public class PrinterCodeValidatorHelper {
		private PrinterCodeValidatorHelper(){};
	private static final String upperCaseRegEx = "[A-Z0-9]*";

	// Masks are defined in a 2D array
	private static final byte[][] MASKS = new byte[][] {
			{ 0x15, 0x60, (byte) 0x98, 0x00 },
			{ 0x1e, (byte) 0x88, 0x10, 0x00 }, { 0x0b, 0x70, 0x30, 0x00 },
			{ 0x18, 0x08, 0x38, 0x00 }, { 0x03, (byte) 0x90, 0x18, 0x00 },
			{ 0x04, 0x38, (byte) 0xc8, 0x00 },
			{ 0x13, (byte) 0xe0, 0x00, 0x00 },
			{ 0x0d, 0x60, (byte) 0xe0, 0x00 }

	};

	public static PrinterCodeValidatorData parsePrinterCode(String printerCode) {

		if (printerCode == null || printerCode.trim().length() == 0) {
			throw new InvalidPrinterCodeException("printer code null or empty");
		}
		String[] splitPrinterCode = new String[2];
		if (printerCode.length() != 16) {
			throw new InvalidPrinterCodeException(
					"printer code length not as expected");
		}
		splitPrinterCode[0] = printerCode.substring(0, 6);
		splitPrinterCode[1] = printerCode.substring(6, printerCode.length());
		if (!splitPrinterCode[1].matches(upperCaseRegEx)) {
			throw new InvalidPrinterCodeException(
					"printer code contains lower case characters");
		}
		PrinterCodeValidatorData validatorData = new PrinterCodeValidatorData();
		validatorData.setHeader(PrinterCodeBase32.decode(splitPrinterCode[0]));
		validatorData.setSerialNumber(splitPrinterCode[1]);
		return validatorData;
	}

	public static PrinterCodeData createPrinterCodeData(
			PrinterCodeValidatorData validatorData) {
		PrinterCodeData printerCodedata = new PrinterCodeData();
		printerCodedata.setSerialNumber(validatorData.getSerialNumber());
		PrinterCodeFields.VERSION.extract(validatorData.getHeader(),
				printerCodedata);
		PrinterCodeFields.REGISTRATION_DOMAIN_INDEX.extract(
				validatorData.getHeader(), printerCodedata);
		PrinterCodeFields.OWNERSHIP_COUNTER.extract(validatorData.getHeader(),
				printerCodedata);
		PrinterCodeFields.OVERRUN_BIT.extract(validatorData.getHeader(),
				printerCodedata);
		PrinterCodeFields.ISSUANCE_COUNTER.extract(validatorData.getHeader(),
				printerCodedata);
		PrinterCodeFields.INSTANT_INK_BIT.extract(validatorData.getHeader(),
				printerCodedata);
		return printerCodedata;
	}

	public static byte[] generateSignatureFragments(
			PrinterCodeValidatorData validatorData, byte[] domainkey) {
		byte[] replacedFragments = replaceSigFragmentsWithZeros(validatorData
				.getHeader());
		byte[] printercodeWithZeros = createHeader(replacedFragments,
				validatorData.getSerialNumber().getBytes());
		byte[] messageSignature = Utility.generateSHA256(printercodeWithZeros,
				domainkey);
		return getActualFragments(messageSignature);
	}

	public static byte[] replaceSigFragmentsWithZeros(byte[] decodedFromBase32) {
		byte[] copyOfHeader = new byte[4];
		System.arraycopy(decodedFromBase32, 0, copyOfHeader, 0,
				decodedFromBase32.length);
		copyOfHeader[1] = (byte) ((decodedFromBase32[1] & 0xFF) & 0xF8);
		copyOfHeader[3] = (byte) ((decodedFromBase32[3] & 0xFF) & 0x03);
		return copyOfHeader;
	}

	public static byte[] createHeader(byte[] signature, byte[] serialNumber) {
		byte[] result = new byte[signature.length + serialNumber.length];
		System.arraycopy(signature, 0, result, 0, signature.length);
		System.arraycopy(serialNumber, 0, result, signature.length,
				serialNumber.length);
		return result;
	}

	// Method to get the actual fragments
	public static byte[] getActualFragments(byte[] decodeHmac) {
		return new byte[] { (byte) ((decodeHmac[0] & 0xFF) & 0x07),
				(byte) ((decodeHmac[1] & 0xFF) & 0x3F), };
	}

	// Method to extract the expected_fragments
	public static byte[] getExpectedFragments(byte[] header) {
		return new byte[] { (byte) ((header[1] & 0xFF) & 0x07),
				(byte) (((header[3] & 0xFF) & 0xFC) >> 2), };
	}

	// Method to unmask the header based on 3 LSB of issuance count
	public static byte[] getUnmaskedHeader(byte[] header) {
		int count = header[2] & 0x07;
		byte[] mask = MASKS[count];
		header[0] ^= mask[0];
		header[1] ^= mask[1];
		header[2] ^= mask[2];
		header[3] ^= mask[3];
		return header;
	}
}