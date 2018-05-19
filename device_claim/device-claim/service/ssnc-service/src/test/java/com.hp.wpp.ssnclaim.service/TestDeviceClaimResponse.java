package com.hp.wpp.ssnclaim.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.discovery.client.data.DiscoveryStatus;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnc.common.enums.UrlType;
import com.hp.wpp.ssnc.common.enums.UrlTypeForResponse;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterCodeInfo;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeFields;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import com.hp.wpp.ssnclaim.service.response.PrinterCodeResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.DiscoveryLink;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;

public class TestDeviceClaimResponse {
    @InjectMocks
    private PrinterCodeResponseGenerator generatePrinterCodeResponse;
    @Mock
    LookUpResponseGenerator lookUpResponseGenerator;
    @Mock
    LinksDaoImpl linksService;

    @Mock
    PrinterResourceServiceConfig printerResourceServiceConfig;

    @Mock
    DiscoveryClient discoveryClient;


    @BeforeMethod
    public void setUp() {
        generatePrinterCodeResponse = new PrinterCodeResponseGenerator();
        MockitoAnnotations.initMocks(this);
        //generatelookUpResponse = new LookUpResponseGenerator();
        //generatelookUpResponse.setLinksDaoImpl(linksService);
        //generatePrinterCodeResponse.setLinksDaoImpl(linksService);
    }

    @Test
    public void testCreateResponseForSSNClaim() {
        SSNFields ssnEntity = new SSNFields();
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
        PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity("hello", 1, true, "");
        List<DiscoveryLink> dLink = new ArrayList<>();
        DiscoveryLink dlink = new DiscoveryLink();
        link.setRel("discoveryMoock");
        link.setHref("discovery_url_mock");
        dLink.add(dlink);
        when(lookUpResponseGenerator.getDiscoveryServiceClient()).thenReturn(discoveryClient);
        when(lookUpResponseGenerator.getPrinterResourceServiceConfig()).thenReturn(printerResourceServiceConfig);
        when(discoveryClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(populateDiscoveryResponse());
     //   when(lookUpResponseGenerator.getDiscoveryClient(Mockito.any(PrinterDataEntity.class), Mockito.any(LinksEntity.class))).thenReturn(entityDiscoveryClient);
       // when(entityDiscoveryClient.discoverClaiminfo()).thenReturn(dLink);
        when(lookUpResponseGenerator.getLink(Mockito.any(UrlType.class), Mockito.any(UrlTypeForResponse.class), Mockito.anyString())).thenReturn(link);
        doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
        doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_code");
        mockdiscoveryapi();
        PrinterInfo ssnClaim = generatePrinterCodeResponse.createResponse(ssnEntity, printerDataLookUpEntity);
        Assert.assertEquals(ssnClaim.getVersion(), "1.0");
    }

    @Test
    public void testCreateResponseForSNKeyLookUp()

    {
        SSNFields ssnEntity = new SSNFields();
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
        PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity("hello", 1, true, "");
        doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
        doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code_validation");
        mockdiscoveryapi();
        when(lookUpResponseGenerator.getPrinterResourceServiceConfig()).thenReturn(printerResourceServiceConfig);
        PrinterInfo ssnClaim = lookUpResponseGenerator.createResponse(printerDataLookUpEntity, true);
        Assert.assertEquals(ssnClaim.getVersion(), "1.0");
    }

    @Test
    public void testSSNClaimForRegisteredPrinter() {

        SSNFields ssnEntity = new SSNFields();
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
        when(lookUpResponseGenerator.getDiscoveryServiceClient()).thenReturn(discoveryClient);
        when(lookUpResponseGenerator.getPrinterResourceServiceConfig()).thenReturn(printerResourceServiceConfig);
        //when(discoveryClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(populateDiscoveryResponse());
        when(lookUpResponseGenerator.getLink(Mockito.any(UrlType.class), Mockito.any(UrlTypeForResponse.class), Mockito.anyString())).thenReturn(link);

        PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity("hello", 1, true, "southe3pk");
        printerDataLookUpEntity.setIsRegistered(true);
        doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
        doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code_validation");
        mockdiscoveryapi();
        PrinterInfo ssnClaim = generatePrinterCodeResponse.createResponse(ssnEntity, printerDataLookUpEntity);
        Assert.assertEquals(ssnClaim.getIsPrinterRegistered(), Boolean.TRUE);
    }

    @Test
    public void testSNKeyLookUpForRegisteredPrinter() {

        SSNFields ssnEntity = new SSNFields();
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
        PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity("hello", 1, true, "southe3pk");
        printerDataLookUpEntity.setIsRegistered(true);
        doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
        doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code_validation");
        mockdiscoveryapi();

        PrinterInfo ssnClaim = lookUpResponseGenerator.createResponse(printerDataLookUpEntity, true);
        Assert.assertEquals(ssnClaim.getIsPrinterRegistered(), Boolean.TRUE);
    }

    @Test
    public void testClaimCodeLookUpForRegisteredPrinter() {

        SSNFields ssnEntity = new SSNFields();
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
        PrinterDataEntity printerDataLookUpEntity = new PrinterDataEntity("hello", 1, true, "southe3pk");
        printerDataLookUpEntity.setIsRegistered(true);
        doReturn(linkEntity).when(linksService).getLinkUrl("ssn_claim");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_info_registered");
        doReturn(linkEntity).when(linksService).getLinkUrl("sn_key_lookup");
        doReturn(linkEntity).when(linksService).getLinkUrl("printer_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code");
        doReturn(linkEntity).when(linksService).getLinkUrl("claim_code_validation");
        mockdiscoveryapi();

        PrinterInfo ssnClaim = lookUpResponseGenerator.createResponse(printerDataLookUpEntity, false);
        Assert.assertEquals(ssnClaim.getIsPrinterRegistered(), Boolean.TRUE);
    }

    @Test
    public void testCreateResponseForPrinterDataLookUp(){
        Link link = new Link();
        link.setHref("mockhref");
        link.setRel("mockrel");
        Mockito.when(lookUpResponseGenerator.getLink(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(link);
        mockdiscoveryapi();
        PrinterInfo info = generatePrinterCodeResponse.createResponse(populatePrinterCodeFields(),populatePrinterDataLookUPEntity());
        Assert.assertEquals(info.getSnKey(),"mocksnkey");
    }
    @Test
    public void testPrinterCodeResponse(){
        Link link = new Link();
        link.setHref("mockhref");
        link.setRel("mockrel");
        Mockito.when(lookUpResponseGenerator.getLink(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(link);
        mockdiscoveryapi();
        PrinterCodeInfo info = generatePrinterCodeResponse.createPrinterCodeResponse(populatePrinterCodeFields(),populatePrinterDataLookUPEntity());
        Assert.assertEquals(info.getSnKey(),"mocksnkey");
        Assert.assertEquals(info.getPrevPrinterCode().getIssuanceCounter(),7);
        Assert.assertEquals(info.getCurrentPrinterCode().getIssuanceCounter(),6);
    }

    private PrinterDataEntity populatePrinterDataLookUPEntity() {
        PrinterDataEntity entity = new PrinterDataEntity();
        entity.setIsRegistered(true);
        entity.setSnKey("mocksnkey");
        entity.setInkCapable(true);
        entity.setCloudId("mockcloudid");
        entity.setOverRunBit(true);
        entity.setPrinterCode("mockprintercode");
        entity.setDomainIndex(7);
        entity.setIssuanceCounter(7);
        entity.setOwnershipCounter(7);
        return entity;
    }

    private PrinterCodeData populatePrinterCodeFields() {
        PrinterCodeData field = new PrinterCodeData();
        field.setDomainIndex(6);
        field.setPrinterCode("mockprintercode2");
        field.setIssuanceCounter(6);
        field.setInstantInkFlag(false);
       field.setOverrunBit(false);
        field.setOwnership(6);
        field.setSerialNumber("mockserialnumber");
        return  field;
    }

    private void mockdiscoveryapi() {
        LinksEntity linkEntityd = populateDiscoveryurl();
        doReturn(linkEntityd).when(linksService).getLinkUrl("discovery_url");
        DiscoveryResponse discoverylinks = populateDiscoveryResponse();
        lookUpResponseGenerator = Mockito.spy(new LookUpResponseGenerator());
        lookUpResponseGenerator.setLinksDaoImpl(linksService);
        when(lookUpResponseGenerator.getDiscoveryServiceClient()).thenReturn(discoveryClient);
        when(lookUpResponseGenerator.getPrinterResourceServiceConfig()).thenReturn(printerResourceServiceConfig);
        when(discoveryClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(populateDiscoveryResponse());
    }

    private LinksEntity populateDiscoveryurl() {
        LinksEntity linkEntityd = new LinksEntity();
        linkEntityd.setUrlType("discovery_url");
        linkEntityd.setUrlValue("http://localhost/avatat/v1/entities?cloud-id=adf");
        return linkEntityd;
    }

    private DiscoveryResponse populateDiscoveryResponse() {
        ArrayList links = new ArrayList();
        com.hp.wpp.discovery.client.data.Link link1 = new com.hp.wpp.discovery.client.data.Link();
        link1.setRel("http://pie-pod1-SignalMgmt-Pod-lb-611920108.us-west-2.elb.amazonaws.com:443/");
        link1.setHref("signal-mgmt");
        links.add(link1);

        com.hp.wpp.discovery.client.data.Link link2 = new com.hp.wpp.discovery.client.data.Link();
        link2.setHref("http://54.69.60.47:8080/virtualprinter/v1/printers/AQAAAAFTwojJugAAAAEtVE6Z/emailaddress");
        link2.setRel("email_address");
        links.add(link2);


        com.hp.wpp.discovery.client.data.Link link3 = new com.hp.wpp.discovery.client.data.Link();
        link3.setHref("http://54.69.60.47:8080/virtualprinter/v1/printers/AQAAAAFTwojJugAAAAEtVE6Z/cloudconfig");
        link3.setRel("cloud_config");
        links.add(link3);

        return new DiscoveryResponse(DiscoveryStatus.SUCCESS, links);
    }
}
