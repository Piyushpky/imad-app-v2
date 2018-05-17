package com.hp.wpp.avatar.registration.device.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.device.repository.EntityDeviceDao;

@Transactional(value = "podDeviceTransactionManager")
@Repository
public class EntityDeviceRepositoryImpl implements EntityDeviceDao {

	@PersistenceContext(unitName = "pod_device")
	private EntityManager em;

	/*
	 * The entity device table entry is persisted
	 */
	@Override
	public void persistEntityDevices(EntityDevice device) {
		em.persist(device);
	}

	/*
	 * The hashed entity identifier table entry is persisted
	 */
	@Override
	public void persistHashedIdentifier(HashedEntityIdentifier entityIdentifier){
		em.persist(entityIdentifier);
	}

}
