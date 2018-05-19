
package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.discovery.client.data.DiscoveryStatus;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnc.common.enums.UrlType;
import com.hp.wpp.ssnc.common.enums.UrlTypeForResponse;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.DiscoveryLink;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterCodeInfo;

import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import com.hp.wpp.ssnclaim.service.response.PrinterCodeResponseGenerator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by kumaniti on 1/13/2017.
 */


public class TestNewPrinterCodeResponse {

    @InjectMocks
    private PrinterCodeResponseGenerator printerCodeResponseGenerator;
    @Mock
    LinksDaoImpl linksDao;
    @Mock
    LookUpResponseGenerator lookUpResponseGenerator;

    @Mock
    PrinterResourceServiceConfig printerResourceServiceConfig;

    @Mock
    DiscoveryClient discoveryClient;

    @BeforeClass
    void setUp() {
        printerCodeResponseGenerator = new PrinterCodeResponseGenerator();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateResponse() {

        LinksEntity linksEntity = new LinksEntity();
        linksEntity.setUrlType("url_type");
        linksEntity.setUrlValue("url_value");
        PrinterCodeInfo pInfo = new PrinterCodeInfo();
        PrinterCodeData pData = new PrinterCodeData();
        pData.setVersion("1.0");
        pData.setIssuanceCounter(10);
        pData.setDomainIndex(1);
        pData.setOverrunBit(Boolean.FALSE);
        pData.setInstantInkFlag(Boolean.TRUE);
        pData.setPrinterCode("HELLO123");
        pData.setSerialNumber("SN1234567");

        PrinterDataEntity printerClaimEntity = new PrinterDataEntity();
        printerClaimEntity.setOverRunBit(false);
        printerClaimEntity.setCloudId("AQAAAAFU_JJZnAAAAAFpVlpF");
        printerClaimEntity.setInkCapable(false);
        printerClaimEntity.setIsRegistered(true);

        printerClaimEntity
                .setSnKey("8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");

        printerClaimEntity.setDomainIndex(1);
        Link self = new Link();
        self.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/SSN_info/HAABKACUO0FVO4BNFF");
        self.setRel("self");

        Link lookup = new Link();
        lookup.setHref("http://pie-globalpod1-devcl-pod-lb-1129532228.us-west-2.elb.amazonaws.com/virtualprinter/v1/printer?SN_key=8AoN5gnYL2m6S33S1GZoVoHHNzSaQyBOcRE1By5J05Q");
        lookup.setRel("sn_key_lookup");

        Link info = new Link();
        info.setHref("http://pie-pod1-vpdev-Pod-lb-557820310.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/info/AQAAAAFU_JJZnAAAAAFpVlpF");
        info.setRel("printer_info");

        List<Link> links = new ArrayList<>();

        links.add(self);
        links.add(lookup);
        links.add(info);
        pInfo.setConfigurations(links);
        LinksEntity linksEntityDiscovery = new LinksEntity();
        List<DiscoveryLink> dLink = new ArrayList<>();
        DiscoveryLink link = new DiscoveryLink();
        link.setRel("discoveryMoock");
        link.setHref("discovery_url_mock");
        dLink.add(link);
        linksEntityDiscovery.setUrlType("discovery_Url");
        linksEntityDiscovery.setUrlValue("http:\\discoverylink");
        when(lookUpResponseGenerator.getDiscoveryServiceClient()).thenReturn(discoveryClient);
        when(lookUpResponseGenerator.getPrinterResourceServiceConfig()).thenReturn(printerResourceServiceConfig);
        when(discoveryClient.getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(populateDiscoveryResponse());
        when(lookUpResponseGenerator.getLink(Mockito.any(UrlType.class), Mockito.any(UrlTypeForResponse.class), Mockito.anyString())).thenReturn(info);
        when(linksDao.getLinkUrl(Mockito.anyString())).thenReturn(linksEntity);
        when(linksDao.getLinkUrl("discovery_url")).thenReturn(linksEntityDiscovery);
        pInfo = printerCodeResponseGenerator.createPrinterCodeResponse(pData, printerClaimEntity);
        System.out.println(pInfo.toString());

        Assert.assertEquals(pInfo.prevPrinterCode.getIsInkCapable(), Boolean.FALSE);
        Assert.assertEquals(pInfo.currentPrinterCode.getIsInkCapable(), Boolean.TRUE);
        Assert.assertNotNull(pInfo.getClaimCode());
        verify(linksDao, atLeastOnce()).getLinkUrl(Mockito.anyString());
        verify(discoveryClient).getDiscoveryResponse(Mockito.anyString(),Mockito.anyMap(),Mockito.anyInt(),Mockito.anyInt());

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

