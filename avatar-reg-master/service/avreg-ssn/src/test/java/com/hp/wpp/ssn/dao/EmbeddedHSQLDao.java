package com.hp.wpp.ssn.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.hp.wpp.ssn.entities.PrinterRegistrationDomain;

@Transactional
public class EmbeddedHSQLDao {
	
	@PersistenceContext	
	private EntityManager entityManager;

	public void createPrinterRegistrationDomain(
			PrinterRegistrationDomain registrationDomain) {
		
		entityManager.persist(registrationDomain);

	}
}
