
package com.hp.wpp.ssnclaim.service.ssn.validator;

import java.util.ArrayList;
import java.util.List;

import com.hp.wpp.ssnc.common.enums.RequestType;
import com.hp.wpp.ssnclaim.exception.SNKeyNotFoundException;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.response.PrinterCodeResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.service.impl.DeviceLookUpServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;
import com.hp.wpp.ssnclaim.exception.DeviceClaimRetriableException;
import com.hp.wpp.ssnclaim.restmodel.json.schema.DeviceClaim;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;

public class DeviceLookUpServiceImplTest {

	@Mock
	PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;

	@Mock
	private RegistrationDomainDaoImpl registrationDomainService;

	@Mock
	private LinksDaoImpl linksService;

	@Mock
	private PrinterCodeResponseGenerator generatePrinterDataResponse;

	@Mock
	private PrinterInfo ssnClaimInfo;

	@InjectMocks
	private DeviceLookUpServiceImpl deviceLookUpServiceImpl;

	public List<RegistrationDomainEntity> domainList;

	@BeforeMethod
	public void setUpMocks() {
		MockitoAnnotations.initMocks(this);

		domainList = new ArrayList<RegistrationDomainEntity>();
		RegistrationDomainEntity registrationDomainEntity = new RegistrationDomainEntity();
		registrationDomainEntity.setDomainIndex(0);
		registrationDomainEntity.setDomainKey("domain_key1");
		domainList.add(registrationDomainEntity);

		registrationDomainEntity = new RegistrationDomainEntity();
		registrationDomainEntity.setDomainIndex(1);
		registrationDomainEntity.setDomainKey("domain_key2");
		domainList.add(registrationDomainEntity);

		Mockito.when(registrationDomainService.getRegistrationDomainEntity())
				.thenReturn(domainList);
		
//		deviceLookUpServiceImpl = new DeviceLookUpServiceImpl(
//				registrationDomainService);
	}

	@Test
	public void updateCloudIdDuringReregistrationTest()
			throws DeviceClaimRetriableException {

		RegistrationDomainEntity registrationDomainEntity = Mockito
				.mock(RegistrationDomainEntity.class);

		PrinterInfo ssnClaimInfo = new PrinterInfo();
		ssnClaimInfo.setPrinterId("AQAAAAFUVoFJlgAAAAHPqpXn");

		PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity();
		printerDataLookUpEntity.setCloudId("SomeOldIdInTheDynamoDb");

		RegisterPrinter deviceClaim = getRegDeviceClaim(0);
		Mockito.when(
				printerDataLookUpDaoImpl.createPrinterDataLookUp(Mockito
						.any(PrinterDataEntity.class))).thenReturn(
				new PrinterDataEntity());

		Mockito.when(
				registrationDomainService.getRegDomainKey(Mockito.anyInt()))
				.thenReturn(registrationDomainEntity);

		Mockito.when(registrationDomainEntity.getDomainKey()).thenReturn(
				"testDomainKey");
		Mockito.when(
				printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(Mockito
						.anyString())).thenReturn(printerDataLookUpEntity);
		Mockito.when(
				generatePrinterDataResponse.createResponse(
						Mockito.any(SSNFields.class),
						Mockito.any(PrinterDataEntity.class))).thenReturn(
				ssnClaimInfo);

		deviceLookUpServiceImpl.processPrinter(deviceClaim);

		Mockito.verify(printerDataLookUpDaoImpl).createPrinterDataLookUp(
				Mockito.any(PrinterDataEntity.class));
	}

	@Test
	public void processPrinterCodeData() throws DeviceClaimRetriableException {
		// SSNClaimInfo ssnClaimInfo =
		// deviceLookUpServiceImpl.processPrinterCodeData(null);

		PrinterDataEntity printerDataLookUpEntity = deviceLookUpServiceImpl
				.processPrinterCodeData(createSSN());

		Assert.assertEquals(printerDataLookUpEntity.isInkCapable(), true);
	}

	@Test
	public void getPrinterCodeDataTest() throws DeviceClaimRetriableException {
		// SSNClaimInfo ssnClaimInfo =
		// deviceLookUpServiceImpl.processPrinterCodeData(null);
		Mockito.when(printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(Mockito.anyString())).thenReturn(createEntity());

		PrinterDataEntity printerDataLookUpEntity = deviceLookUpServiceImpl
				.getPrinterCodeData(createSSN());

		Assert.assertEquals(printerDataLookUpEntity.getPrinterCode(), "hello12");
	}

	@Test(expectedExceptions = SNKeyNotFoundException.class)
	public void getPrinterCodeDataThrowsSnKeyExceptionTest() throws DeviceClaimRetriableException {
		// SSNClaimInfo ssnClaimInfo =
		// deviceLookUpServiceImpl.processPrinterCodeData(null);
		//Mockito.when(printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(Mockito.anyString())).thenReturn(createEntity());

		PrinterDataEntity printerDataLookUpEntity = deviceLookUpServiceImpl
				.getPrinterCodeData(createSSN());

		Assert.assertEquals(printerDataLookUpEntity.getPrinterCode(), "hello12");
	}

	@Test
	public void deletePrinterCodeDataTest() throws DeviceClaimRetriableException {
		Mockito.when(printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(Mockito.anyString())).thenReturn(createEntity());
		deviceLookUpServiceImpl.deletePrinterCodeData(createSSN());
	}

	@Test(expectedExceptions = SNKeyNotFoundException.class)
	public void deletePrinterCodeDataThrowsSnKeyExceptionTest() throws DeviceClaimRetriableException {
		deviceLookUpServiceImpl.deletePrinterCodeData(createSSN());
	}

	@Test
	public void getPrinterCodeDataThrowsExceptionTest() throws DeviceClaimRetriableException {
		// SSNClaimInfo ssnClaimInfo =
		// deviceLookUpServiceImpl.processPrinterCodeData(null);
		Mockito.when(printerDataLookUpDaoImpl.getPrinterDataLookUpEntity(Mockito.anyString())).thenReturn(createEntity());

		PrinterDataEntity printerDataLookUpEntity = deviceLookUpServiceImpl
				.getPrinterCodeData(createNullSSN());

		Assert.assertEquals(printerDataLookUpEntity.getPrinterCode(), "hello12");
	}
	@DataProvider(name = "deviceClaimSamples")
	public Object[][] passwords() {
		return new Object[][] { { getRegDeviceClaim(0), false },
				{ getRegDeviceClaim(0), false },// GUAEGGH2-39DSFSJK2L
				{ getRegDeviceClaim(1), true }

		};
	}

	private DeviceClaim getDeviceClaim(String SSN) {
		DeviceClaim deviceClaim = new DeviceClaim();
		deviceClaim.setDomainIndex(SSN);
		deviceClaim.setCloudId("AQAAAAFUVoFJlgAAAAHPqpXn");
		deviceClaim.setEntityId("TH5C418027");
		return deviceClaim;
	}

	private RegisterPrinter getRegDeviceClaim(int domainIndex) {
		RegisterPrinter regDeviceClaim = new RegisterPrinter();
		regDeviceClaim.setDomainIndex(domainIndex);
		regDeviceClaim.setCloudId("AQAAAAFUVoFJlgAAAAHPqpXn");
		regDeviceClaim.setEntityId("TH5C418027");
		return regDeviceClaim;
	}

	public PrinterInfo createMockedResponse(SSNFields ssnEntity,
			PrinterDataEntity printerDataLookUpEntity, RequestType requestType,
			Boolean isRegistered) {
		PrinterInfo ssnData = new PrinterInfo();
		ssnData.setIsInkCapable(ssnEntity.getInstantInkFlag());
		ssnData.setVersion("1.0");
		ssnData.setSnKey(printerDataLookUpEntity.getSnKey());
		return ssnData;
	}

	private PrinterCodeData createSSN() {
		PrinterCodeData ssnEntity = new PrinterCodeData();
		ssnEntity.setDomainIndex(1);
		ssnEntity.setInstantInkFlag(true);
		ssnEntity.setIssuanceCounter(100);
		ssnEntity.setOverrunBit(false);
		ssnEntity.setSerialNumber("Model123");
		ssnEntity.setPrinterCode("hello12");
		ssnEntity.setVersion("1.0");

		return ssnEntity;
	}
	private PrinterCodeData createNullSSN() {
		PrinterCodeData ssnEntity = new PrinterCodeData();
		ssnEntity.setPrinterCode(" ");



		return ssnEntity;
	}
	private PrinterDataEntity createEntity() {
		PrinterDataEntity ssnEntity = new PrinterDataEntity();
		ssnEntity.setDomainIndex(1);

		ssnEntity.setIssuanceCounter(100);


		ssnEntity.setPrinterCode("hello12");

		return ssnEntity;
	}

}

