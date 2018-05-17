package com.hp.wpp.avatar.framework.processor.data;

public class EntityIdentificationBO {

	private String specVersion;
	
	private String entityId;
	
	private String entityModel;
	
	private String entityUUID;

	private String entityMCID;
	
	private String entityName;
	
	private String entityDomain;
	
	private String countryAndRegionName;
	
	private String language;
	
	private String entityAdditionalIds;
	
	private String entityInfo;
	
	private int resetCounter;
	
	private String originator;
	
	private String entityRevision;
	
	private String entityVersionDate;

	private String entityClassifier;
	private short podCode;
	private String entityType;

	//private String ssn;
	private  String hostIP;

	public String getEntityClassifier() {
		return entityClassifier;
	}

	public void setEntityClassifier(String entityClassifier) {
		this.entityClassifier = entityClassifier;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityModel() {
		return entityModel;
	}

	public void setEntityModel(String entityModel) {
		this.entityModel = entityModel;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityDomain() {
		return entityDomain;
	}

	public void setEntityDomain(String entityDomain) {
		this.entityDomain = entityDomain;
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

	public String getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}

	public int getResetCounter() {
		return resetCounter;
	}

	public void setResetCounter(int resetCounter) {
		this.resetCounter = resetCounter;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
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

	public String getEntityAdditionalIds() {
		return entityAdditionalIds;
	}

	public void setEntityAdditionalIds(String entityAdditionalIds) {
		this.entityAdditionalIds = entityAdditionalIds;
	}

	public String getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}

	public short getPodCode() {
		return podCode;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setPodCode(short podCode) {
		this.podCode = podCode;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityMCID() {
		return entityMCID;
	}

    public void setEntityMCID(String entityMCID) { this.entityMCID = entityMCID; }

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}


/*public String getSSN() {
		return ssn;
	}

	public void setSSN(String ssn) {
		this.ssn = ssn;
	}*/
}
