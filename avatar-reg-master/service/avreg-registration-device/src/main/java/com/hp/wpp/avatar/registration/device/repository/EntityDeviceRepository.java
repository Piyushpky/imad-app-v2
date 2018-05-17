package com.hp.wpp.avatar.registration.device.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.DeviceAdditionalInfo;

@Transactional(value="podDeviceTransactionManager")
@Repository
public interface EntityDeviceRepository extends CrudRepository<EntityDevice,Long>, EntityDeviceDao {

	public static final String GET_DEVICEADDITIONALINFO_BY_CLOUDID = "select i from DeviceAdditionalInfo i where i.cloudId= ?1";
	public static final String UPDATE_DEVICE_ADDITIONAL_INFO = "update DeviceAdditionalInfo i set i.entityRevision = :entityRevision, i.entityVersionDate = :entityVersionDate, i.countryAndRegionName = :countryAndRegionName, i.language = :language, i.specVersion = :specVersion, i.originator = :originator, i.entityAdditionalIds = :entityAdditionalIds, i.entityInfo = :entityInfo,i.entityMCID = :entityMCID  where i.cloudId = :cloudId";
	public static final String GET_DEVICE_WITH_ADDITIONAL_INFO_BY_CLOUDID = "select d from EntityDevice d JOIN FETCH d.deviceAdditionalInfo where d.cloudId= ?1";

	public EntityDevice findByCloudId(String cloudId);
	
	public EntityDevice findByEntityIdAndEntityModel(String entityId, String entityModel);
	
    @Query(value = GET_DEVICEADDITIONALINFO_BY_CLOUDID)
	public DeviceAdditionalInfo retrieveDeviceAdditionalInfoByCloudId(String cloudId);
    
    @Modifying(clearAutomatically = true)
    @Query(value = UPDATE_DEVICE_ADDITIONAL_INFO)
	public void updateDeviceAdditionalInfo(@Param("cloudId") String cloudId,
			@Param("entityRevision") String entityRevision,
			@Param("entityVersionDate") String entityVersionDate, @Param("countryAndRegionName") String countryAndRegionName, @Param("language") String language,
			@Param("specVersion") String specVersion, @Param("originator") String originator, @Param("entityAdditionalIds") String entityAdditionalIds,
			@Param("entityInfo") String entityInfo, @Param("entityMCID")String entityMCID);

	@Query(value = GET_DEVICE_WITH_ADDITIONAL_INFO_BY_CLOUDID)
	EntityDevice getDeviceWithDeviceAdditionalInfo(String cloudId);

}

