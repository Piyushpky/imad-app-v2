package com.hp.wpp.avatar.registration.device.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.device.entities.ServiceInstance;

@Transactional(value="podDeviceTransactionManager")
@Repository
public interface DeviceServiceInstanceRepository extends CrudRepository<ServiceInstance, Long>{
	
	public ServiceInstance findByServiceTypeAndSpecVersion(String serviceType, String specVersion);
}
