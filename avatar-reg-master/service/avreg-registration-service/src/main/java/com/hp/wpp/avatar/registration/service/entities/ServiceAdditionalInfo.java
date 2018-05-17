package com.hp.wpp.avatar.registration.service.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity(name="service_additional_info")
public class ServiceAdditionalInfo implements Serializable{

	private static final long serialVersionUID = -3563702642229801237L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private EntityService entityService;
	
	@Column(name = "cloud_id")
	private String cloudId;

	@Column(name = "country_and_region_name")
	private String countryAndRegionName;

	@Column(name = "language")
	private String language;

	@Column(name = "entity_revision")
	private String entityRevision;

	@Column(name = "entity_version_date")
	private String entityVersionDate;

	@Column(name = "spec_version")
	private String specVersion;

	@Column(name = "originator")
	private String originator;
	
	@Column(name = "entity_additional_ids")
	private String entityAdditionalIds;
	
	@Column(name = "entity_info")
	private String entityInfo;

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
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

	public void setLanguage(String language) {
		this.language = language;
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

	public void setSpecVersion(String version) {
		this.specVersion = version;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

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
}
