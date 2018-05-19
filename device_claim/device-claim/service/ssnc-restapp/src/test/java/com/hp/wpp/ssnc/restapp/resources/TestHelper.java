package com.hp.wpp.ssnc.restapp.resources;

import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.service.impl.DeviceLookUpServiceImpl;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;

public class TestHelper {
	
	@Mock
	RegistrationDomainDaoImpl regDomainService;
	
	@Mock
	LinksDaoImpl linksService;
	
	@Mock
	PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;
	
	private DeviceLookUpServiceImpl deviceLookUpServiceImpl;
	
	
	@BeforeMethod
	public void setUp() {
		RegistrationDomainEntity regEntity = new RegistrationDomainEntity(1,"testDomainKey");
		MockitoAnnotations.initMocks(this);
		deviceLookUpServiceImpl = new DeviceLookUpServiceImpl(regDomainService, printerDataLookUpDaoImpl, null);
		deviceLookUpServiceImpl.setRegistrationDomainService(regDomainService);
		deviceLookUpServiceImpl.setPrinterDataLookUpDaoImpl(printerDataLookUpDaoImpl);
		doReturn(regEntity).when(regDomainService).getRegDomainKey(1);
		
	}

	public void createInputData() {
		SSNFields ssnEntity =  new SSNFields();
		ssnEntity.setDomainIndex(1);
		ssnEntity.setInstantInkFlag(false);
		ssnEntity.setIssuanceCounter(100);
		ssnEntity.setOverrunBit(false);
		ssnEntity.setSerialNumber("Model123");
		ssnEntity.setSsn("hello12");
		ssnEntity.setVersion("1.0");
		Link link = new Link();
		link.setHref("hi12");
		link.setRel("hi1234");
		
		List<Link> links = new ArrayList<Link>();
		LinksEntity linkEntity = new LinksEntity();
		linkEntity.setUrlType("helo123");
		linkEntity.setUrlValue("helo1233");
	    links.add(link);
		PrinterInfo ssnData = new PrinterInfo();
		ssnData.setIsInkCapable(false);
		ssnData.setIsPrinterRegistered(false);
		ssnData.setSnKey("mo1241mo");
		ssnData.setVersion("1.0");
		ssnData.setConfigurations(links);
	    PrinterDataEntity printerDataLookUpEntity=  new PrinterDataEntity("hello", 1, true, "");
		doReturn(null).when(printerDataLookUpDaoImpl).createPrinterDataLookUp(printerDataLookUpEntity);
//		doReturn(ssnData).when(generatePrinterCodeResponse).createResponse(any(SSNFields.class), any(SSNPrinterClaimEntity.class));
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
		doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
		doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
	}
	
	public void createInputRegistered() {
		SSNFields ssnEntity =  new SSNFields();
		ssnEntity.setDomainIndex(1);
		ssnEntity.setInstantInkFlag(false);
		ssnEntity.setIssuanceCounter(100);
		ssnEntity.setOverrunBit(false);
		ssnEntity.setSerialNumber("Model123");
		ssnEntity.setSsn("hello12");
		ssnEntity.setVersion("1.0");
		Link link = new Link();
		link.setHref("hi12");
		link.setRel("hi1234");
		List<Link> links = new ArrayList<Link>();
		LinksEntity linkEntity = new LinksEntity();
		linkEntity.setUrlType("helo123");
		linkEntity.setUrlValue("helo1233");
	    links.add(link);
		PrinterInfo ssnData = new PrinterInfo();
		ssnData.setIsInkCapable(false);
		ssnData.setIsPrinterRegistered(true);
		ssnData.setSnKey("mo1241mo");
		ssnData.setVersion("1.0");
		ssnData.setConfigurations(links);
		 PrinterDataEntity printerDataLookUpEntity=  new PrinterDataEntity("hello", 1, true, "");
		doReturn(null).when(printerDataLookUpDaoImpl).createPrinterDataLookUp(printerDataLookUpEntity);
//		doReturn(ssnData).when(generatePrinterCodeResponse).createResponse(any(SSNFields.class), any(SSNPrinterClaimEntity.class));
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
		doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
		doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
		doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
	}
}
