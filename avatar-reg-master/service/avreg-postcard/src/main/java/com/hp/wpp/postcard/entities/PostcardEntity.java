/**
 * 
 */
package com.hp.wpp.postcard.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author mahammad
 *
 */
@Entity
@Table(name = "postcard", uniqueConstraints={@UniqueConstraint(columnNames = {"entity_id"})})
public class PostcardEntity {
	
	@Id
	@Column(name="postcard_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long postcardId;
	
	@Column(name="entity_id")
	private String entityId;
	
	@Column(name="key_id")
	private String keyId;
	
	@Column(name="shared_secret")
	private byte[] secret;

	@Column(name="key_agreement_scheme")
	private String keyAgreementScheme;
	
	@OneToMany(mappedBy = "postcardEntity", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<PostcardAdditionalInfoEntity> postcardAdditionalInfoEntities;
	
	@OneToMany(mappedBy = "postcardEntity", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<PostcardRenegotiationInfoEntity> postcardRenogotiationInfoEntities;
	
	/**
	 * @return the keyAgreementScheme
	 */
	public String getKeyAgreementScheme() {
		return keyAgreementScheme;
	}

	/**
	 * @param keyAgreementScheme the keyAgreementScheme to set
	 */
	public void setKeyAgreementScheme(String keyAgreementScheme) {
		this.keyAgreementScheme = keyAgreementScheme;
	}

	/**
	 * @return the deviceId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the keyId
	 */
	public String getKeyId() {
		return keyId;
	}

	/**
	 * @param keyId the keyId to set
	 */
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return the id
	 */
	public long getPostcardId() {
		return postcardId;
	}

	/**
	 * @param id the id to set
	 */
	public void setPostcardId(long id) {
		this.postcardId = id;
	}

	/**
	 * @return the ask
	 */
	public byte[] getSecret() {
		return secret;
	}

	/**
	 * @param ask the ask to set
	 */
	public void setSecret(byte[] secret) {
		this.secret = secret;
	}

	public List<PostcardAdditionalInfoEntity> getPostcardAdditionalInfoEntities() {
		return postcardAdditionalInfoEntities;
	}

	public void setPostcardAdditionalInfoEntities(List<PostcardAdditionalInfoEntity> postcardAdditionalInfoEntities) {
		this.postcardAdditionalInfoEntities = postcardAdditionalInfoEntities;
	}

	public List<PostcardRenegotiationInfoEntity> getPostcardRenogotiationInfoEntities() {
		return postcardRenogotiationInfoEntities;
	}

	public void setPostcardRenogotiationInfoEntities(
			List<PostcardRenegotiationInfoEntity> postcardRenogotiationInfoEntities) {
		this.postcardRenogotiationInfoEntities = postcardRenogotiationInfoEntities;
	}
	
}
