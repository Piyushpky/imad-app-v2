package com.hp.wpp.ssn;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.ssn.dao.PrinterRegistrationDomainDAO;
import com.hp.wpp.ssn.dao.SignedSerialNumberDAO;
import com.hp.wpp.ssn.entities.SSNEntity;
import com.hp.wpp.ssn.exception.InvalidSSNCounterException;
import com.hp.wpp.ssn.exception.InvalidSSNException;
import com.hp.wpp.ssn.exception.InvalidSSNSignatureException;
import com.hp.wpp.ssn.impl.SSNValidatorHelper;
import com.hp.wpp.ssn.impl.SignedSerialNumberValidatorImpl;
import com.hp.wpp.ssn.util.Utility;

public class SignedSerialNumberValidatorTest {

	private SignedSerialNumberValidator signedSerialNumber;
	private SignedSerialNumberValidatorImpl signedSerialNumberImpl;

	@Mock
	PrinterRegistrationDomainDAO registrationDomainDao;

	@Mock
	SignedSerialNumberDAO ssnDao;

	String serialNumber = "ABCDEFGHIJ";
	String domainRegKey = "ABCDEFGHIJ";
	int issuanceCounter = 10;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		signedSerialNumberImpl = new SignedSerialNumberValidatorImpl();
		signedSerialNumberImpl.setRegistrationDomainDao(registrationDomainDao);
		signedSerialNumberImpl.setSsnDao(ssnDao);

		signedSerialNumber = (SignedSerialNumberValidator) signedSerialNumberImpl;
		doReturn(domainRegKey).when(registrationDomainDao)
				.getPrinterRegistrationDomainKey(anyInt());

	}

	@DataProvider(name = "validSSN")
	public Object[][] dataProviderForValidSSN() {
		return new Object[][] {
				{ "YHBQHAP4-ABCDEFGHIJ"},
				{ "2HBTZPU6-ABCDEFGHIJ"},
				{ "2HBTZPU6 ABCDEFGHIJ" },
				{"YHBQHAP4ABCDEFGHIJ"},
				{ "YHB-QHAP4-ABCDEF GHIJ"},
				{ "2HBT ZPU6-ABCDEFG-HIJ"},
				{ "2HBTZPU6 ABCDEF-GHIJ" },
				{ "2HBTZPU6_ABCDEF_GHIJ" }
			};
	}

	@Test(dataProvider = "validSSN")
	public void testSSNSignatureValid(String validSSN) throws Exception {

		doReturn(null).when(ssnDao).getSSN(Mockito.anyString(),Mockito.anyInt());
		signedSerialNumber.validateAndPersistSSN(validSSN);
		verify(ssnDao, times(1)).createSSN((SSNEntity)Mockito.anyObject());

	}


	@DataProvider(name = "validExceptions")
	public Object[][] dataProviderForOnlyValidate() {
		return new Object[][] {
				// {new InvalidSSNCounterException()},

				{ "IFBEGMZS-ABCDEFGHIJ", new InvalidSSNSignatureException() }


		};
	}
	@Test(dataProvider = "validExceptions")
    public void testValidateOnlySSN(String inputSSN,Exception expectedException) throws Exception {
		try {
			signedSerialNumberImpl.validateSSN(inputSSN);;
		} catch (Exception actualException) {
			Assert.assertEquals(actualException.getClass().getName(),
					expectedException.getClass().getName());
		}

	}

	@DataProvider(name = "validSSNWithSerialNumber")
	public Object[][] dataalidateSSNwithSerialNumber() {
		return new Object[][] {
				{ "YHBQHAP4-ABCDEFGHIJ", serialNumber },
				{ "2HBTZPU6-ABCDEFGHIJ", serialNumber },
				{ "2HBTZPU6 ABCDEFGHIJ", serialNumber },
				{"YHBQHAP4ABCDEFGHIJ", serialNumber}
			};
	}
	@Test(dataProvider = "validSSNWithSerialNumber")
	public void testValidateSSNwithSerialNumber(String validSSN, String serialNumber) throws Exception {

		doReturn(null).when(ssnDao).getSSN(Mockito.anyString(),Mockito.anyInt());
		signedSerialNumber.validateAndPersistSSN(validSSN,serialNumber);
		verify(ssnDao, times(1)).createSSN((SSNEntity)Mockito.anyObject());

	}

	@DataProvider(name = "validSSNWithInvalidSerialNumber")
	public Object[][] dataalidateSSNwithInvalidSerialNumber() {
		return new Object[][] {
				{ "YHBQHAP4-ABCDEFGHIJ", "ABCDEFGHIA" },
				{ "2HBTZPU6-ABCDEFGHIJ", "ABCDEFGHIE" },
				{ "2HBTZPU6 ABCDEFGHIJ", "ABCDEFGHIA"  },
				{"YHBQHAP4ABCDEFGHIJ", "ABCDEFGHIA"}
			};
	}
	@Test(dataProvider = "validSSNWithInvalidSerialNumber", expectedExceptions = InvalidSSNException.class)
	public void testValidateSSNwithInvalidSerialNumber(String validSSN, String serialNumber) throws Exception {

		doReturn(null).when(ssnDao).getSSN(Mockito.anyString(),Mockito.anyInt());
		signedSerialNumber.validateAndPersistSSN(validSSN,serialNumber);
		verify(ssnDao, times(1)).createSSN((SSNEntity)Mockito.anyObject());

	}


	@DataProvider(name = "validateSignatureExceptions")
	public Object[][] dataProviderForSSNExceptions() {
		return new Object[][] {
				// {new InvalidSSNCounterException()},
				{ "YHBQHAP4-ABCDEFG", new InvalidSSNException() },
				{ "2HBTZPU6KLDFSJLJLFSD-ABCDEFGHIJALLLL",
						new InvalidSSNException() },
				{ "IFBEGMZS-ABCDEFGHIJ", new InvalidSSNSignatureException() },
				{ " ", new InvalidSSNException() },
				{ null, new InvalidSSNException() },
				{ "YH9QHZP4-ABCDEFGHIJ", new InvalidSSNException() },
				{"YHBQHAP4ABCDEFGHIJFGH", new InvalidSSNException()}

		};
	}



	@Test(dataProvider = "validateSignatureExceptions")
	public void testSSNSignatureWithException(String inputSSN, Exception expectedException) throws Exception {

		try {
			signedSerialNumber.validateAndPersistSSN(inputSSN);
		} catch (Exception actualException) {
			Assert.assertEquals(actualException.getClass().getName(),
					expectedException.getClass().getName());
		}

	}

	@Test
	public void validateSerialNumber() throws InvalidSSNException{
		whenDBReturnsSSNValue().executeVerifyAndStoredSSN(11).verifyUpdateSSNInvocation();
	}

	private SignedSerialNumberValidatorTest whenDBReturnsSSNValue() {
		SSNEntity existingEntity = new SSNEntity();
		existingEntity.setIssuanceCounter(10);
		existingEntity.setOverrunBit(false);
		when(ssnDao.getSSN(Mockito.anyString(),Mockito.anyInt())).thenReturn(existingEntity);
		return this;

	}

	@Test
	public void testcreateSSNEntity() throws Exception {
		String ssn = "3EARXAKR-"+serialNumber;
		SSNEntity ssnEntity = SSNValidatorHelper.createSSNEntity(SSNValidatorHelper.parseSSN(ssn));
		String expectedVersion = "6";
		int domainIndex = 4;
		Assert.assertEquals(ssnEntity.getVersion(), expectedVersion);
		Assert.assertEquals(ssnEntity.getDomainIndex(), domainIndex);
		Assert.assertTrue(ssnEntity.getOverrunBit());
		Assert.assertTrue(ssnEntity.getInstantInkFlag());
		Assert.assertEquals(ssnEntity.getIssuanceCounter(), issuanceCounter);
		Assert.assertEquals(ssnEntity.getSerialNumber(), Utility.generateSHA256AndBase64(serialNumber.getBytes()));

	}

	@Test
	public void testSSNwithDB() throws Exception {

		whenSSNNotfoundInDB().executeVerifyAndStoredSSN(issuanceCounter).verifyCreateSSNInvocation();


	}

	public SignedSerialNumberValidatorTest verifyCreateSSNInvocation() {
		verify(ssnDao, times(1)).createSSN((SSNEntity)Mockito.anyObject());
		return this;
	}

	public SignedSerialNumberValidatorTest verifyUpdateSSNInvocation() {
		verify(ssnDao, times(1)).updateSSN((SSNEntity)Mockito.anyObject());
		return this;
	}

	public SignedSerialNumberValidatorTest executeVerifyAndStoredSSN(int issuanceCounter) throws InvalidSSNException {

		SSNEntity ssnEntity = new SSNEntity();
		ssnEntity.setIssuanceCounter(issuanceCounter);
		ssnEntity.setSerialNumber(serialNumber);
		ssnEntity.setOverrunBit(false);

		signedSerialNumberImpl.verifyWithStoredSSN(ssnEntity);
		return this;
	}



	public SignedSerialNumberValidatorTest whenSSNNotfoundInDB() {
		doReturn(null).when(ssnDao).getSSN(Mockito.anyString(),Mockito.anyInt());
		return this;
	}


	@DataProvider(name = "createSSNData")
	public Object[][] createSSNDataForDatabase() {
		return new Object[][] {
				{ 16 },
				{ 11 },
				{ 12 } };
	}

	@Test(dataProvider = "createSSNData")
	public void testSSNwithDBUpdates(int counter) throws Exception {

		whenDBReturnsSSNValue().executeVerifyAndStoredSSN(counter).verifyUpdateSSNInvocation();

	}

	@Test(expectedExceptions = InvalidSSNCounterException.class)
	public void testSSNCounterException() throws InvalidSSNException {

		SSNEntity existingEntity = new SSNEntity();
		existingEntity.setOverrunBit(false);
		existingEntity.setIssuanceCounter(11);

		when(ssnDao.getSSN(Mockito.anyString(),Mockito.anyInt())).thenReturn(existingEntity);

		executeVerifyAndStoredSSN(issuanceCounter);

		}


	@Test(expectedExceptions={InvalidSSNCounterException.class})
    public void testOverRunBitChangeFlow() throws Exception{
            SSNEntity dbSSN= new SSNEntity();
            dbSSN.setIssuanceCounter(1023);
            dbSSN.setOverrunBit(true);

            SSNEntity inputSSN = new SSNEntity();
            inputSSN.setIssuanceCounter(3);
            inputSSN.setOverrunBit(false);

            when(ssnDao.getSSN(Mockito.anyString(),Mockito.anyInt())).thenReturn(dbSSN);

            signedSerialNumberImpl.verifyWithStoredSSN(inputSSN);
            verify(ssnDao, times(1)).updateSSN(dbSSN);
    }
	
	@Test(expectedExceptions={InvalidSSNException.class})
	public void testLowerCaseCharacters() throws Exception{
		String inputSSN = "YHBQHAP4-abcdefghij";
		signedSerialNumber.validateAndPersistSSN(inputSSN);
		
	}
	
	

}







	// Below test cases functions are not yet implemented
	/*
	 * //@Test public void testSSNAgianstSerialNumber() throws Exception{
	 *
	 * Mockito.doReturn(true).when(signedSerialNumber).is_SSN_Valid(SSN,
	 * SERIAL_NUMBER);
	 *
	 * Assert.assertTrue(signedSerialNumber.is_SSN_Valid(SSN,SERIAL_NUMBER));
	 *
	 * }
	 *
	 * //@Test public void testSSNAgianstSerialNumberWithException() throws
	 * Exception{ Exception expectedException = new
	 * InvalidSSNSignatureException();
	 * Mockito.doThrow(expectedException).when(signedSerialNumber
	 * ).is_SSN_Valid(SSN,SERIAL_NUMBER);
	 *
	 * try{ signedSerialNumber.is_SSN_Valid(SSN,SERIAL_NUMBER); }catch(Exception
	 * actualException){
	 * Assert.assertEquals(actualException.getClass().getName(),
	 * expectedException.getClass().getName()); } }
	 */

