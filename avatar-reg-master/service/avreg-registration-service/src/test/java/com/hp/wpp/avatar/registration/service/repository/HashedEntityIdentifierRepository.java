package com.hp.wpp.avatar.registration.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;

@Transactional(value="podServiceTransactionManager")
@Repository
public interface HashedEntityIdentifierRepository extends CrudRepository<HashedEntityIdentifier, Long>{
	public HashedEntityIdentifier findByEntityIdentifier(String entityIdentifier);
}
