/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.KeyAgreementScheme;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.InitiateKeyNegotiationException;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.KeyExistsException;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.util.PostcardUtility;

/**
 * @author mahammad
 *
 */
public class PostcardSecretGenerator implements SecretGenerator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardSecretGenerator.class);
	
	@Autowired
	private PostcardDao postcardDao;
	@Autowired
	private KeyNegotiator keyNegotiator;
	@Autowired
	private PostcardCipher postcardCipher;
	@Autowired
	private PostcardConfig postcardConfig;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Override
	public PostcardEntity retrieveAndStoreSecret(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		byte[] postcardSecret = null;
		PostcardEntity postcardEntity = null;
		String entityId = postcardContent.getPostcardSignedInfo().getEntityId();
		// no need to check for validation of entityId.. bcoz it is parsers responsibility to check mandatory fields.
		
		postcardEntity = postcardDao.getPostcard(entityId);
		if(postcardEntity == null) {
			LOG.debug("shared secret not found for entityId: {}, starting key negotiation.", entityId);
			postcardSecret = generateSecret(postcardContent, postcardJson);
			postcardEntity = new PostcardEntity();
			postcardEntity.setSecret(postcardCipher.encrypt(postcardSecret, postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
			postcardEntity.setEntityId(entityId);
			postcardEntity.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
			postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
			postcardDao.createPostcard(postcardEntity);
		} else {
			String keyId = postcardEntity.getKeyId();
			String requestKeyId = postcardContent.getPostcardSignedInfo().getKeyId();

			if(keyId.equals(requestKeyId) && postcardContent.getPostcardSignedInfo().getKeys().size() > 0){
				throw new KeyExistsException("Key id already exists");
			}else if(!keyId.equals(requestKeyId)) {
				LOG.debug("Request keyId and previous session negotiated keyId are different. so starting key negotiation.");
				if(postcardContent.getPostcardSignedInfo().getKeys().size() > 0) {
					postcardSecret = generateSecret(postcardContent, postcardJson);
					postcardEntity.setKeyId(requestKeyId);
					postcardEntity.setSecret(postcardCipher.encrypt(postcardSecret, postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
					LOG.trace("shared secret generated: {} for entityId: {}", PostcardUtility.returnFormattedText(postcardSecret), entityId);
					postcardDao.updatePostcard(postcardEntity);
				} else {
					throw new InitiateKeyNegotiationException("No keys found in the request for key negotiation.");
				}
			} else {
				byte[] encryptedPostcardSecret = postcardEntity.getSecret();
				postcardSecret = postcardCipher.decrypt(encryptedPostcardSecret, postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV());
				// we should not log shared secret
				LOG.trace("shared secret fetched from database: {} for entityId: {}", PostcardUtility.returnFormattedText(postcardSecret), entityId);
			}
		}
		postcardEntity.setSecret(postcardSecret);
		return postcardEntity;
	}
	
	@Override
	public PostcardEntity getSecret(String entityId) throws PostcardNonRetriableException {
		PostcardEntity postcardEntity = postcardDao.getPostcard(entityId);
		if(postcardEntity == null) {
			throw new PostcardEntityNotFoundException("Postcard Enity not found");
		}
		postcardEntity.setSecret(postcardCipher.decrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
		return postcardEntity; 
	}

	/**
	 * @param postcardContent
	 * @return
	 * @throws PostcardException 
	 */
	protected byte[] generateSecret(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		byte[] postcardSecret = null;
		byte[] salt = keyNegotiator.validateKeysAndGenerateSalt(postcardContent, postcardJson);
		postcardSecret = PostcardUtility.generateSHA256Hash(salt, PostcardConstants.POSTCARD_SECRET_KEY_SIZE);
		LOG.debug("Shared secret generated: {} of length: {} in bytes.", PostcardUtility.returnFormattedText(postcardSecret), postcardSecret.length);
		return postcardSecret;
	}

	@Override
	public String generateEntityKey(String cloudId, String entityId, String applicationId) throws PostcardEntityNotFoundException, PostcardNonRetriableException {
		LOG.debug("Generating entity key for cloudId: {}, entityId: {}, applicationId: {}", cloudId, entityId, applicationId);
		PostcardEntity postcardEntity = postcardDao.getPostcard(entityId);
		
		if(postcardEntity == null) {
			throw new PostcardEntityNotFoundException("Postcard entity not found");
		}
		
		LOG.trace("encrypted secret: {}", PostcardUtility.returnFormattedText(postcardEntity.getSecret()));
		byte[] secret = postcardCipher.decrypt(postcardEntity.getSecret(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV());
		LOG.trace("secret: {}", PostcardUtility.returnFormattedText(secret));
		byte[] entityKeyBytes = generateKey((cloudId + applicationId).getBytes(), secret);
		String entityKey = Base64.encodeBase64String(entityKeyBytes);
		LOG.debug("EntityKey generated: {} for entityId: {}, applicationId: {}", entityKey, entityId, applicationId);
		return entityKey;
	}
	
	private byte[] generateKey(byte[] content, byte[] key) throws PostcardNonRetriableException {
		byte[] entityKey = null;
		Mac mac;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			entityKey = mac.doFinal(content);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new KeyCertificateException("Exception while generating hash",e);
		}
		return entityKey;
	}
	
	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}

	@Override
	public PostcardEntity generateAndStoreSecret(PostcardContent postcardContent, String postcardJson) throws InitiateKeyNegotiationException, PostcardNonRetriableException {
		byte[] postcardSecret = generateSecret(postcardContent, postcardJson);
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setSecret(postcardCipher.encrypt(postcardSecret, postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV()));
		postcardEntity.setEntityId(postcardContent.getPostcardSignedInfo().getEntityId());
		postcardEntity.setKeyId(postcardContent.getPostcardSignedInfo().getKeyId());
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardDao.createPostcard(postcardEntity);
		return postcardEntity;
	}
	
}
