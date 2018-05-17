package com.hp.wpp.avatar.registration.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name="hashed_entity_identifier")
public class HashedEntityIdentifier {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="hashed_entity_identifier")
	private String hashedEntityIdentifier;
	
	@Column(name="entity_identifier")
	private String entityIdentifier;
	
	public HashedEntityIdentifier() {
	}
	
	public HashedEntityIdentifier(String entityIdentifier, String hashedEntityIdentifier) {
		this.entityIdentifier = entityIdentifier ;
		this.hashedEntityIdentifier = hashedEntityIdentifier;
	}
	

	public String getHashedEntityIdentifier() {
		return hashedEntityIdentifier;
	}

	public void setHashedEntityIdentifier(String hashedEntityIdentifier) {
		this.hashedEntityIdentifier = hashedEntityIdentifier;
	}

	public String getEntityIdentifier() {
		return entityIdentifier;
	}

	public void setEntityIdentifier(String entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}
	
}
