package com.hp.wpp.avatar.registration.device.repository;

import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(value="podDeviceTransactionManager")
@Repository
public interface HashedEntityIdentifierRepository extends CrudRepository<HashedEntityIdentifier, Long>{
	HashedEntityIdentifier findByEntityIdentifier(String entityIdentifier);
	HashedEntityIdentifier findByHashedEntityIdentifier(String hashedEntityIdentifier);
}
