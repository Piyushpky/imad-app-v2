package com.hp.wpp.ssnclaim.service.ssn.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.hp.wpp.discovery.client.DiscoveryClient;
import com.hp.wpp.discovery.client.data.DiscoveryResponse;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;

import com.hp.wpp.ssnc.common.config.PrinterResourceServiceConfig;
import com.hp.wpp.ssnc.common.enums.UrlType;
import com.hp.wpp.ssnc.common.enums.UrlTypeForResponse;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.exception.*;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ClaimInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ResponseUUID;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.printercode.impl.PrinterCodeValidatorImpl;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNValidatorData;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.dao.impl.PrinterDataLookUpDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;


import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;

import javax.annotation.PostConstruct;

public class DeviceLookUpServiceImpl implements DeviceLookUpService {

    private static final WPPLogger logger = WPPLoggerFactory
            .getLogger(DeviceLookUpServiceImpl.class);

    private PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl;

    private RegistrationDomainDaoImpl registrationDomainService;

    private PrinterCodeValidatorImpl printerCodeImpl;

    private Map<Integer, String> domainMap;

    private final String VERSION = "1.0";

    @Autowired
    private LookUpResponseGenerator lookUpResponse;

    @Autowired
    public DeviceLookUpServiceImpl(
            RegistrationDomainDaoImpl registrationDomainDaoImpl, PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl, PrinterCodeValidatorImpl printerCodeImpl) {
        this.registrationDomainService = registrationDomainDaoImpl;
        this.printerDataLookUpDaoImpl = printerDataLookUpDaoImpl;
        this.printerCodeImpl = printerCodeImpl;
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

    @Autowired
    private LinksDaoImpl linksDaoImpl;

    public LinksDaoImpl getLinksDaoImpl() {
        return linksDaoImpl;
    }

    @PostConstruct
    public void initDeviceLookup() {
        List<RegistrationDomainEntity> domainList = registrationDomainService.getRegistrationDomainEntity();
        if (domainList == null)
            logger.error("failed to load the domain map from dynamoDB");
        domainMap = new HashMap();
        for (RegistrationDomainEntity entity : domainList) {
            domainMap.put(entity.getDomainIndex(), entity.getDomainKey());
        }
    }

    public RegistrationDomainDaoImpl getRegistrationDomainService() {
        return registrationDomainService;
    }

    public void setRegistrationDomainService(
            RegistrationDomainDaoImpl registrationDomainService) {
        this.registrationDomainService = registrationDomainService;
    }

    public PrinterDataLookUpDaoImpl getPrinterDataLookUpDaoImpl() {
        return printerDataLookUpDaoImpl;
    }

    public void setPrinterDataLookUpDaoImpl(
            PrinterDataLookUpDaoImpl printerDataLookUpDaoImpl) {
        this.printerDataLookUpDaoImpl = printerDataLookUpDaoImpl;
    }

    public PrinterDataEntity processPrinterCodeData(PrinterCodeData ssnFields) {
        PrinterServiceHelper printerServiceHelper = new PrinterServiceHelper();
        String snKey = printerServiceHelper.createSNKey(
                ssnFields.getSerialNumber(), ssnFields.getDomainIndex());
        logger.debug("The sn key generated is {}", snKey);
        PrinterDataEntity printerClaim = getPrinterData(snKey);
        printerClaim = createOrUpadtePrinter(snKey, ssnFields, printerClaim);
        return printerClaim;
    }

    public void processPrinter(RegisterPrinter regDeviceClaim) {
        SSNValidatorData validatorData = new SSNValidatorData();
        validatorData.setDomainIndex(regDeviceClaim.getDomainIndex());
        validatorData.setSerialNumber(regDeviceClaim.getEntityId());

        logger.debug("SSN Validation Successful for Domain Index {}",
                regDeviceClaim.getDomainIndex());
        PrinterServiceHelper printerServiceHelper = new PrinterServiceHelper();
        String snKey = printerServiceHelper.createSNKey(
                regDeviceClaim.getEntityId(), regDeviceClaim.getDomainIndex());
        logger.debug("The sn key generated is {}", snKey);
        storePrintertoDB(snKey, regDeviceClaim);

    }

    public PrinterDataEntity snKeyValidation(String snKey) {
        PrinterDataEntity sSNPrinterClaimEntity = printerDataLookUpDaoImpl
                .getPrinterDataLookUpEntity(snKey);
        if (sSNPrinterClaimEntity == null) {
            throw new SNKeyNotFoundException(String.format(
                    "SN key not there in database with snkey:%s", snKey));
        }
        return sSNPrinterClaimEntity;
    }

    @Override
    public PrinterDataEntity getPrinterCodeData(String printerId, String claimCode) {
        PrinterDataEntity printerDataLookUpEntity = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByCloudID(printerId);
        if (printerDataLookUpEntity == null) {
            logger.debug("cloudId not found in database: {}", printerId);
            throw new CloudIdNotFoundException();
        } else {
            PrinterDataEntity sSNPrinterClaimEntity = printerDataLookUpDaoImpl
                    .getPrinterDataLookUpEntity(claimCode);
            if (sSNPrinterClaimEntity == null) {
                throw new InvalidClaimCodeException(String.format(
                        "SN key not there in database with snkey:%s", claimCode));
            }
            if (!printerId.equals(sSNPrinterClaimEntity.getCloudId())) {
                logger.debug("cloudId not matching: {}", printerId);
                throw new ValidationDataMismatchException("cloudId not matching");
            }

            return sSNPrinterClaimEntity;
        }
    }

    @Override
    public PrinterDataEntity getPrinterCodeData(PrinterCodeData ssnFields) {
        PrinterServiceHelper printerServiceHelper = new PrinterServiceHelper();
        String snKey = printerServiceHelper.createSNKey(
                ssnFields.getSerialNumber(), ssnFields.getDomainIndex());
        logger.debug("The sn key generated is {}", snKey);

        PrinterDataEntity printerClaim = snKeyValidation(snKey);
        return printerClaim;

    }

    @Override
    public ClaimInfo createClaimResponse(String printerId) {
        ClaimInfo claimInfo = new ClaimInfo();
        PrinterDataEntity printerDataLookUpEntity = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByCloudID(printerId);
        if (printerDataLookUpEntity == null) {
            logger.debug("cloudId not found in database: {}", printerId);
            throw new CloudIdNotFoundException();
        } else {

            ArrayList<Link> links = new ArrayList<>();
            claimInfo.setVersion("1.0.0");
            claimInfo.setClaimCode(printerDataLookUpEntity.getSnKey());
            Link link = lookUpResponse.getLink(UrlType.CLAIMCODE, UrlTypeForResponse.SELF, printerDataLookUpEntity.getCloudId());
            links.add(link);
            String href = printerId + "/" + printerDataLookUpEntity.getSnKey();
            link = lookUpResponse.getLink(UrlType.CLAIMCODEVALIDATION, UrlTypeForResponse.CLAIMCODEVALIDATION, href);
            links.add(link);
            claimInfo.setConfigurations(links);
            if (printerDataLookUpEntity.getIsRegistered() == null || !printerDataLookUpEntity.getIsRegistered()) {
                printerDataLookUpEntity.setIsRegistered(true);
                printerDataLookUpDaoImpl.createPrinterDataLookUp(printerDataLookUpEntity);
            }
        }

        return claimInfo;
    }

    @Override
    public void deletePrinterCodeData(PrinterCodeData printerCodeData) {
        PrinterServiceHelper printerServiceHelper = new PrinterServiceHelper();
        String snKey = printerServiceHelper.createSNKey(
                printerCodeData.getSerialNumber(), printerCodeData.getDomainIndex());
        logger.debug("The sn key generated is {}", snKey);
        PrinterDataEntity printerClaim = snKeyValidation(snKey);
        printerDataLookUpDaoImpl.deletePrinterDataLookUpEntity(printerClaim);
    }

    @Override
    public PrinterCodeData validatePrinterCode(String code) {
        return printerCodeImpl.validatePrinterCode(code, domainMap);
    }

    public SSNFields
    validateSSNCode(String code) {
        SSNValidatorData validatorData = PrinterServiceHelper.parseSSN(code);
        SSNFields ssnFields = PrinterServiceHelper
                .createSSNFields(validatorData);
        ssnFields.setSsn(code);

        byte[] actualSignatureFragments = PrinterServiceHelper
                .generateSignatureFragments(
                        validatorData,
                        getRegistrationDomainKey(validatorData.getDomainIndex()));
        byte[] expectedFragments = PrinterServiceHelper
                .getExpectedFragments(validatorData.getHeader());

        if (!Arrays.equals(expectedFragments, actualSignatureFragments)) {
            throw new InvalidHeaderSignatureException(
                    "ssn signature validation failed");
        }
        return ssnFields;
    }

    private byte[] getRegistrationDomainKey(int domainIndex) {
        RegistrationDomainEntity regDomainEntity = registrationDomainService
                .getRegDomainKey(domainIndex);
        String domainRegKey = regDomainEntity.getDomainKey();
        return domainRegKey.getBytes();
    }

    private PrinterDataEntity getPrinterData(String snKey) {
        PrinterDataEntity printerDataLookUpEntity = null;
        printerDataLookUpEntity = printerDataLookUpDaoImpl
                .getPrinterDataLookUpEntity(snKey);
        return printerDataLookUpEntity;
    }

    private PrinterDataEntity createOrUpadtePrinter(String snKey,
                                                    PrinterCodeData printerCodeFields,
                                                    PrinterDataEntity printerDataLookUpEntity) {
        if (printerDataLookUpEntity == null) {
            printerDataLookUpEntity = new PrinterDataEntity(snKey,
                    printerCodeFields.getDomainIndex(),
                    printerCodeFields.getInstantInkFlag(), null);
            printerDataLookUpEntity.setIssuanceCounter(printerCodeFields
                    .getIssuanceCounter());
            printerDataLookUpEntity.setPrinterCode(printerCodeFields
                    .getPrinterCode());
            printerDataLookUpEntity.setOverRunBit(printerCodeFields
                    .getOverrunBit());
            printerDataLookUpEntity.setOwnershipCounter(printerCodeFields
                    .getOwnership());
            printerDataLookUpDaoImpl
                    .createPrinterDataLookUp(printerDataLookUpEntity);
        } else {
            printerDataLookUpEntity.setInkCapable(printerCodeFields
                    .getInstantInkFlag()); // added to add the ink status after
            // reg
            printerDataLookUpEntity.setIssuanceCounter(printerCodeFields
                    .getIssuanceCounter());
            printerDataLookUpEntity.setPrinterCode(printerCodeFields
                    .getPrinterCode());
            printerDataLookUpEntity.setOverRunBit(printerCodeFields
                    .getOverrunBit());
            printerDataLookUpEntity.setOwnershipCounter(printerCodeFields
                    .getOwnership());
            printerDataLookUpDaoImpl
                    .createPrinterDataLookUp(printerDataLookUpEntity);
        }
        return printerDataLookUpEntity;
    }

    private void storePrintertoDB(String snKey, RegisterPrinter regDeviceClaim) {
        PrinterDataEntity printerDataLookUpEntity = null;
        printerDataLookUpEntity = printerDataLookUpDaoImpl
                .getPrinterDataLookUpEntity(snKey);
        if (printerDataLookUpEntity == null) {
            printerDataLookUpEntity = new PrinterDataEntity(snKey,
                    regDeviceClaim.getDomainIndex(), false,
                    regDeviceClaim.getCloudId());
            printerDataLookUpEntity.setIsRegistered(false);
            printerDataLookUpEntity.setdeviceUUID(regDeviceClaim.getdeviceUUID());
            printerDataLookUpDaoImpl
                    .createPrinterDataLookUp(printerDataLookUpEntity);
            /*
			 * Commenting the null check for the Reregistration scenarios to
			 * work If such cases, the same SN key will be mapped to different
			 * printer ids
			 */
        } else {
            printerDataLookUpEntity.setCloudId(regDeviceClaim.getCloudId());
            printerDataLookUpEntity.setIsRegistered(false);
            printerDataLookUpEntity.setdeviceUUID(regDeviceClaim.getdeviceUUID());
            logger.debug("Updating device claim with printerId");
            printerDataLookUpDaoImpl
                    .createPrinterDataLookUp(printerDataLookUpEntity);
        }
    }

    @Override
    public ResponseUUID getPrinterInfo(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new InvalidUUIDException();
        }
        else{
            ResponseUUID responseUUID = new ResponseUUID();
            PrinterDataEntity printerDataLookUpEntity = printerDataLookUpDaoImpl.getPrinterDataLookUpEntityByUUID(uuid);
            String cloudId = printerDataLookUpEntity.getCloudId();
            if (StringUtils.isBlank(cloudId)) {
                logger.debug("Cloud ID not found in database for uuid: {}", uuid);
                throw new InternalServerErrorException("Cloud ID not found in database");
            } else {
                responseUUID.setCpid(cloudId);
                // Fetching URL's from Discovery Service
                int retryLimit = 0;
                int delayBetweenRetriesInSecs = 2;
                DiscoveryResponse discoveryResponse = getDiscoveryServiceClient().getDiscoveryResponse(cloudId, lookUpResponse.getAvDisAuthHeader(), retryLimit, delayBetweenRetriesInSecs);
                List<Link> links = new ArrayList();
                for (com.hp.wpp.discovery.client.data.Link discoveryLink : discoveryResponse.getLinks()) {
                    if (discoveryLink.getRel().equals(UrlTypeForResponse.IPP.geturlTypeForResponse())) {
                        Link link = new Link();
                        link.setRel(discoveryLink.getRel());
                        link.setHref(discoveryLink.getHref());
                        links.add(link);
                    }
                    
                    if (discoveryLink.getRel().equals(UrlTypeForResponse.DATABRIDGE.geturlTypeForResponse())) {
                        Link link = new Link();
                        link.setRel(discoveryLink.getRel());
                        link.setHref(discoveryLink.getHref());
                        links.add(link);
                    }
                }
                responseUUID.setLinks(links);
                responseUUID.setVersion(VERSION);
            }
            return responseUUID;
        }
    }
}
