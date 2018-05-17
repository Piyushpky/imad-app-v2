package com.hp.wpp.avatar.registration.device.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.registration.device.entities.ServiceInstance;

public class ServiceInstanceRepositoryTest extends BaseRepositoryTest{
	
	@Autowired
	private DeviceServiceInstanceRepository deviceServiceInstanceRepository;
	
	@Test
	public void testGetServiceInstance(){
		ServiceInstance serviceInstance = deviceServiceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue(), "1.0");
		Assert.assertNotNull(serviceInstance);
	}

}
