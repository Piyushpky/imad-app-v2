package com.hp.wpp.avatar.registration.device.entities;

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
import javax.persistence.Table;

@Entity
@Table(name = "entity_device")
public class EntityDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cloud_id")
    private String cloudId;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_model")
    private String entityModel;

    @Column(name = "entity_uuid")
    private String entityUUID;

    public String getEntityUUID() {
        return entityUUID;
    }

    public void setEntityUUID(String entityUUID) {
        this.entityUUID = entityUUID;
    }

    @Column(name = "reset_counter")
    private int resetCounter;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_domain")
    private String entityDomain;

    //Actual relation is OneToOne mapping. To achieve the lazy loading behavior, it is defined as OneToMany @ JPA level.
    @OneToMany(mappedBy = "device", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<DeviceAdditionalInfo> deviceAdditionalInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DeviceAdditionalInfo getDeviceAdditionalInfo() {
        if (this.deviceAdditionalInfo != null)
            return deviceAdditionalInfo.iterator().next();
        else
            return null;

    }

    public void setDeviceAdditionalInfo(DeviceAdditionalInfo deviceAdditionalInfoItem) {
        if (this.deviceAdditionalInfo == null) {
            synchronized (this) {
                if (this.deviceAdditionalInfo == null) {
                    this.deviceAdditionalInfo = new HashSet<DeviceAdditionalInfo>(1);
                }
            }
        } else {
            this.deviceAdditionalInfo.clear();
        }
        this.deviceAdditionalInfo.add(deviceAdditionalInfoItem);
    }

}

