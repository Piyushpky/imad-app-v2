package com.hp.wpp.ssnclaim.service.response;

import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.discovery.client.data.DiscoveryStatus;
import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnc.common.enums.UrlType;
import com.hp.wpp.ssnc.common.enums.UrlTypeForResponse;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LookUpResponseGenerator {
    @Autowired
    private LinksDaoImpl linksDaoImpl;

    public LinksDaoImpl getLinksDaoImpl() {
        return linksDaoImpl;
    }

    public void setLinksDaoImpl(LinksDaoImpl linksDaoImpl) {
        this.linksDaoImpl = linksDaoImpl;
    }

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

    public PrinterInfo createResponse(PrinterDataEntity printerDataLookUpEntity, boolean snKeyBased) {
        Boolean isRegistered = printerDataLookUpEntity.getIsRegistered();
        PrinterInfo printerInfo = new PrinterInfo();
        printerInfo.setVersion(VERSION);
        printerInfo.setIsInkCapable(printerDataLookUpEntity.isInkCapable());
        printerInfo.setSnKey(printerDataLookUpEntity.getSnKey());
        printerInfo.setClaimCode(printerDataLookUpEntity.getSnKey());
        if(snKeyBased) {
            createLookUpLinks(isRegistered, printerInfo, printerDataLookUpEntity);
        } else {
            createClaimCodeLookUpLinks(isRegistered, printerInfo, printerDataLookUpEntity);
        }
        return printerInfo;
    }

    private void createClaimCodeLookUpLinks(Boolean isRegistered, PrinterInfo printerInfo, PrinterDataEntity printerDataLookUpEntity) {
        List<Link> links = new ArrayList();
        Link link;
        printerInfo.setIsPrinterRegistered(isRegistered);
        String href = printerDataLookUpEntity.getCloudId() + "/" + printerDataLookUpEntity.getSnKey();
        link = getLink(UrlType.CLAIMCODEVALIDATION, UrlTypeForResponse.SELF, href);
        links.add(link);
        link = getLink(UrlType.SNKEYLOOKUP, UrlTypeForResponse.SNKEYLOOKUP, printerDataLookUpEntity.getSnKey());
        links.add(link);

        buildLinks(isRegistered, printerInfo, printerDataLookUpEntity, links);

        printerInfo.setConfigurations(links);
    }

    public void buildLinks(Boolean isRegistered, PrinterInfo printerInfo, PrinterDataEntity printerDataLookUpEntity, List<Link> links) {
        LinksEntity linksEntity;
        Link link;
        linksEntity = linksDaoImpl.getLinkUrl(UrlType.DISCOVERYTYPE.geturlType());


        if(isRegistered)
        {
            DiscoveryResponse discoveryResponse = getDiscoveryServiceClient().getDiscoveryResponse(printerDataLookUpEntity.getCloudId(), getAvDisAuthHeader(), 0, 2);
            if (discoveryResponse.getDiscoveryStatus().equals(DiscoveryStatus.SUCCESS) && discoveryResponse.getLinks() != null) {
                for (com.hp.wpp.discovery.client.data.Link discoveryLink :  discoveryResponse.getLinks()) {
                    link = new Link();
                    if ((discoveryLink.getRel().equals(UrlTypeForResponse.EMAIL.geturlTypeForResponse()))
                            || (discoveryLink.getRel().equals(UrlTypeForResponse.CLOUDCONFIG.geturlTypeForResponse()))
                            || (discoveryLink.getRel().equals(UrlTypeForResponse.DEVICEINFO.geturlTypeForResponse()))
                            || (discoveryLink.getRel().equals(UrlTypeForResponse.DEVICESTATUS.geturlTypeForResponse()))) {
                        link.setRel(discoveryLink.getRel());
                        link.setHref(discoveryLink.getHref());
                        links.add(link);

                    }
                }

                link = getLink(UrlType.PRINTERINFOREGISTERED, UrlTypeForResponse.PRINTERINFO, printerDataLookUpEntity.getCloudId());
                links.add(link);

            } else {

                link = getLink(UrlType.PRINTERINFO, UrlTypeForResponse.PRINTERINFO, String.format("%s", printerDataLookUpEntity.getDomainIndex()));
                links.add(link);
            }
            printerInfo.setPrinterId(printerDataLookUpEntity.getCloudId());

            link = getLink(UrlType.PRINTERCODE, UrlTypeForResponse.PRINTERCODE, printerDataLookUpEntity.getPrinterCode());
            links.add(link);
        } else {
            link = getLink(UrlType.PRINTERINFO, UrlTypeForResponse.PRINTERINFO,
                    String.format("%s", printerDataLookUpEntity.getDomainIndex()));
            links.add(link);
        }
    }


    private void createLookUpLinks(Boolean isRegistered, PrinterInfo printerInfo, PrinterDataEntity printerDataLookUpEntity) {
        List<Link> links = new ArrayList();
        Link link;
        LinksEntity linksEntity;
        printerInfo.setIsPrinterRegistered(isRegistered);
        link = getLink(UrlType.SNKEYLOOKUP, UrlTypeForResponse.SELF, printerDataLookUpEntity.getSnKey());
        links.add(link);

        buildLinks(isRegistered, printerInfo, printerDataLookUpEntity, links);

        printerInfo.setConfigurations(links);
    }


    public HashMap<String, String> getAvDisAuthHeader() {
        HashMap<String, String> headers = new HashMap();
        headers.put(HttpHeaders.AUTHORIZATION, getPrinterResourceServiceConfig().getAvDisAuthHeader());
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    public Link getLink(UrlType urlType, UrlTypeForResponse urlTypeForResponse, String hrefVal) {
        LinksEntity linksEntity = linksDaoImpl.getLinkUrl(urlType.geturlType());
        Link link = new Link();
        link.setRel(urlTypeForResponse.geturlTypeForResponse());
        if (hrefVal == null)
            link.setHref(linksEntity.getUrlValue());
        else
            link.setHref(String.format(linksEntity.getUrlValue(), hrefVal));

        return link;

    }
}

