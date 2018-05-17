package com.hp.wpp.avatar.framework.processor;

import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;

public interface EntityRegistrationProcessor {
	
	EntityConfigurationBO registerEntity(EntityIdentificationBO entityIdentification) throws EntityRegistrationNonRetriableException;
	
	RegisteredEntityBO getRegisteredEntity(String cloudId) throws  EntityRegistrationNonRetriableException;

	void validateEntityIdentificationBO(EntityIdentificationBO entityIdentificationBO)  throws EntityRegistrationNonRetriableException;
	
	EntityIdentificationBO getEntityIdentificationBO(String cloudId) throws EntityRegistrationNonRetriableException;
}
