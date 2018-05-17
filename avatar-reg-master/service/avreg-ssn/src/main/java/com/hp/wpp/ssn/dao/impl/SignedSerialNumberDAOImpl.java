package com.hp.wpp.ssn.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.ssn.dao.SignedSerialNumberDAO;
import com.hp.wpp.ssn.entities.SSNEntity;

@Transactional("ssnTransactionManager")
public class SignedSerialNumberDAOImpl implements SignedSerialNumberDAO {
	
	@PersistenceContext(unitName="ssn")	
	private EntityManager em;

	@Override
	public void createSSN(SSNEntity ssn) {
		em.persist(ssn);
	}

	@Override
	public void updateSSN(SSNEntity ssn) {
		em.merge(ssn);
	}

	@Override
	public SSNEntity getSSN(String securedSerialNumber,int domainIndex) {
		Query query = em.createNamedQuery("getSSN").setParameter("securedSerialNumber", securedSerialNumber).setParameter("domainIndex",domainIndex);
		try {
			return (SSNEntity)query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
}
