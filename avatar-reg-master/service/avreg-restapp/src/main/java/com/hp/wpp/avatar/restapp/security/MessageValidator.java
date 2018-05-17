package com.hp.wpp.avatar.restapp.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRequestException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restmodel.json.schema.MessageValidationEntity;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

@Component("messageValidator")
public class MessageValidator implements EntityValidator {

	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(MessageValidator.class);

	@Autowired
	private Postcard postcard;

	@Autowired
	private ValidationHelper validationHelper;
	
	public void setPostcard(Postcard postcard) {
		this.postcard = postcard;
	}
	
	public void setValidationHelper(
			ValidationHelper validationHelper) {
		this.validationHelper = validationHelper;
	}


	@Override
	public RegisteredEntityBO validate(String cloudId, String applicationId,
			ValidatorBean validatorBean)
			throws EntityRegistrationNonRetriableException {

		MessageValidationEntity messageEntity = (MessageValidationEntity) unmarshal(
				MessageValidationEntity.class, validatorBean.getMessgae());
		validateMandatoryParamsMessage(messageEntity);
		if(!cloudId.equals(messageEntity.getCloudId())||!applicationId.equals(messageEntity.getApplicationId()))
		{
			throw new InvalidRequestException("URL cloud-id or application-id mismatch with payload");
		}

		RegisteredEntityBO entityDevice = validationHelper
				.validateCloudId(cloudId);
		if (entityDevice == null) {
			throw new EntityValidationException(
					"Entity not registered with cloud-id = " + cloudId);
		}

		String printerKey = null;
		try {
			printerKey = postcard.generateEntityKey(cloudId,
					entityDevice.getEntityUUID(), applicationId);
		} catch (PostcardNonRetriableException e) {
			throw new EntityValidationException("Postcard exception while generating key for cloud-id = "+cloudId+" and application-id = "+applicationId,e);
		}
		byte[] message = Base64.decodeBase64(messageEntity.getMessage());
		byte[] calcSignature = encryptHMAC(message, printerKey.getBytes());
		byte[] recvSignature = Base64
				.decodeBase64(messageEntity.getSignature());

		if (Arrays.equals(calcSignature, recvSignature)) {
			LOG.debug(
					"Successful Message Validation for cloud-id = {} and  application-id = {}",
					cloudId, applicationId);
			return entityDevice;
		} else
			throw new EntityValidationException(
					"Message Validation Failure for cloud-id = " + cloudId
							+ "and  application-id = " + applicationId);
	}

	private byte[] encryptHMAC(byte[] content, byte[] key)
			throws EntityValidationException {
		byte[] encryptedData = null;
		Mac mac;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key,
					PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			encryptedData = mac.doFinal(content);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new EntityValidationException("Exception while generating hash for message validation",e);
		}
		return encryptedData;
	}

	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}

	public <T> Object unmarshal(Class<T> pojoClass, String jsonText)
			throws InvalidRequestException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonText, pojoClass);
		} catch (IOException e) {
			throw new InvalidRequestException("Exception while parsing json string for message validation",e);
		}
	}

	public <T> String marshal(T object) throws InvalidRequestException {
		try {
			ObjectWriter writter = getObjectWriter();
			return writter.writeValueAsString(object);
		} catch (IOException e) {
			throw new InvalidRequestException("Exception while serializing to json string for message validation",e);
		}
	}

	protected ObjectWriter getObjectWriter() throws IOException {
		return new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY)
				.writer().withDefaultPrettyPrinter();
	}

	protected void validateMandatoryParamsMessage(
			MessageValidationEntity messageEntity)
			throws InvalidRequestException {
		Validator valid = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<MessageValidationEntity>> validator = valid
				.validate(messageEntity);
		if (!validator.isEmpty()) {
			throw new InvalidRequestException("Mandatory params missing");
		}
	}

}
