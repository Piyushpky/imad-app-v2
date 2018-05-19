package com.hp.wpp.ssnclaim.service.printercode.impl;

import java.util.Arrays;
import java.util.Map;

import com.hp.wpp.ssnc.common.util.CryptoHelper;
import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeSignatureException;
import com.hp.wpp.ssnclaim.service.printercode.PrinterCode;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeValidatorData;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.exception.CryptographyException;

public class PrinterCodeValidatorImpl implements PrinterCode {
	@Autowired
	public PrinterResourceServiceConfig printerResourceServiceConfig;
	

	public void setPrinterResourceServiceConfig(PrinterResourceServiceConfig printerResourceServiceConfig) {
		this.printerResourceServiceConfig = printerResourceServiceConfig;
	}
	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(PrinterCodeValidatorImpl.class);

	public PrinterCodeData validatePrinterCode(String printerCode,
											   Map<Integer, String> domainMap) {
		// Step-1 : validate signature fragments from printercode

		PrinterCodeValidatorData validatorData = PrinterCodeValidatorHelper
				.parsePrinterCode(printerCode);
		byte[] unmaskedHeader = PrinterCodeValidatorHelper
				.getUnmaskedHeader(validatorData.getHeader());
		validatorData.setHeader(unmaskedHeader);
		PrinterCodeData printerCodedata = PrinterCodeValidatorHelper
				.createPrinterCodeData(validatorData);
		printerCodedata.setPrinterCode(printerCode);
		validatorData.setDomainIndex(printerCodedata.getDomainIndex());
		validatePrinterCode(validatorData, domainMap);
		LOG.debug("Printer Code Validation Successful " + printerCode);
		
		return printerCodedata;
	}

	private void validatePrinterCode(PrinterCodeValidatorData validatorData,
			Map<Integer, String> domainMap) {
		byte[] domainkey = null;
		String decryptKey = printerResourceServiceConfig.getDomainKeyEncyptedKeyValue();
			LOG.debug("DomainKey is decoded for printercode ");
			try {
				if(domainMap.get(validatorData.getDomainIndex())==null ||domainMap.get(validatorData.getDomainIndex()).isEmpty())
					throw new  CryptographyException("domain index found null or invalid");

				domainkey = Base64.decodeBase64(CryptoHelper.aesDecryptBase64((domainMap.get(validatorData.getDomainIndex())), decryptKey));
			
		} catch (CryptographyException  e) {
			throw new InvalidPrinterCodeSignatureException(
					"Printer Code signature validation failed due to " + e.getMessage());
		}

		byte[] actualSignatureFragments = PrinterCodeValidatorHelper.generateSignatureFragments(validatorData,
				domainkey);
		byte[] expectedFragments = PrinterCodeValidatorHelper.getExpectedFragments(validatorData.getHeader());
		if (!Arrays.equals(expectedFragments, actualSignatureFragments)) {
			throw new InvalidPrinterCodeSignatureException("Printer Code signature validation failed");
		}
	}

}
