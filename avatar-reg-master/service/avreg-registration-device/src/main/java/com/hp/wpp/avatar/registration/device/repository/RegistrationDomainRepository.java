package com.hp.wpp.avatar.registration.device.repository;

import com.hp.wpp.avatar.registration.device.entities.domain.RegistrationDomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value="podDeviceTransactionManager")
@Repository
public interface RegistrationDomainRepository extends CrudRepository<RegistrationDomain,Long> {

    String GET_REGISTRATION_DOMAIN__BY_NAME = "select d from RegistrationDomain d where d.entityDomain= ?1";

    @Query(value = GET_REGISTRATION_DOMAIN__BY_NAME)
    RegistrationDomain findByRegistrationDomain(String entityDomain);
}
