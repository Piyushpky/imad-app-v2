package com.hp.wpp.avatar.registration.device.repository;

import com.hp.wpp.avatar.registration.device.entities.domain.RegistrationDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RegistrationDomainRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RegistrationDomainRepository registrationDomainRepository;

    @BeforeClass
    public void init(){
		//org.hsqldb.util.DatabaseManagerSwing.main(new String[] { "--url","jdbc:hsqldb:mem:pod_device", "--noexit" });
    }

    @Test
    public void testGetRegistrationDomainByName() throws Exception{
        RegistrationDomain domain = registrationDomainRepository.findByRegistrationDomain("test_domain");
        Assert.assertEquals(domain.getDomainIndex(),0);
    }
}
