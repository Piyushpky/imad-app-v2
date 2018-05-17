package com.hp.wpp.avatar.registration.service.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.ServiceAdditionalInfo;

@Transactional(value="podServiceTransactionManager")
@Repository
public interface EntityServiceRepository extends CrudRepository<EntityService,Long>, EntityServiceDao {

	public static final String GET_SERVICEADDITIONALINFO_BY_CLOUDID = "select i from service_additional_info i where i.cloudId= ?1";
	public static final String UPDATE_SERVICE_ADDITIONAL_INFO = "update service_additional_info i set i.entityRevision = :entityRevision, i.entityVersionDate = :entityVersionDate, i.countryAndRegionName = :countryAndRegionName, i.language = :language, i.specVersion = :specVersion, i.originator = :originator, i.entityAdditionalIds = :entityAdditionalIds, i.entityInfo = :entityInfo  where i.cloudId = :cloudId";

	public EntityService findByCloudId(String cloudId);
	
	public EntityService findByEntityIdAndEntityModel(String entityId, String entityModel);
	
    @Query(value = GET_SERVICEADDITIONALINFO_BY_CLOUDID)
	public ServiceAdditionalInfo retrieveServiceAdditionalInfoByCloudId(String cloudId);
    
    @Modifying(clearAutomatically = true)
    @Query(value = UPDATE_SERVICE_ADDITIONAL_INFO)
	public void updateServieAdditionalInfo(@Param("cloudId") String cloudId,
			@Param("entityRevision") String entityRevision,
			@Param("entityVersionDate") String entityVersionDate, @Param("countryAndRegionName") String countryAndRegionName, @Param("language") String language,
			@Param("specVersion") String specVersion, @Param("originator") String originator, @Param("entityAdditionalIds") String entityAdditionalIds,
			@Param("entityInfo") String entityInfo);
	
}
