package com.hp.wpp.avatar.registration.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.service.entities.ServiceInstance;

@Transactional(value="podServiceTransactionManager")
@Repository
public interface ServiceInstanceRepository extends CrudRepository<ServiceInstance, Long>{

	public ServiceInstance findByServiceTypeAndSpecVersion(String serviceType, String specVersion);
}
