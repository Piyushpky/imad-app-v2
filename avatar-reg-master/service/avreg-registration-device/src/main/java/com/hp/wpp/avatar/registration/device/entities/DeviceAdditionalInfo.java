package com.hp.wpp.avatar.registration.device.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="device_additional_info")
public class DeviceAdditionalInfo implements Serializable {
	
	private static final long serialVersionUID = 4786182323748418291L;
	
	@Id
	@JoinColumn(name = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EntityDevice device;
	
	@Column(name = "cloud_id")
	private String cloudId;
	
	@Column(name = "entity_revision")
	private String entityRevision;

	@Column(name = "entity_version_date")
	private String entityVersionDate;

	@Column(name = "country_and_region_name")
	private String countryAndRegionName;
	
	@Column(name = "language")
	private String language;
	
	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "spec_version")
	private String specVersion;
	
	@Column(name = "originator")
	private String originator;
	
	@Column(name = "entity_info")
	private String entityInfo;
	
	@Column(name = "entity_additional_ids")
	private String entityAdditionalIds;

	@Column(name = "entity_mcid", nullable = true)
	private String entityMCID;

	public String getEntityMCID() { return entityMCID; }

	public void setEntityMCID(String entityMCID) { this.entityMCID = entityMCID; }
	
	public String getEntityAdditionalIds() {
		return entityAdditionalIds;
	}

	public void setEntityAdditionalIds(String entityAdditionalIds) {
		this.entityAdditionalIds = entityAdditionalIds;
	}

	public String getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}

	public EntityDevice getDevice() {
		return device;
	}

	public void setDevice(EntityDevice device) {
		this.device = device;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getEntityRevision() {
		return entityRevision;
	}

	public void setEntityRevision(String entityRevision) {
		this.entityRevision = entityRevision;
	}

	public String getEntityVersionDate() {
		return entityVersionDate;
	}

	public void setEntityVersionDate(String entityVersionDate) {
		this.entityVersionDate = entityVersionDate;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public String getCountryAndRegionName() {
		return countryAndRegionName;
	}

	public void setCountryAndRegionName(String countryAndRegionName) {
		this.countryAndRegionName = countryAndRegionName;
	}

	public String getLanguage() {
		return language;
	}
}

