package com.hp.wpp.ssnclaim.service;

import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.discovery.client.data.DiscoveryStatus;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;

/**
 * Created by kumaniti on 12/1/2017.
 */
public class LookupResponseGeneratorTest {

    @InjectMocks
    LookUpResponseGenerator responseGenerator;
    @Mock
    private LinksDaoImpl linksDaoImpl;

    @Mock
    private DiscoveryClient discoveryServiceClient;

   @Mock
    private PrinterResourceServiceConfig printerResourceServiceConfig;

    @BeforeMethod
    public void setUp(){
        responseGenerator=new LookUpResponseGenerator();
        MockitoAnnotations.initMocks(this);
    }
   @Test
    public void testCreateResponseSnkeyBased(){
       Mockito.when(linksDaoImpl.getLinkUrl(Mockito.anyString())).thenReturn(prepareLinkObj());
       PrinterInfo info=responseGenerator.createResponse(preparePrinterDataEntity(),true);
       Assert.assertEquals(info.getClaimCode(),"mockclaimcode");

   }
    @Test
    public void testCreateResponseNotSnkeyBased(){
        Mockito.when(linksDaoImpl.getLinkUrl(Mockito.anyString())).thenReturn(prepareLinkObj());
        PrinterInfo info=responseGenerator.createResponse(preparePrinterDataEntity(),false);
        Assert.assertEquals(info.getClaimCode(),"mockclaimcode");

    }

    @Test
    public void buildLinks(){
        java.util.List<Link> links = new ArrayList();
        java.util.List<com.hp.wpp.discovery.client.data.Link> lis=new ArrayList<>();
        com.hp.wpp.discovery.client.data.Link lin=new com.hp.wpp.discovery.client.data.Link();
        lin.setHref("email_address");lin.setRel("email_address");
        lis.add(lin);
        DiscoveryResponse res=prepareDiscRe();
        res.setLinks(lis);
        Mockito.when(discoveryServiceClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(res);
        Mockito.when(linksDaoImpl.getLinkUrl(Mockito.anyString())).thenReturn(prepareLinkObj());
        responseGenerator.buildLinks(true,prepareInfo(),preparePrinterDataEntity(),links);
        Mockito.verify(linksDaoImpl,Mockito.times(3)).getLinkUrl(Mockito.anyString());
    }
    @Test
    public void buildLinksWithLinksNull(){
        java.util.List<Link> links = new ArrayList();
        Mockito.when(discoveryServiceClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(prepareDiscRe());
        Mockito.when(linksDaoImpl.getLinkUrl(Mockito.anyString())).thenReturn(prepareLinkObj());
        responseGenerator.buildLinks(true,prepareInfo(),preparePrinterDataEntity(),links);
        Mockito.verify(linksDaoImpl,Mockito.times(3)).getLinkUrl(Mockito.anyString());
    }

    private DiscoveryResponse prepareDiscRe() {
        DiscoveryResponse res=new DiscoveryResponse(DiscoveryStatus.SUCCESS);
        return res;
    }

    private PrinterInfo prepareInfo(){
        PrinterInfo info=new PrinterInfo();
        info.setClaimCode("mockclaimcode");
        info.setIsPrinterRegistered(true);
        return  info;
    }

    private LinksEntity prepareLinkObj() {
        LinksEntity entity=new LinksEntity();
        entity.setUrlType("MockUrl");
        entity.setUrlValue("https://thisIsnotGonnaWork");
        return entity;
    }

    private PrinterDataEntity preparePrinterDataEntity() {
        PrinterDataEntity entity=new PrinterDataEntity();
        entity.setSnKey("mocksnkey");
        entity.setIsRegistered(false);
        entity.setInkCapable(true);
        entity.setSnKey("mockclaimcode");
        entity.setPrinterCode("mockPrinterCode");
        entity.setDomainIndex(7);
        entity.setCloudId("mockcloudid");
        return  entity;
    }
}

