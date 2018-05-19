package com.hp.wpp.ssnclaim.it.mock;


import com.hp.wpp.mock.dynamodb.http.DynamoDBHttpMockService;
import com.hp.wpp.ssnclaim.dao.impl.LinksDaoImpl;
import com.hp.wpp.ssnclaim.dao.impl.RegistrationDomainDaoImpl;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundharb on 6/17/2016.
 */
public class DeviceLookupDynamoMock {

    @Autowired
    private RegistrationDomainDaoImpl registrationDomainDaoImpl;

    @Autowired
    private LinksDaoImpl linksDaoImpl;

    @Autowired
    private DynamoDBHttpMockService dynamoDBHttpMockService;

    public DeviceLookupDynamoMock(){
        System.out.println("DeviceLookupDynamoMock created");
    }

    @PostConstruct
    public void setup() {
        System.out.println("DeviceLookupDynamoMock -- in setup");
        dynamoDBHttpMockService.start();

        //Create tables as many as required
        List<Class> classes = new ArrayList<>();
        classes.add(PrinterDataEntity.class);
        classes.add(RegistrationDomainEntity.class);
        classes.add(LinksEntity.class);
        try {
            dynamoDBHttpMockService.createTable(classes);
        }catch (Throwable t){
            t.printStackTrace();
        }

        this.loadRegistrationDomains();
        this.loadLinks();
    }

    @PreDestroy
    public void teardown(){
        //Stop mock service
        dynamoDBHttpMockService.stop();
    }

    public void loadRegistrationDomains(){
        System.out.println("Loading registration domains ------------------------------------------------------");
        RegistrationDomainEntity registrationDomainEntity = new RegistrationDomainEntity();
        registrationDomainEntity.setDomainIndex(0);
        registrationDomainEntity.setDomainKey("1R+YeaCiPI94hYbqZIBFeSpAquJODAQZw8m9OpY2iOg=");
        registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
        registrationDomainEntity = new RegistrationDomainEntity();
        registrationDomainEntity.setDomainIndex(1);
        registrationDomainEntity.setDomainKey("1R+YeaCiPI94hYbqZIBFeSpAquJODAQZw8m9OpY2iOg=");//testDomainKey");
        registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
        registrationDomainEntity.setDomainIndex(36);
        registrationDomainEntity.setDomainKey("1R+YeaCiPI94hYbqZIBFeSpAquJODAQZw8m9OpY2iOg=");//testDomainKey");
        registrationDomainDaoImpl.createRegistrationDomainEntity(registrationDomainEntity);
    }

    public void loadLinks(){
        System.out.println("Loading links ------------------------------------------------------------");
        LinksEntity emailUrl = new LinksEntity("email_address_url","http://localhost/nexus/");
        LinksEntity printerInfo = new LinksEntity("printer_info","http://localhost/virtualprinter/v1/printers/generic/info/%s");
        LinksEntity printerInfoReg = new LinksEntity("printer_info_registered","http://localhost/virtualprinter/v1/printers/info/%s");
        LinksEntity snKeyLookUp = new LinksEntity("sn_key_lookup","http://localhost/virtualprinter/v1/printer?SN_key=%s");
        LinksEntity ssnClaim = new LinksEntity("ssn_claim","http://localhost/virtualprinter/v1/SSN_info/%s");
        LinksEntity cloudConfigUrl = new LinksEntity("cloud_config_url","http://localhost/virtualprinter/v1/printers/%s/cloudconfig");
        LinksEntity discoveryUrl = new LinksEntity("discovery_url","https://localhost/avatar/v1/entities?cloud-id=%s");
        LinksEntity printerCode=new LinksEntity("printer_code","https://localhost/virtualprinter/v1/printer_code_info/%s");
        LinksEntity claimCode=new LinksEntity("claim_code","https://localhost/virtualprinter/v1/printer/claim_code/%s");
        LinksEntity claimCodeValidation=new LinksEntity("claim_code_validation","https://localhost/virtualprinter/v1/printer/claim_code/%s");
        linksDaoImpl.createLinkUrl(emailUrl);
        linksDaoImpl.createLinkUrl(printerInfo);
        linksDaoImpl.createLinkUrl(printerInfoReg);
        linksDaoImpl.createLinkUrl(snKeyLookUp);
        linksDaoImpl.createLinkUrl(ssnClaim);
        linksDaoImpl.createLinkUrl(cloudConfigUrl);
        linksDaoImpl.createLinkUrl(discoveryUrl);
        linksDaoImpl.createLinkUrl(printerCode);
        linksDaoImpl.createLinkUrl(claimCode);
        linksDaoImpl.createLinkUrl(claimCodeValidation);

    }
}
