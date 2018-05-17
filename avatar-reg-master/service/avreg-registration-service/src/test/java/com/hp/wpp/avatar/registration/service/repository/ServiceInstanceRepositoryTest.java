package com.hp.wpp.avatar.registration.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.registration.service.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.service.repository.ServiceInstanceRepository;

public class ServiceInstanceRepositoryTest extends BaseRepositoryTest{
	
	@Autowired
	private ServiceInstanceRepository serviceInstanceRepository;
	
	@Test
	public void testGetServiceInstance(){
		ServiceInstance serviceInstance = serviceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue(), "1.0");
		Assert.assertNotNull(serviceInstance);
	}

}
