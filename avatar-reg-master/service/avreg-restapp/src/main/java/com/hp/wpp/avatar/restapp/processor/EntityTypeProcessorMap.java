package com.hp.wpp.avatar.restapp.processor;

import java.util.Map;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EnumMapException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;


public class EntityTypeProcessorMap {

	private Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap;

	public void setEntityTypeToProcessorMap(Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap) {
		this.entityTypeToProcessorMap = entityTypeToProcessorMap;
	}
	
	public EntityRegistrationProcessor getEntityRegistrationProcessor(EntityType entityType){
		EntityRegistrationProcessor entityRegistrationProcessor = entityTypeToProcessorMap.get(entityType);
		if(entityRegistrationProcessor == null){
			throw new EnumMapException("No processor found for entityType: {}"+entityType);
		}
		return entityRegistrationProcessor;
	}
	
}
