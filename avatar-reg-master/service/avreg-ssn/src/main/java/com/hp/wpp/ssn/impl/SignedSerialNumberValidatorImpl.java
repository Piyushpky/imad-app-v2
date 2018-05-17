package com.hp.wpp.ssn.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssn.SignedSerialNumberValidator;
import com.hp.wpp.ssn.dao.PrinterRegistrationDomainDAO;
import com.hp.wpp.ssn.dao.SignedSerialNumberDAO;
import com.hp.wpp.ssn.data.SSNValidatorData;
import com.hp.wpp.ssn.entities.SSNEntity;
import com.hp.wpp.ssn.exception.InvalidSSNCounterException;
import com.hp.wpp.ssn.exception.InvalidSSNException;
import com.hp.wpp.ssn.exception.InvalidSSNSignatureException;

public class SignedSerialNumberValidatorImpl implements
		SignedSerialNumberValidator {

	@Autowired
	private SignedSerialNumberDAO ssnDao;

	@Autowired
	private PrinterRegistrationDomainDAO registrationDomainDao;

	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(SignedSerialNumberValidatorImpl.class);

	public void setSsnDao(SignedSerialNumberDAO ssnDao) {
		this.ssnDao = ssnDao;
	}

	public void setRegistrationDomainDao(
			PrinterRegistrationDomainDAO registrationDomainDao) {
		this.registrationDomainDao = registrationDomainDao;
	}

	@Override
	public void validateAndPersistSSN(String ssn) throws InvalidSSNException {
		// Step-1 : validate signature fragments from ssn
		SSNValidatorData validatorData = SSNValidatorHelper.parseSSN(ssn);
		validateSSN(validatorData);
		// Step-2: validate ssn against stored SSN values.
		validateWithStoredSSN(validatorData);
		LOG.debug("SSN Validation Successful " + ssn);
		// System.out.println("INFO: SSN Validation Successful" + ssn);
	}

	@Override
	public void validateAndPersistSSN(String ssn, String serialNumber) throws InvalidSSNException {
		SSNValidatorData validatorData = SSNValidatorHelper.parseSSN(ssn);
		// step-1: validate serialNumber against SSN.
		if (!(validatorData.getSerialNumber().equals(serialNumber)))
			throw new InvalidSSNException("ssn and serial number not matching.");
		// Step-2 : validate signature fragments from ssn.
		validateSSN(validatorData);
		// Step-3: validate ssn against stored SSN values.
		validateWithStoredSSN(validatorData);
	}

	@Override
	public void validateSSN(String ssn) throws InvalidSSNException {
		SSNValidatorData validatorData = SSNValidatorHelper.parseSSN(ssn);
		validateSSN(validatorData);
	}


	private void validateSSN(SSNValidatorData validatorData) throws InvalidSSNSignatureException {
		byte[] actualSignatureFragments = SSNValidatorHelper
				.generateSignatureFragments(
						validatorData,
						getRegistrationDomainKey(validatorData.getDomainIndex()));
		byte[] expectedFragments = SSNValidatorHelper
				.getExpectedFragments(validatorData.getHeader());

		if (!Arrays.equals(expectedFragments, actualSignatureFragments)) {
			throw new InvalidSSNSignatureException("ssn signature validation failed");
		}
	}

	private void validateWithStoredSSN(SSNValidatorData validatorData) throws InvalidSSNException {
		SSNEntity inputSSN = SSNValidatorHelper.createSSNEntity(validatorData);
		verifyWithStoredSSN(inputSSN);
	}

	public void verifyWithStoredSSN(SSNEntity inputSSN) throws InvalidSSNCounterException {
		SSNEntity storedSSN = ssnDao.getSSN(inputSSN.getSerialNumber(),inputSSN.getDomainIndex());
		if (storedSSN == null) {
			ssnDao.createSSN(inputSSN);
			return;
		}

		verifyIssuanceCounter(inputSSN, storedSSN);
		if(storedSSN.getOverrunBit() == true && inputSSN.getOverrunBit() == false)
		{
			throw new InvalidSSNCounterException("Invalid ssn overrun-bit values exception.Overrun bit flipped to zero, serial number =" +inputSSN.getSerialNumber());
		}
		storedSSN.setOverrunBit(inputSSN.getOverrunBit());
		storedSSN.setIssuanceCounter(inputSSN.getIssuanceCounter());
		ssnDao.updateSSN(storedSSN);
	}

	private void verifyIssuanceCounter(SSNEntity inputSSN, SSNEntity ssn) throws InvalidSSNCounterException {
		if ((ssn.getOverrunBit()==inputSSN.getOverrunBit()) && (ssn.getIssuanceCounter() > inputSSN.getIssuanceCounter())) {
			throw new InvalidSSNCounterException("Invalid input ssn counter values");
		}
	}

	private byte[] getRegistrationDomainKey(int domainIndex) {
		String domainRegKey = registrationDomainDao
				.getPrinterRegistrationDomainKey(domainIndex);
		return domainRegKey.getBytes();

	}

}
