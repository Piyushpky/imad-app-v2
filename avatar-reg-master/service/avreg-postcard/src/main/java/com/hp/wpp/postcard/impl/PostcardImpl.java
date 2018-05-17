/**
 * 
 */
package com.hp.wpp.postcard.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.InitiateKeyNegotiationException;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.instruction.PostcardInstructionGenerator;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardContent.PostcardSignedInfo;
import com.hp.wpp.postcard.json.schema.PostcardCreator;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.json.schema.PostcardMessage;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.key.generator.DKGenerator;
import com.hp.wpp.postcard.key.generator.IntegrityValidator;
import com.hp.wpp.postcard.key.generator.PostcardSignature;
import com.hp.wpp.postcard.key.generator.SecretGenerator;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.service.PostcardCryptoService;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.validator.PostcardValidator;

/**
 * @author mahammad
 *
 */
public class PostcardImpl implements Postcard {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardImpl.class);
	
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private SecretGenerator secretGenerator;
	@Autowired
	private DKGenerator dkGenerator;
	@Autowired
	private PostcardCryptoService postcardCryptoService;
	@Autowired
	private IntegrityValidator integrityValidator;
	@Autowired
	private PostcardValidator postcardValidator;
	@Autowired
	private PostcardInstructionGenerator postcardInstructionGenerator;
	@Autowired
	private PostcardConfig postcardConfig;
	@Autowired
	private PostcardCipher postcardCipher;

	@Override
	public PostcardData validateAndDecryptPostcard(String postcard) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		long postcardCreationOrReceivedTime = startTime;
		PostcardEntity postcardEntity = null;
		PostcardContent postcardContent = null;
		String entityId = null;
		LOG.debug("postcard validation and decryption started with payload: {}", postcard);
		try {
			LOG.debug("Json Parsing.");
			postcardContent = postcardParser.parseAndValidatePostcard(postcard);
			entityId = postcardContent.getPostcardSignedInfo().getEntityId();
			LOG.debug("postcard validation and retrieve shared secret.");

			postcardEntity = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
			postcardValidator.validatePostcardAdditionalInfo(postcardContent, postcardEntity);
			Date postcardCreationDate = PostcardUtility.parsePostcardDate(postcardContent.getPostcardSignedInfo().getTimestamp());
			postcardCreationOrReceivedTime = postcardCreationDate.getTime();
			LOG.debug("Derived Key generation.");
			byte[] postcardId = Base64.decodeBase64(postcardContent.getPostcardSignedInfo().getPostcardId());
			LOG.debug("postcardId received: {} of length: {} bytes", PostcardUtility.returnFormattedText(postcardId), postcardId.length);
			byte[] dk = dkGenerator.generateDK(postcardEntity.getSecret(), postcardId);
			LOG.debug("Integrity check.");
			integrityCheck(postcardContent, dk);
			// decrypt contents
			LOG.debug("Content decryption.");
			PostcardData postcardData = postcardCryptoService.decryptAndDecompressMessageContents(postcardContent.getPostcardSignedInfo().getVersion(), postcardContent.getPostcardSignedInfo().getMessages(), dk);
			
			//if entity instruction is received, it will decrypt and store.
			PostcardData entityInstructionData = checkAndDecryptPostcardEntityInstruction(postcardContent, dk);
			String entityInstruction = null;
			if(entityInstructionData != null && entityInstructionData.getMessages().size() > 0) {
				entityInstruction = Base64.encodeBase64String(entityInstructionData.getMessages().get(0).getContent());
			}
			
			checkRenegotiationRequestAndRefreshSecret(postcardContent, postcard);
			
			// storing additional info in database.
			// only after successful decryption of content.. additional info we are persisting.
			byte[] encryptedSecret = postcardCipher.encrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV());
			postcardEntity.setSecret(encryptedSecret);
			postcardValidator.populateAndStorePostcardAdditionalInfo(postcardEntity, postcardContent, Creator.getCreator(postcardContent.getPostcardSignedInfo().getCreator().value()), false, entityInstruction);
			postcardData.setApplicationType(ApplicationType.getApplicationType(Integer.parseInt(postcardContent.getPostcardSignedInfo().getApplicationId())));
			postcardData.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
			postcardData.setVersion(postcardContent.getPostcardSignedInfo().getVersion());
			return postcardData;
			
		} catch (Exception e) {
			if(e instanceof InitiateKeyNegotiationException) {
			LOG.debug("Generating key negotiation request payload..");
				if(!StringUtils.isBlank(entityId)) {
					PostcardData postcardData = new PostcardData();
					postcardData.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
					postcardData.setVersion(postcardContent.getPostcardSignedInfo().getVersion());
					postcardData.setApplicationType(ApplicationType.AVATAR_REGISTRATION);
					String serviceInstructionPostcard = postcardInstructionGenerator.generateServiceInstruction(postcardData);
					InitiateKeyNegotiationException ikne = new InitiateKeyNegotiationException();
					ikne.setKeyNegotiationRequestPostcard(serviceInstructionPostcard);
					throw ikne;
				} else {
					throw new KeyCertificateException("Enough information not found for generating service instruction command for key negotiation request.");
				}
			}
			throw e;
		} finally {
			LOG.debug("Time took for postcard decryption: {} msec. \nTime took for postcard to reach server: {} msec. ", (System.currentTimeMillis() - startTime)/1000, (startTime - postcardCreationOrReceivedTime)/1000);
		}
	}
	
	@Override
	public String encryptPostcard(PostcardData postcardData) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		LOG.debug("Encrypting postcard started..");
		try {
			validatePostcardData(postcardData);
			// retrieve ask
			PostcardEntity postcardEntity = secretGenerator.getSecret(postcardData.getEntityId());
			// generate DK
			byte[] postcardId = PostcardUtility.generateRandomBytes(PostcardConstants.POSTCARD_ID_LENGTH);
			LOG.debug("PostcardId generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(postcardId), postcardId.length);
			byte[] dk = dkGenerator.generateDK(postcardEntity.getSecret(), postcardId);
			// encrypt content
			PostcardContent postcardContent = new PostcardContent();
			populatePostcard(postcardData, postcardEntity, postcardContent);
			postcardContent.getPostcardSignedInfo().setPostcardId(Base64.encodeBase64String(postcardId));
			List<PostcardMessage> postcardMessages = postcardCryptoService.encryptAndCompressMessageContents(postcardContent.getPostcardSignedInfo().getVersion(), postcardData, dk);
			postcardContent.getPostcardSignedInfo().getMessages().addAll(postcardMessages);
			// additional info update
			postcardEntity.setSecret(postcardCipher.encrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
			postcardValidator.populateAndStorePostcardAdditionalInfo(postcardEntity, postcardContent, Creator.SERVICE, false, null);
			// generate hash and signatures
			integrityFieldsGeneration(postcardContent, dk);
			// serialize
			return postcardParser.serialize(postcardContent);
		} finally {
			LOG.debug("Time took for postcard encryption: {} msec.", (System.currentTimeMillis() - startTime)/1000);
		}
	}
	
	@Override
	public String generateEntityKey(String cloudId, String entityId, String applicationId) throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		return secretGenerator.generateEntityKey(cloudId, entityId, applicationId);
	}

	@Override
	public boolean isValidKey(String cloudId, String entityId, String applicationId, String entityKey) throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		String postcardEntityKey = secretGenerator.generateEntityKey(cloudId, entityId, applicationId);
		if(postcardEntityKey.equals(entityKey)) {
			LOG.debug("keys are valid.");
			return true;
		}
		return false;
	}
	
	protected void validatePostcardData(PostcardData postcardData) throws PostcardJSONCorruptedException {
		Validator valid = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<PostcardData>> validator = valid.validate(postcardData);
		if(!validator.isEmpty()) {
			throw new PostcardJSONCorruptedException("Mandatory params missing in generated postcard.");
		}
	}
	
	protected void populatePostcard(PostcardData postcardData, PostcardEntity postcardEntity, PostcardContent postcardContent) {
		PostcardContent.PostcardSignedInfo signedInfo = null;
		if(postcardContent.getPostcardSignedInfo() != null) {
			signedInfo = postcardContent.getPostcardSignedInfo();
		} else {
			signedInfo = new PostcardSignedInfo();
		}
		
		if(StringUtils.isBlank(postcardData.getVersion()))
			signedInfo.setVersion(PostcardConstants.POSTCARD_VERSION);
		else
			signedInfo.setVersion(postcardData.getVersion());
		
		signedInfo.setMimeType(PostcardConstants.POSTCARD_MIME_TYPE);
		signedInfo.setCreator(PostcardCreator.valueOf(Creator.SERVICE.getCreatorName()));
		signedInfo.setApplicationId(String.valueOf(postcardData.getApplicationType().getApplicationId()));
		signedInfo.setKeyId(postcardEntity.getKeyId());
		signedInfo.setTimestamp(PostcardUtility.getDate());
		signedInfo.setEntityId(postcardData.getEntityId());
		signedInfo.setSeqNum(new BigInteger("1"));
		postcardContent.setPostcardSignedInfo(signedInfo);
	}
	
	/**
	 * @param postcardContent
	 * @param dk
	 * @throws PostcardException 
	 */
	protected void integrityCheck(PostcardContent postcardContent, byte[] dk) throws PostcardNonRetriableException {
		// TODO: Need to check better way of doing.. during key negotiation
		// integrity check will be done by different algo
		if("1.0".equals(postcardContent.getPostcardSignedInfo().getVersion()) && postcardContent.getPostcardSignedInfo().getKeys().size() <= 0) {
			PostcardSignature postcardSignature = null;
			for (PostcardContent.Signatures signature : postcardContent.getSignatures()) {
				if (postcardContent.getPostcardSignedInfo().getKeyId().equals(signature.getKeyId())) {
					postcardSignature = new PostcardSignature();
					postcardSignature.setHash(postcardContent.getHash());
					postcardSignature.setSignature(signature.getSignature());
					if ("1.0".equals(postcardContent.getPostcardSignedInfo().getVersion()))
						postcardSignature.setSignatureScheme(PostcardSignatureScheme.hmac_sha256.name());
					else if ("1.1".equals(postcardContent.getPostcardSignedInfo().getVersion()))
						postcardSignature.setSignatureScheme(signature.getSignatureScheme().name());
				}
			}
			if(postcardSignature != null)
				integrityValidator.validateHashAndSignature(postcardContent, getMAK(dk), postcardSignature);
			else
				LOG.error("Postcard signature verification not happend.");
		}
	}
	
	protected void integrityFieldsGeneration(PostcardContent postcardContent, byte[] dk) throws PostcardNonRetriableException {
		PostcardSignature postcardSignature = integrityValidator.generateHashAndSignatures(postcardContent, getMAK(dk));
		postcardContent.setHash(postcardSignature.getHash());
		PostcardContent.Signatures signatures = new PostcardContent.Signatures();
		signatures.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
		signatures.setSignature(postcardSignature.getSignature());
		if(!StringUtils.isBlank(postcardSignature.getSignatureScheme()) && "1.1".equals(postcardContent.getPostcardSignedInfo().getVersion())) {
			signatures.setSignatureScheme(PostcardSignatureScheme.fromValue(postcardSignature.getSignatureScheme()));
		}
		postcardContent.getSignatures().add(signatures);
	}
	
	private byte[] getMAK(byte[] derivedKey) throws PostcardNonRetriableException {
		byte[] mak = new byte[PostcardConstants.MAK_LENGTH];
		mak = Arrays.copyOfRange(derivedKey, 0, PostcardConstants.MAK_LENGTH);
		LOG.debug("MAK generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(mak), mak.length);
		return mak;
	}
	
	private PostcardData checkAndDecryptPostcardEntityInstruction(PostcardContent postcardContent, byte[] dk) throws PostcardNonRetriableException {
		if(postcardContent.getPostcardSignedInfo().getControl() != null) {
			LOG.debug("Entity instruction receivied in postcard, now decrypting postcard instruction.");
			return postcardCryptoService.decryptAndDecompressInstruction(postcardContent.getPostcardSignedInfo().getVersion(), postcardContent.getPostcardSignedInfo().getControl(), dk);
		}
		return null;
	}
	
	/**
	 * It checks whether postcard is for renegotiation, if yes refresh the secret
	 * 
	 * @param postcardContent
	 * @throws PostcardException
	 */
	private void checkRenegotiationRequestAndRefreshSecret(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		boolean isRefreshSecretRequest = false;
		if(postcardContent.getPostcardSignedInfo().getKeys() != null && postcardContent.getPostcardSignedInfo().getKeys().size() > 0) {
			for (PostcardKey postcardKey : postcardContent.getPostcardSignedInfo().getKeys()) {
				// if the runtime postcard has keys.. means client is requesting for refreshing the secret with renegotiation postcard.
				if(!postcardContent.getPostcardSignedInfo().getKeyId().equalsIgnoreCase(postcardKey.getPostcardSecretKeyId())) {
					isRefreshSecretRequest = true;
				}
			}
		}
		
		if(isRefreshSecretRequest) {
			LOG.debug("Postcard has renegotiation request with all new keys, trying to generated new shared secret and refresh..");
			try {
				PostcardEntity postcardEntity = secretGenerator.generateAndStoreSecret(postcardContent, postcardJson);
				LOG.debug("Derived Key generation.");
				byte[] postcardId = Base64.decodeBase64(postcardContent.getPostcardSignedInfo().getPostcardId());
				LOG.debug("postcardId received: {} of length: {} bytes", PostcardUtility.returnFormattedText(postcardId), postcardId.length);
				byte[] dk = dkGenerator.generateDK(postcardEntity.getSecret(), postcardId);
				doIntegrityCheckDuringRenegotiation(postcardContent, dk);
				LOG.debug("postcard shared secret has been updated successfully for entity={}", postcardContent.getPostcardSignedInfo().getEntityId());
			} catch (PostcardNonRetriableException pe) {
				LOG.warning("Unable to do postcard secret refesh, so next postcard will be generated using old shared secret for entity={}; failureReason={}",postcardContent.getPostcardSignedInfo().getEntityId(), pe.getMessage());
			}
			LOG.debug("postcard secret has refreshed for entity: {} with new keys", postcardContent.getPostcardSignedInfo().getEntityId());
		}
	}
	
	private void doIntegrityCheckDuringRenegotiation(PostcardContent postcardContent, byte[] dk) throws PostcardNonRetriableException {
		// TODO: Need to check better way of doing.. during key negotiation integrity check will be done by different algo

		PostcardSignature postcardSignature = null;
		for (PostcardContent.Signatures signature : postcardContent.getSignatures()) {
			if (!postcardContent.getPostcardSignedInfo().getKeyId().equals(signature.getKeyId())) {
				postcardSignature = new PostcardSignature();
				postcardSignature.setSignature(signature.getSignature());
				postcardSignature.setHash(postcardContent.getHash());
				if ("1.0".equals(postcardContent.getPostcardSignedInfo().getVersion()))
					postcardSignature.setSignatureScheme(PostcardSignatureScheme.hmac_sha256.name());
				else
					postcardSignature.setSignatureScheme(signature.getSignatureScheme().name());
			}
		}
		if(postcardSignature != null)
			integrityValidator.validateHashAndSignature(postcardContent, getMAK(dk), postcardSignature);
		else
			throw new PostcardNonRetriableException("Postcard signature verification not happened.");
	}

	@Override
	public void refreshSharedSecret(String postcard) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		long postcardCreationOrReceivedTime = startTime;
		PostcardEntity postcardEntity = null;
		LOG.debug("postcard validation and decryption started with payload: {}", postcard);
		try {
			LOG.debug("Json Parsing.");
			PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(postcard);
			LOG.debug("postcard validation and retrieve shared secret.");
			postcardEntity = secretGenerator.retrieveAndStoreSecret(postcardContent, postcard);
			
			// No need to validate additional info like seq number ..etc.. because refresh shared secret will reset the seq number
//			postcardValidator.validatePostcardAdditionalInfo(postcardContent, postcardEntity);
			Date postcardCreationDate = PostcardUtility.parsePostcardDate(postcardContent.getPostcardSignedInfo().getTimestamp());
			postcardCreationOrReceivedTime = postcardCreationDate.getTime();
			LOG.debug("Derived Key generation.");
			byte[] postcardId = Base64.decodeBase64(postcardContent.getPostcardSignedInfo().getPostcardId());
			byte[] dk = dkGenerator.generateDK(postcardEntity.getSecret(), postcardId);
			LOG.debug("Integrity check.");
			integrityCheck(postcardContent, dk);
			// decrypt contents
			LOG.debug("Content decryption.");
			PostcardData postcardData = postcardCryptoService.decryptAndDecompressMessageContents(postcardContent.getPostcardSignedInfo().getVersion(), postcardContent.getPostcardSignedInfo().getMessages(), dk);
			postcardData.setApplicationType(ApplicationType.getApplicationType(Integer.parseInt(postcardContent.getPostcardSignedInfo().getApplicationId())));
			postcardData.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
			postcardData.setVersion(postcardContent.getPostcardSignedInfo().getVersion());
			postcardEntity.setSecret(postcardCipher.encrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
			postcardValidator.populateAndStorePostcardAdditionalInfo(postcardEntity, postcardContent, Creator.getCreator(postcardContent.getPostcardSignedInfo().getCreator().value()), true, null);
			postcardValidator.storePostcardRenegotiationInfo(postcardEntity, postcardData);
		} finally {
			LOG.debug("Time took for postcard decryption: {} msec. \nTime took for postcard to reach server: {} msec. ", (System.currentTimeMillis() - startTime)/1000, (startTime - postcardCreationOrReceivedTime)/1000);
		}
	}
}
