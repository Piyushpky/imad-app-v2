/**
 * 
 */
package com.hp.wpp.postcard.client;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.common.PostcardEnums.EnvironmentType;
import com.hp.wpp.postcard.common.PostcardEnums.KeyAgreementScheme;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.impl.PostcardImpl;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardCreator;
import com.hp.wpp.postcard.json.schema.PostcardMessage;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.key.generator.DKGenerator;
import com.hp.wpp.postcard.key.generator.IntegrityValidator;
import com.hp.wpp.postcard.key.generator.KeyNegotiator;
import com.hp.wpp.postcard.key.generator.PostcardSignature;
import com.hp.wpp.postcard.key.generator.SecretGenerator;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.service.PostcardCryptoService;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.validator.PostcardValidator;

/**
 * This class has implementation for creating the postcards for the clients
 * 
 * @author mahammad
 *
 */
public class PostcardClientImpl extends PostcardImpl implements PostcardClient {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardClientImpl.class);
	
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private KeyNegotiator keyNegotiator;
	@Autowired
	private DKGenerator dkGenerator;
	@Autowired
	private PostcardCryptoService postcardCryptoService;
	@Autowired
	private IntegrityValidator integrityValidator;
	@Autowired
	private PostcardValidator postcardValidator;
	@Autowired
	private SecretGenerator secretGenerator;
	@Autowired
	private PostcardCipher postcardCipher;
	@Autowired
	private PostcardConfig postcardConfig;
	
	@Override
	public String encryptPostcard(PostcardData postcardData, Creator creator) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		LOG.debug("Encrypting postard started..");
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
			postcardContent.getPostcardSignedInfo().setCreator(PostcardCreator.valueOf(creator.getCreatorName()));
			postcardContent.getPostcardSignedInfo().setPostcardId(Base64.encodeBase64String(postcardId));
			List<PostcardMessage> postcardMessages = postcardCryptoService.encryptAndCompressMessageContents(postcardData.getVersion(), postcardData, dk);
			postcardContent.getPostcardSignedInfo().getMessages().addAll(postcardMessages);
			// additional info update
			postcardEntity.setSecret(postcardCipher.encrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
			postcardValidator.populateAndStorePostcardAdditionalInfo(postcardEntity, postcardContent, creator, false, null);
			// generate hash and signatures
			integrityFieldsGeneration(postcardContent, dk);
			// serialize
			return postcardParser.serialize(postcardContent);
		} finally {
			LOG.debug("Time took for postcard encryption: {} msec.", (System.currentTimeMillis() - startTime)/1000);
		}
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.client.PostcardClient#generatePostcardForKeyNegotiation(com.hp.wpp.postcard.PostcardData)
	 */
	@Override
	public String generatePostcardForKeyNegotiation(PostcardData postcardData, ClientType clientType, Creator creator, EnvironmentType environmentType) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		PostcardEntity postcardEntity = null;
		byte[] salt = null;
		PostcardContent postcardContent = new PostcardContent();
		LOG.debug("Generating postcard for key negotiaton started, for entityId; {}", postcardData.getEntityId());
		try {
			validatePostcardData(postcardData);
			// retrieve ask
			salt = PostcardUtility.generateRandomBytes(PostcardConstants.POSTCARD_SALT_SIZE);
			LOG.trace("Salt generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(salt), salt.length);
			postcardContent = keyNegotiator.generateKeysAndSignatures(postcardData.getVersion(), salt, clientType, environmentType);
			postcardEntity = new PostcardEntity();
			postcardEntity.setEntityId(postcardData.getEntityId());
			postcardEntity.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
			byte[] sharedSecret = PostcardUtility.generateSHA256Hash(salt, PostcardConstants.POSTCARD_SECRET_KEY_SIZE);
			LOG.debug("Shared Secret generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(sharedSecret), sharedSecret.length);
			postcardEntity.setSecret(postcardCipher.encrypt(sharedSecret, postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
			postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());

			// generate DK
			byte[] postcardId = PostcardUtility.generateRandomBytes(PostcardConstants.POSTCARD_ID_LENGTH);
			LOG.debug("PostcardId generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(postcardId), postcardId.length);
			byte[] dk = dkGenerator.generateDK(sharedSecret, postcardId);
			// encrypt content
			populatePostcard(postcardData, postcardEntity, postcardContent);
			postcardContent.getPostcardSignedInfo().setCreator(PostcardCreator.valueOf(creator.getCreatorName()));
			postcardContent.getPostcardSignedInfo().setPostcardId(Base64.encodeBase64String(postcardId));
			List<PostcardMessage> postcardMessages = postcardCryptoService.encryptAndCompressMessageContents(postcardData.getVersion(), postcardData, dk);
			postcardValidator.populateAndStorePostcardAdditionalInfo(postcardEntity, postcardContent, creator, false, null);
			postcardContent.getPostcardSignedInfo().getMessages().addAll(postcardMessages);
			
			// generate hash and signatures
			integrityFieldsGenerationForKeyNegotiation(postcardContent, dk, clientType);
			if("1.1".equals(postcardContent.getPostcardSignedInfo().getVersion()))
				integrityFieldsGeneration(postcardContent, dk);
			
			// serialize
			return postcardParser.serialize(postcardContent);
		} finally {
			LOG.debug("Time took for postcard encryption with key negotiation data: {} msec.", (System.currentTimeMillis() - startTime)/1000);
		}
	}
	
	@Override
	public PostcardData validateAndDecryptInstruction(String postcard) throws PostcardNonRetriableException {
		long startTime = System.currentTimeMillis();
		long postcardCreationOrReceivedTime = startTime;
		PostcardEntity postcardEntity = null;
		LOG.debug("postcard validation and decryption started with payload: {}", postcard);
		try {
			LOG.debug("Json Parsing.");
			PostcardContent postcardContent = postcardParser.parseAndValidatePostcard(postcard);
			LOG.debug("postcard validation and retrieve shared secret.");
			postcardEntity = secretGenerator.getSecret(postcardContent.getPostcardSignedInfo().getEntityId());
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
			PostcardData postcardData = postcardCryptoService.decryptAndDecompressInstruction(postcardContent.getPostcardSignedInfo().getVersion(), postcardContent.getPostcardSignedInfo().getControl(), dk);
			postcardData.setApplicationType(ApplicationType.getApplicationType(Integer.parseInt(postcardContent.getPostcardSignedInfo().getApplicationId())));
			postcardData.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
			postcardData.setVersion(postcardContent.getPostcardSignedInfo().getVersion());
			return postcardData;
			
		} catch (Exception e) {
			throw new PostcardNonRetriableException("Exception occured while decrypt and validate instruction",e);
		} finally {
			LOG.debug("Time took for postcard decryption: {} msec. \nTime took for postcard to reach server: {} msec. ", (System.currentTimeMillis() - startTime)/1000, (startTime - postcardCreationOrReceivedTime)/1000);
		}
	}
	
	private void integrityFieldsGenerationForKeyNegotiation(PostcardContent postcardContent, byte[] dk, ClientType clientType) throws PostcardNonRetriableException {
		PostcardSignature postcardSignature = integrityValidator.generateHashAndSignaturesForKeyNegotiation(postcardContent, clientType);
		postcardContent.setHash(postcardSignature.getHash());
		PostcardContent.Signatures signatures = new PostcardContent.Signatures();
		signatures.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
		signatures.setSignature(postcardSignature.getSignature());
		if(!StringUtils.isBlank(postcardSignature.getSignatureScheme()) && "1.1".equals(postcardContent.getPostcardSignedInfo().getVersion())) {
			signatures.setSignatureScheme(PostcardSignatureScheme.fromValue(postcardSignature.getSignatureScheme()));
		}
		postcardContent.getSignatures().add(signatures);
	}

}
