package com.hp.wpp.avatar.restapp.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

@Component("authValidator")
public class AuthValidator implements EntityValidator {

	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(AuthValidator.class);

	@Autowired
	private AvatarApplicationConfig avatarApplicationConfig;

	@Autowired
	private Postcard postcard;

	@Autowired
	private ValidationHelper validationHelper;
	
	public void setPostcard(Postcard postcard) {
		this.postcard = postcard;
	}
	
	public void setAvatarApplicationConfig(
			AvatarApplicationConfig avatarApplicationConfig) {
		this.avatarApplicationConfig = avatarApplicationConfig;
	}
	
	public void setValidationHelper(
			ValidationHelper validationHelper) {
		this.validationHelper = validationHelper;
	}

	@Override
	public RegisteredEntityBO validate(String cloudId, String applicationId,
			ValidatorBean validatorBean)
			throws EntityRegistrationNonRetriableException {

		String[] authParts = validatorBean.getHeader().split("\\s+");
		String authInfo = authParts[1];

		String decodedAuth = new String(Base64.decodeBase64(authInfo));
		String auth[] = decodedAuth.split(":");
		if (auth.length != 2)
			throw new EntityValidationException(
					"custom auth header do not have expected no.of fields");

		String pCloudId = auth[0];
		String entityKey = auth[1];

		if (!cloudId.equals(pCloudId))
			throw new EntityValidationException("url cloudId=" + cloudId
					+ " and header cloudId=" + pCloudId + " mismatch");

		RegisteredEntityBO registeredEntity = validationHelper
				.validateCloudId(cloudId);

		try {
			boolean validateKey = postcard.isValidKey(cloudId,
					registeredEntity.getEntityUUID(), applicationId, entityKey);
			if (!validateKey)
				throw new EntityValidationException(
						"postcard validation failed with cloudId=" + cloudId);

		} catch (PostcardNonRetriableException e) {
			throw new EntityValidationException("Postcard exception while validating request for cloudId="+cloudId,e);
		}

		return registeredEntity;
	}
}