package com.hp.wpp.avatar.restapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.cidgenerator.InvalidCloudIdException;
import com.hp.wpp.cidgenerator.ParsedCloudID;


@Component
public class ValidationHelper {
	
	@Autowired
	private CloudIdGenerator cloudIdGenerator;
	
	@Autowired
	private EntityTypeProcessorMap entityTypeProcessorMap;

	public void setEntityTypeProcessorMap(
			EntityTypeProcessorMap entityTypeProcessorMap) {
		this.entityTypeProcessorMap = entityTypeProcessorMap;
	}

	public void setCloudIdGenerator(CloudIdGenerator cloudIdGenerator) {
		this.cloudIdGenerator = cloudIdGenerator;
	}

	public RegisteredEntityBO validateCloudId(String cloudId) throws EntityRegistrationNonRetriableException
			{

		ParsedCloudID parsedCloudID = null;
		try {
			parsedCloudID = cloudIdGenerator.parse(cloudId);
		} catch (InvalidCloudIdException e) {
			throw new EntityValidationException(e);
		}
		;

		int entityTypeInt = (int) parsedCloudID.getEntitytype();
		EntityType entityType = EntityType.getEntityTypeByValue(entityTypeInt);
		EntityRegistrationProcessor registrationProcessor = entityTypeProcessorMap
				.getEntityRegistrationProcessor(entityType);

		return registrationProcessor.getRegisteredEntity(cloudId);

	}

}
