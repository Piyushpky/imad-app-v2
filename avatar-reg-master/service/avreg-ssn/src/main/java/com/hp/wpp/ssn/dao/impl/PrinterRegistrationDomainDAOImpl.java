package com.hp.wpp.ssn.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.ssn.dao.PrinterRegistrationDomainDAO;

@Transactional("ssnTransactionManager")
public class PrinterRegistrationDomainDAOImpl implements
		PrinterRegistrationDomainDAO {
	
	@PersistenceContext(unitName="ssn")	
	private EntityManager entityManager;

	@Override
	public String getPrinterRegistrationDomainKey(String registrationDomain) {
		Query query = entityManager.createNamedQuery("getDomainKeyByName").setParameter("domainName", registrationDomain);
		try {
			return (String)query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public String getPrinterRegistrationDomainKey(int ssnIndex) {
		Query query = entityManager.createNamedQuery("getDomainKeyBySSNIndex").setParameter("ssnIndex", ssnIndex);
		return (String)query.getSingleResult();
	}
}

