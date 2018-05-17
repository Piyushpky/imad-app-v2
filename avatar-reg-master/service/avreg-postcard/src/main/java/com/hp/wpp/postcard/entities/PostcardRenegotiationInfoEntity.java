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
@Table(name = "postcard_renegotiation_info", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "postcard_id", "application_id"}))
public class PostcardRenegotiationInfoEntity {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="application_id")
	private String applicationId;
	
	@Column(name="credential_refresh_info")
	private byte[] credentialRefreshInfo;
	
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

	public byte[] getCredentialRefreshInfo() {
		return credentialRefreshInfo;
	}

	public void setCredentialRefreshInfo(byte[] credentialRefreshInfo) {
		this.credentialRefreshInfo = credentialRefreshInfo;
	}

	public PostcardEntity getPostcardEntity() {
		return postcardEntity;
	}

	public void setPostcardEntity(PostcardEntity postcardEntity) {
		this.postcardEntity = postcardEntity;
	}
}
