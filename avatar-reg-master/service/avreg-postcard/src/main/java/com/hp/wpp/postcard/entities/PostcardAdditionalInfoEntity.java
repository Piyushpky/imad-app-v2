/**
 * 
 */
package com.hp.wpp.postcard.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author mahammad
 *
 */
@Entity
@Table(name = "postcard_additional_info", uniqueConstraints = @UniqueConstraint(columnNames = {"postcard_id", "application_id"}))
public class PostcardAdditionalInfoEntity {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="application_id")
	private String applicationId;
	
	@Column(name="entity_seq_num")
	private long entitySeqNum;
	
	@Column(name="service_seq_num")
	private long serviceSeqNum;
	
	@Column(name="entity_message_id")
	private String entityMessageId;
	
	@Column(name="service_message_id")
	private String serviceMessageId;
	
	@Column(length = 512, name="entity_signature_hash")
	private String entitySignatureHash;
	
	@Column(length = 512, name="service_signature_hash")
	private String serviceSignatureHash;
	
	@Column(length = 512, name="entity_instruction")
	private String entityInstruction;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postcard_id")
	private PostcardEntity postcardEntity;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public long getEntitySeqNum() {
		return entitySeqNum;
	}

	public void setEntitySeqNum(long entitySeqNum) {
		this.entitySeqNum = entitySeqNum;
	}

	public long getServiceSeqNum() {
		return serviceSeqNum;
	}

	public void setServiceSeqNum(long serviceSeqNum) {
		this.serviceSeqNum = serviceSeqNum;
	}

	public String getEntityMessageId() {
		return entityMessageId;
	}

	public void setEntityMessageId(String entityMessageId) {
		this.entityMessageId = entityMessageId;
	}

	public String getServiceMessageId() {
		return serviceMessageId;
	}

	public void setServiceMessageId(String serviceMessageId) {
		this.serviceMessageId = serviceMessageId;
	}

	public String getEntitySignatureHash() {
		return entitySignatureHash;
	}

	public void setEntitySignatureHash(String entitySignatureHash) {
		this.entitySignatureHash = entitySignatureHash;
	}

	public String getServiceSignatureHash() {
		return serviceSignatureHash;
	}

	public void setServiceSignatureHash(String serviceSignatureHash) {
		this.serviceSignatureHash = serviceSignatureHash;
	}

	public PostcardEntity getPostcardEntity() {
		return postcardEntity;
	}

	public void setPostcardEntity(PostcardEntity postcardEntity) {
		this.postcardEntity = postcardEntity;
	}

	public String getEntityInstruction() {
		return entityInstruction;
	}

	public void setEntityInstruction(String entityInstruction) {
		this.entityInstruction = entityInstruction;
	}
}
