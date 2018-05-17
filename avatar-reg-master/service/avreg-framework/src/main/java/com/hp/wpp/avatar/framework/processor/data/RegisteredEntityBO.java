package com.hp.wpp.avatar.framework.processor.data;


public class RegisteredEntityBO {
	
	private String cloudId;
	
	private String entityId;
	
	private String entityModel;
	
	private String entityUUID;
	
	private int resetCounter;
	
	private String entityDomain;
	
	private String entityName;
	
	private String entityType;
	
	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
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

	public int getResetCounter() {
		return resetCounter;
	}

	public void setResetCounter(int resetCounter) {
		this.resetCounter = resetCounter;
	}

	public String getEntityDomain() {
		return entityDomain;
	}

	public void setEntityDomain(String entityDomain) {
		this.entityDomain = entityDomain;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}
