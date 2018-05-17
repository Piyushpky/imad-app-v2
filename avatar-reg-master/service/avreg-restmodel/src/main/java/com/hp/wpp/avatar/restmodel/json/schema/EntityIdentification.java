package com.hp.wpp.avatar.restmodel.json.schema;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import org.hibernate.validator.constraints.NotBlank;

import com.hp.wpp.avatar.framework.enums.EntityClassifier;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;

public class EntityIdentification {

	@NotBlank
	@JsonProperty("version")
	protected String version;

	@NotBlank
	@JsonProperty("entity_id")
	protected String entityId;

	@NotBlank
	@JsonProperty("entity_model")
	protected String entityModel;

	@NotBlank
	@JsonProperty("entity_name")
	protected String entityName;

	@Valid
	@JsonProperty("entity_version")
	private EntityIdentification.EntityVersion entityVersion;

	@NotNull
	@JsonProperty("entity_type")
	protected EntityType entityType;
	
	@JsonProperty("entity_classifier")
	protected EntityClassifier entityClassifier;

	@NotBlank
	@JsonProperty("entity_domain")
	protected String entityDomain;

	@NotNull
	@JsonProperty("country_and_region_name")
	protected Country countryAndRegionName;

	@JsonProperty("language")
	protected Language language;

	@JsonProperty("entity_additional_ids")
	private List<EntityAdditionalId> entityAdditionalIds;

	@JsonProperty("entity_info")
	private List<EntityInfo> entityInfo;

	@Min(value = 0)
	@JsonProperty("reset_counter")
	protected int resetCounter;

	@JsonProperty("request_originator")
	private String originator;

	@JsonProperty("hostIP")
	private String hostIP;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public EntityIdentification.EntityVersion getEntityVersion() {
		return entityVersion;
	}

	public void setEntityVersion(EntityIdentification.EntityVersion entityVersion) {
		this.entityVersion = entityVersion;
	}

	public EntityType getEntityType() {
		return entityType;
	}
	

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public EntityClassifier getEntityClassifier() {
		return entityClassifier;
	}
	
	public void setEntityClassifier(EntityClassifier entityClassifier) {
		this.entityClassifier = entityClassifier;
	}
	
	public String getEntityDomain() {
		return entityDomain;
	}

	public void setEntityDomain(String entityDomain) {
		this.entityDomain = entityDomain;
	}

	public Country getCountryAndRegionName() {
		return countryAndRegionName;
	}

	public void setCountryAndRegionName(Country countryAndRegionName) {
		this.countryAndRegionName = countryAndRegionName;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
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

	public List<EntityAdditionalId> getEntityAdditionalIds() {
		return entityAdditionalIds;
	}

	public void setEntityAdditionalIds(List<EntityAdditionalId> entityAdditionalIds) {
		this.entityAdditionalIds = entityAdditionalIds;
	}

	public List<EntityInfo> getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(List<EntityInfo> entityInfo) {
		this.entityInfo = entityInfo;
	}

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public static class EntityVersion {

		@JsonProperty
		protected String revision;

		@JsonProperty
		protected String date;

		public String getRevision() {
			return revision;
		}

		public void setRevision(String value) {
			this.revision = value;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String value) {
			this.date = value;
		}

		@Override
		public String toString() {
			return "[ version=" + revision + ", date=" + date + "]";
		}
	}
	
	public static class EntityAdditionalId{
		
		@JsonProperty("id_type")
		protected String idType;
		
		@JsonProperty("id_value")
		protected String idValue;

		public String getIdType() {
			return idType;
		}

		public void setIdType(String idType) {
			this.idType = idType;
		}

		public String getIdValue() {
			return idValue;
		}

		public void setIdValue(String idValue) {
			this.idValue = idValue;
		}
		
	}
	
	public static class EntityInfo{
		
		@JsonProperty("info_type")
		protected String infoType;
		
		@JsonProperty("info_value")
		protected String infoValue;

		public String getInfoType() {
			return infoType;
		}

		public void setInfoType(String infoType) {
			this.infoType = infoType;
		}

		public String getInfoValue() {
			return infoValue;
		}

		public void setInfoValue(String infoValue) {
			this.infoValue = infoValue;
		}
		
	}
	

}
