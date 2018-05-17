package com.hp.wpp.avatar.registration.service.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="entity_service")
public class EntityService {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long entityServiceId;
	
	@Column(name="cloud_id")
	private String cloudId;
	
	@Column(name="entity_id")
	private String entityId;
	
	@Column(name="entity_model")
	private String entityModel;
	
	@Column(name="entity_uuid")
	private String entityUUID;
	
	@Column(name="reset_counter")
	private int resetCounter;
	
	@Column(name="entity_domain")
	private String entityDomain;
	
	@Column(name="entity_name")
	private String entityName;
	
	// Actual relation is OneToOne mapping. To achieve the lazy loading behavior, it is defined as OneToMany @ JPA level.
	@OneToMany(mappedBy = "entityService", cascade= {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private Set<ServiceAdditionalInfo> serviceAdditionalInfo;

	public long getEntityServiceId() {
		return entityServiceId;
	}

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

	public ServiceAdditionalInfo getServiceAdditionalInfo() {
		if(this.serviceAdditionalInfo!=null)
			return serviceAdditionalInfo.iterator().next();
		else
			return null;
	}

	public void setServiceAdditionalInfo(ServiceAdditionalInfo serviceAdditionalInfo) {
		if(this.serviceAdditionalInfo == null){
			synchronized (this) {
				if(this.serviceAdditionalInfo == null){
					this.serviceAdditionalInfo=new HashSet<ServiceAdditionalInfo>(1);
				}
			}
		}else {
			this.serviceAdditionalInfo.clear();
		}
		this.serviceAdditionalInfo.add(serviceAdditionalInfo);
	}

	public String getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}
}
