package com.hp.wpp.avatar.restapp.security;

import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;

public interface EntityValidator {

	/**
	 * Method to validate AuthHeader or Message
	 * @param cloudId
	 * @param applicationId
	 * @param header
	 * @param body
	 * @return RegisteredEntityBO
	 */
	public RegisteredEntityBO validate(String cloudId, String applicationId, ValidatorBean validatorBean) throws EntityRegistrationNonRetriableException;
}
