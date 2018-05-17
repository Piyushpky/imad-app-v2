package com.hp.wpp.avatar.restmodel.json.schema;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hp.wpp.avatar.framework.enums.EntityClassifier;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonPropertyOrder({ "version", "entity_id", "entity_model", "entity_name", "entity_version", "entity_type", "country_and_region_name", "language", "entity_additional_ids" })
public class EntityInformation {

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
    private EntityInformation.EntityVersion entityVersion;

    @NotNull
    @JsonProperty("entity_type")
    protected EntityType entityType;

    @JsonProperty("entity_classifier")
    protected EntityClassifier entityClassifier;

    @NotNull
    @JsonProperty("country_and_region_name")
    protected Country countryAndRegionName;

    @JsonProperty("language")
    protected Language language;

    @JsonProperty("entity_additional_ids")
    private List<EntityAdditionalId> entityAdditionalIds;

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

    public EntityInformation.EntityVersion getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(EntityInformation.EntityVersion entityVersion) {
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

    public List<EntityAdditionalId> getEntityAdditionalIds() {
        return entityAdditionalIds;
    }

    public void setEntityAdditionalIds(List<EntityAdditionalId> entityAdditionalIds) {
        this.entityAdditionalIds = entityAdditionalIds;
    }

    @JsonPropertyOrder({ "revision", "date"})
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
}
