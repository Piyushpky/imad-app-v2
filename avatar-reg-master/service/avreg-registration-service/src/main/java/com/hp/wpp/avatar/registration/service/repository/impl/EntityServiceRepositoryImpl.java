package com.hp.wpp.avatar.registration.service.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.service.repository.EntityServiceDao;

@Transactional(value="podServiceTransactionManager")
@Repository
public class EntityServiceRepositoryImpl implements EntityServiceDao{
	
	@PersistenceContext(unitName="pod_service")
	private EntityManager em;
	
	/*
	 *  These two table entries needs to be persisted in a single transaction.
	 */
	@Override
	public void persistEntityServices(EntityService service, HashedEntityIdentifier entityIdentifier){
		em.persist(service);
		em.persist(entityIdentifier);
	}
}
