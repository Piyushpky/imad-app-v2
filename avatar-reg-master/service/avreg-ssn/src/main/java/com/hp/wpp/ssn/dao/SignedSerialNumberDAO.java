package com.hp.wpp.ssn.dao;

import com.hp.wpp.ssn.entities.SSNEntity;

public interface SignedSerialNumberDAO {

	public void createSSN(SSNEntity ssn);
	
	public void updateSSN(SSNEntity ssn);
	
	public SSNEntity getSSN(String SerialNumber, int domainIndex);
	
	
}
