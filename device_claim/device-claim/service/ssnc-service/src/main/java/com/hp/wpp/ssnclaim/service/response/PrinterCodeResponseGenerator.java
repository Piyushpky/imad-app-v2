package com.hp.wpp.ssnclaim.service.response;

import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.discovery.client.data.DiscoveryStatus;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnc.common.enums.UrlType;
import com.hp.wpp.ssnc.common.enums.UrlTypeForResponse;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterCodeInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrinterCodeResponseGenerator {
    @Autowired
    private LinksDaoImpl linksDaoImpl;

    @Autowired
    LookUpResponseGenerator lookUpResponseGenerator;

    public LinksDaoImpl getLinksDaoImpl() {
        return linksDaoImpl;
    }

    public void setLinksDaoImpl(LinksDaoImpl linksDaoImpl) {
        this.linksDaoImpl = linksDaoImpl;
    }

    private static final WPPLogger logger = WPPLoggerFactory
            .getLogger(PrinterCodeResponseGenerator.class);

    @Autowired
    private DiscoveryClient discoveryServiceClient;

    public DiscoveryClient getDiscoveryServiceClient() {
        return discoveryServiceClient;
    }

    @Autowired
    private PrinterResourceServiceConfig printerResourceServiceConfig;

    public PrinterResourceServiceConfig getPrinterResourceServiceConfig() {
        return printerResourceServiceConfig;
    }

    String VERSION = "1.0";

    public PrinterInfo createResponse(SSNFields ssnEntity, PrinterDataEntity printerDataLookUpEntity) {

        Boolean isRegistered = printerDataLookUpEntity.getIsRegistered();
        PrinterInfo ssnData = new PrinterInfo();
        ssnData.setIsInkCapable(printerDataLookUpEntity.isInkCapable());
        ssnData.setVersion(VERSION);
        ssnData.setSnKey(printerDataLookUpEntity.getSnKey());
        ssnData.setIsPrinterRegistered(isRegistered);
        ssnData.setPrinterId(printerDataLookUpEntity.getCloudId());
        List<Link> links = new ArrayList();
        Link link;
        link = lookUpResponseGenerator.getLink(UrlType.SSNCLAIM, UrlTypeForResponse.SELF, null);
        links.add(link);
        links.addAll(createPrinterCodeLinks(isRegistered, printerDataLookUpEntity));
        ssnData.setConfigurations(links);
        link = ssnData.getConfigurations().get(0);
        link.setHref(String.format(link.getHref(), ssnEntity.getSsn()));
        return ssnData;
    }


    private List<Link> createPrinterCodeLinks(Boolean isRegistered, PrinterDataEntity printerDataLookUpEntity) {
        List<Link> links = new ArrayList();
        Link link;
        LinksEntity linksEntity;
        linksEntity = linksDaoImpl.getLinkUrl(UrlType.DISCOVERYTYPE.geturlType());
        link = lookUpResponseGenerator.getLink(UrlType.SNKEYLOOKUP, UrlTypeForResponse.SNKEYLOOKUP, printerDataLookUpEntity.getSnKey());
        links.add(link);
        if (isRegistered) {
            DiscoveryResponse discoveryResponse = getDiscoveryServiceClient().getDiscoveryResponse(printerDataLookUpEntity.getCloudId(), getAvDisAuthHeader(), 0, 2);
            if (discoveryResponse.getDiscoveryStatus().equals(DiscoveryStatus.SUCCESS) && discoveryResponse.getLinks() != null) {
                    for (com.hp.wpp.discovery.client.data.Link discoveryLink : discoveryResponse.getLinks()) {
                        link = new Link();
                        if ((discoveryLink.getRel().equals(UrlTypeForResponse.EMAIL.geturlTypeForResponse()))
                                || (discoveryLink.getRel().equals(UrlTypeForResponse.CLOUDCONFIG.geturlTypeForResponse()))
                                || (discoveryLink.getRel().equals(UrlTypeForResponse.DEVICEINFO.geturlTypeForResponse()))
                                || (discoveryLink.getRel().equals(UrlTypeForResponse.DEVICESTATUS.geturlTypeForResponse()))
                                || (discoveryLink.getRel().equals(UrlTypeForResponse.DATABRIDGE.geturlTypeForResponse()))) {
                            link.setRel(discoveryLink.getRel());
                            link.setHref(discoveryLink.getHref());
                            links.add(link);

                        }
                    }
                    link = lookUpResponseGenerator.getLink(UrlType.PRINTERINFOREGISTERED, UrlTypeForResponse.PRINTERINFO, printerDataLookUpEntity.getCloudId());
                    links.add(link);
                } else {
                    link = lookUpResponseGenerator.getLink(UrlType.PRINTERINFO, UrlTypeForResponse.PRINTERINFO, String.format("%s", printerDataLookUpEntity.getDomainIndex()));
                    links.add(link);
                }
            } else {
                link = lookUpResponseGenerator.getLink(UrlType.PRINTERINFO, UrlTypeForResponse.PRINTERINFO, String.format("%s", printerDataLookUpEntity.getDomainIndex()));
                links.add(link);
            }

        return links;
    }

    private HashMap<String, String> getAvDisAuthHeader() {
        HashMap<String, String> headers = new HashMap();
        headers.put(HttpHeaders.AUTHORIZATION, getPrinterResourceServiceConfig().getAvDisAuthHeader());
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    public PrinterInfo createResponse(PrinterCodeData printerCodeFields,
                                      PrinterDataEntity printerDataLookUpEntity) {
        PrinterInfo ssnData = new PrinterInfo();
        Boolean isRegistered = printerDataLookUpEntity.getIsRegistered();
        ssnData.setIsInkCapable(printerDataLookUpEntity.isInkCapable());
        ssnData.setVersion(VERSION);
        ssnData.setSnKey(printerDataLookUpEntity.getSnKey());
        ssnData.setIsPrinterRegistered(isRegistered);
        ssnData.setPrinterId(printerDataLookUpEntity.getCloudId());
        ssnData.setClaimCode(printerDataLookUpEntity.getSnKey());
        List<Link> links = new ArrayList();
        Link link;
        link = lookUpResponseGenerator.getLink(UrlType.SSNCLAIM, UrlTypeForResponse.SELF, null);
        links.add(link);
        links.addAll(createPrinterCodeLinks(isRegistered, printerDataLookUpEntity));
        if(isRegistered){
        link = lookUpResponseGenerator.getLink(UrlType.PRINTERCODE, UrlTypeForResponse.PRINTERCODE, printerDataLookUpEntity.getPrinterCode());
        links.add(link);
        }
        ssnData.setConfigurations(links);
        link = ssnData.getConfigurations().get(0);
        link.setHref(String.format(link.getHref(), printerCodeFields.getPrinterCode()));
        return ssnData;

    }


    public PrinterCodeInfo createPrinterCodeResponse(PrinterCodeData printerCodeFields, PrinterDataEntity printerDataLookUpEntity) {
        PrinterCodeInfo printerData = new PrinterCodeInfo();
        PrinterCodeInfo.CurrentPrinterCode currentPrinterCode = new PrinterCodeInfo.CurrentPrinterCode();
        PrinterCodeInfo.PrevPrinterCode prevPrinterCode = new PrinterCodeInfo.PrevPrinterCode();
        currentPrinterCode.setOwnershipCounter(printerCodeFields.getOwnership());
        currentPrinterCode.setIssuanceCounter(printerCodeFields.getIssuanceCounter());
        currentPrinterCode.setIsInkCapable(printerCodeFields.getInstantInkFlag());
        currentPrinterCode.setOverrunBit(printerCodeFields.getOverrunBit());
        printerData.setCurrentPrinterCode(currentPrinterCode);
        prevPrinterCode.setOwnershipCounter(printerDataLookUpEntity.getOwnershipCounter());
        prevPrinterCode.setIsInkCapable(printerDataLookUpEntity.isInkCapable());
        prevPrinterCode.setIssuanceCounter(printerDataLookUpEntity.getIssuanceCounter());
        if(printerDataLookUpEntity.getOverRunBit()!=null)
        prevPrinterCode.setOverrunBit(printerDataLookUpEntity.getOverRunBit());
        printerData.setPrevPrinterCode(prevPrinterCode);
        printerData.setVersion(VERSION);
        printerData.setSnKey(printerDataLookUpEntity.getSnKey());
        printerData.setClaimCode(printerDataLookUpEntity.getSnKey());
        printerData.setDeviceUUID(printerDataLookUpEntity.getdeviceUUID());
        Boolean isRegistered = printerDataLookUpEntity.getIsRegistered();
        printerData.setIsPrinterRegistered(isRegistered);
        printerData.setCloudId(printerDataLookUpEntity.getCloudId());
        List<Link> links = new ArrayList();
        Link link;
        link = lookUpResponseGenerator.getLink(UrlType.PRINTERCODE, UrlTypeForResponse.SELF, null);
        links.add(link);
        links.addAll(createPrinterCodeLinks(isRegistered, printerDataLookUpEntity));
        printerData.setConfigurations(links);
        link = printerData.getConfigurations().get(0);
        link.setHref(String.format(link.getHref(), printerCodeFields.getPrinterCode()));
        return printerData;
    }
}
