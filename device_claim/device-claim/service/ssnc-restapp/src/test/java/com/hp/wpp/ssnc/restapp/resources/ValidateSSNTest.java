package com.hp.wpp.ssnc.restapp.resources;

import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeException;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.impl.PrinterCodeValidatorImpl;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.service.impl.DeviceLookUpServiceImpl;
import junit.framework.Assert;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;
import com.hp.wpp.ssnclaim.exception.DeviceClaimRetriableException;
import com.hp.wpp.ssnclaim.exception.InvalidHeaderSignatureException;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;

public class ValidateSSNTest {

	@InjectMocks
	private DeviceLookUpServiceImpl deviceLookUpServiceImpl;

	@Mock
	RegistrationDomainDaoImpl regDomainService;

	@Mock
	LinksDaoImpl linksService;

	@Mock
	PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;

	@Mock
	private PrinterCodeValidatorImpl printerCodeImpl;

	@Mock
	private RegistrationDomainEntity registrationDomainEntity;


	String domainRegKey = "testDomainKey";

	public List<RegistrationDomainEntity> domainList;

	@BeforeMethod
	public void setUp() throws InvalidPrinterCodeException {
		RegistrationDomainEntity regEntity = new RegistrationDomainEntity(1,
				"testDomainKey");
		MockitoAnnotations.initMocks(this);
		domainList = new ArrayList<RegistrationDomainEntity>();
		domainList.add(regEntity);		
		Mockito.when(regDomainService.getRegistrationDomainEntity())
				.thenReturn(domainList);
		//deviceLookUpServiceImpl = new DeviceLookUpServiceImpl(regDomainService);
		
		deviceLookUpServiceImpl.setRegistrationDomainService(regDomainService);
		deviceLookUpServiceImpl
				.setPrinterDataLookUpDaoImpl(printerDataLookUpDaoImpl);

	}

	@Test
	public void testSSNSignatureValid() throws Exception {
		createInputData();
		Mockito.when(regDomainService.getRegDomainKey(Mockito.anyInt())).thenReturn(registrationDomainEntity);
		Mockito.when(registrationDomainEntity.getDomainKey()).thenReturn("testDomainKey");
				SSNFields ssnFields = deviceLookUpServiceImpl
						.validateSSNCode("EEAFRJB7SN15092333");


		PrinterCodeData printerFields = getPrinterFieldsFromSsn(
				"EEAFRJB7SN15092333", ssnFields);
		PrinterDataEntity ssnData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerFields);
		Assert.assertEquals(ssnData.isInkCapable(), true);
	}

	@Test
	public void testRegisteredPrinter() throws Exception {
		createInputRegistered();
		Mockito.when(regDomainService.getRegDomainKey(Mockito.anyInt())).thenReturn(registrationDomainEntity);
		Mockito.when(registrationDomainEntity.getDomainKey()).thenReturn("testDomainKey");
		SSNFields ssnFields = deviceLookUpServiceImpl
				.validateSSNCode("EEAFRJB7SN15092333");
		PrinterCodeData printerFields = getPrinterFieldsFromSsn(
				"EEAFRJB7SN15092333", ssnFields);
		PrinterDataEntity ssnData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerFields);
		Assert.assertEquals(ssnData.isInkCapable(), true);
	}

	@Test(expectedExceptions = DeviceClaimRetriableException.class)
	public void testSSNSignatureInValid() throws Exception {
		createInputData();
		SSNFields ssnFields = deviceLookUpServiceImpl
				.validateSSNCode("EEAF-JB-SN15092333");
		PrinterCodeData printerCodeFields = getPrinterFieldsFromSsn(
				"EEAFRJB7SN15092333", ssnFields);
		PrinterDataEntity ssnData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerCodeFields);
	}

	@Test
	public void testPrinterCodeSignatureValid() throws Exception {
		createInputData();
		Mockito.when(
				printerCodeImpl.validatePrinterCode(Mockito.anyString(),
						Mockito.anyMap())).thenReturn(getPrinterCodeData());
		PrinterCodeData printerCodeFields = deviceLookUpServiceImpl
				.validatePrinterCode("AJK82LERYX5JD634");
		Assert.assertEquals(printerCodeFields.getDomainIndex(), 1);
		Assert.assertTrue(printerCodeFields.getInstantInkFlag());
		Assert.assertEquals(printerCodeFields.getPrinterCode(),
				"AJK82LERYX5JD634");

		PrinterDataEntity printerData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerCodeFields);
		Assert.assertEquals(printerData.isInkCapable(), true);
	}

	@Test
	public void testRegisteredPrinterWithPrinterCode() throws Exception {
		createInputRegistered();
		Mockito.when(
				printerCodeImpl.validatePrinterCode(Mockito.anyString(),
						Mockito.anyMap())).thenReturn(getPrinterCodeData());


		PrinterCodeData printerCodeFields = deviceLookUpServiceImpl
				.validatePrinterCode("AJK82LERYX5JD634");
		PrinterDataEntity printerData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerCodeFields);
		Assert.assertEquals(printerData.getDomainIndex(), 1);
		Assert.assertEquals(printerData.isInkCapable(), true);
		Assert.assertEquals(printerData.getPrinterCode(), "AJK82LERYX5JD634");
	}

	@Test(expectedExceptions = InvalidHeaderSignatureException.class)
	public void testInvalidSignature() throws Exception {
		createInputData();
		Mockito.when(regDomainService.getRegDomainKey(Mockito.anyInt())).thenReturn(registrationDomainEntity);
		Mockito.when(registrationDomainEntity.getDomainKey()).thenReturn("testDomainKey");
		SSNFields ssnFields = deviceLookUpServiceImpl
				.validateSSNCode("EEAFRJB7SN15092456");
		PrinterCodeData printerCodeFields = getPrinterFieldsFromSsn(
				"EEAFRJB7SN15092456", ssnFields);
		PrinterDataEntity ssnData = deviceLookUpServiceImpl
				.processPrinterCodeData(printerCodeFields);

	}

	public void createInputData() {
		Link link = new Link();
		link.setHref("hi12");
		link.setRel("hi1234");

		List<Link> links = new ArrayList<Link>();
		LinksEntity linkEntity = new LinksEntity();
		linkEntity.setUrlType("helo123");
		linkEntity.setUrlValue("helo1233");
		links.add(link);
		PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity(
				"hello", 1, true, "");
		doReturn(null).when(printerDataLookUpDaoImpl).createPrinterDataLookUp(
				printerDataLookUpEntity);
		// doReturn(ssnData).when(generatePrinterCodeResponse).createResponse(any(SSNFields.class),
		// any(PrinterDataLookUpEntity.class));
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
		doReturn(linkEntity).when(linksService).getLinkUrl(
				"printer_info_registered");
		doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
		doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
	}

	public void createInputRegistered() {

		Link link = new Link();
		link.setHref("hi12");
		link.setRel("hi1234");
		List<Link> links = new ArrayList<Link>();
		LinksEntity linkEntity = new LinksEntity();
		linkEntity.setUrlType("helo123");
		linkEntity.setUrlValue("helo1233");
		links.add(link);
		PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity(
				"hello", 1, true, "printer123");
		doReturn(null).when(printerDataLookUpDaoImpl).createPrinterDataLookUp(
				printerDataLookUpEntity);
		// doReturn(ssnData).when(generatePrinterCodeResponse).createResponse(any(SSNFields.class),
		// any(PrinterDataLookUpEntity.class));
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
		doReturn(linkEntity).when(linksService).getLinkUrl(
				"printer_info_registered");
		doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
		doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
	}

	private PrinterCodeData getPrinterFieldsFromSsn(String code,
			SSNFields ssnFields) {
		PrinterCodeData printerCodeData = new PrinterCodeData();
		BeanUtils.copyProperties(ssnFields, printerCodeData);
		printerCodeData.setOwnership(0);
		printerCodeData.setPrinterCode(code);
		return printerCodeData;
	}

	private PrinterCodeData getPrinterCodeData() {
		PrinterCodeData printerFields = new PrinterCodeData();
		printerFields.setDomainIndex(1);
		printerFields.setInstantInkFlag(true);
		printerFields.setSerialNumber("O0FVO4BNFF");
		printerFields.setPrinterCode("AJK82LERYX5JD634");
		return printerFields;
	}
}
