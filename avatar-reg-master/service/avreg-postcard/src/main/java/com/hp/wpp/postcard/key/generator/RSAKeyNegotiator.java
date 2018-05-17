/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.cipher.ByteArrayCipherData;
import com.hp.wpp.postcard.cipher.ByteArrayCipherKey;
import com.hp.wpp.postcard.cipher.CertificateManager;
import com.hp.wpp.postcard.cipher.CipherData;
import com.hp.wpp.postcard.cipher.CipherKey;
import com.hp.wpp.postcard.cipher.PostcardCertValidator;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.cipher.PostcardRSAKEMCipherData;
import com.hp.wpp.postcard.cipher.RSACipherKey;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardEnums;
import com.hp.wpp.postcard.common.PostcardEnums.ClientType;
import com.hp.wpp.postcard.common.PostcardEnums.EnvironmentType;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardCertificateInfoEntity;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.InvalidPublicKeyException;
import com.hp.wpp.postcard.exception.KeyCertificateException;
import com.hp.wpp.postcard.exception.PostcardHashMismatchException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardContent.PostcardSignedInfo;
import com.hp.wpp.postcard.json.schema.PostcardDomain;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.json.schema.PostcardKey.EntitySignature;
import com.hp.wpp.postcard.json.schema.PostcardKey.KeyAgreement;
import com.hp.wpp.postcard.json.schema.PostcardKey.KeyAgreement.RsaKem;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.version.factory.PostcardVersionStrategyFactory;

/**
 * @author mahammad
 *
 */
public class RSAKeyNegotiator implements KeyNegotiator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RSAKeyNegotiator.class);
	
	@Autowired
	private PostcardCipher postcardCipher;
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private CertificateManager certificateManager;
	@Autowired
	private PostcardDao postcardDao;
	@Autowired
	private PostcardConfig postcardConfig;
	@Autowired
	private PostcardVersionStrategyFactory postcardVersionFactory;
	@Autowired
	private PostcardCertValidator postcardCertValidator;
	
	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.key.generator.KeyNegotiator#validateKeysAndGenerateSalt(com.hp.wpp.postcard.json.schemas.PostcardContent)
	 */
	@Override
	public byte[] validateKeysAndGenerateSalt(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		byte[] salt = null;
		
		if(postcardContent.getPostcardSignedInfo().getKeys().size() <= 0) {
			throw new InvalidPostcardException("No keys received for key negotiation for first time request.");
		}
		
		CipherData cipherData = getRSAKEMCipherData(postcardContent);
		CipherKey cipherKey = verifyServerPublicKeyAndLoadServerPrivateKey(cipherData);
		if(postcardConfig.isPostcardEntityCertValidationRequired()) {
			validatePostcardEntityCertificate(cipherData);
		} else {
			LOG.debug("Postcard entity certificate validation is disabled..");
		}
		salt = postcardCipher.decrypt(cipherData, cipherKey);
		LOG.debug("salt derived from request: {} of size: {} bytes", PostcardUtility.returnFormattedText(salt), salt.length);
		
		// verifying entity signature
		SignatureData entitySignatureData = populateEntitySignatureData(postcardContent, postcardJson);
		entitySignatureData.setKey(salt);
		verifyEntitySignature(postcardContent, entitySignatureData);
		
		// verifying postcard signature during key negotiation
		List<KeySignatureData> postcardSignatureData = populatePostcardKeySignatureData(postcardContent, postcardJson);
		verifyKeySignature(postcardContent, postcardSignatureData);
		return salt;
	}
	
	private CipherKey verifyServerPublicKeyAndLoadServerPrivateKey(CipherData cipherData) throws PostcardNonRetriableException {
		PostcardRSAKEMCipherData postcardRSAKEMCipherData = (PostcardRSAKEMCipherData) cipherData;
		RSACipherKey cipherKey = new RSACipherKey();
		try {
			KeyPair keyPair = null;
			if(!StringUtils.isBlank(postcardConfig.getJavaCertsKeyStoreLocation())) {
				LOG.debug("Loading private key from keystore location: {}", postcardConfig.getJavaCertsKeyStoreLocation());
				keyPair = certificateManager.getRsaKeyPair(postcardConfig.getJavaCertsKeyStoreLocation(), postcardRSAKEMCipherData.getServerPublicKeyId());
			} else {
				keyPair = certificateManager.getRsaKeyPair(postcardRSAKEMCipherData.getServerPublicKeyId());
			}
	        
	        PrivateKey privateKey = keyPair.getPrivate();
			cipherKey.setPrivateKey(privateKey);
	        	
		} catch (PostcardNonRetriableException e) {
			throw new InvalidPublicKeyException("Exception occured while public key id validation and loading private key",e);
		} 
		return cipherKey;
	}
	
	private void validatePostcardEntityCertificate(CipherData cipherData) throws PostcardNonRetriableException {
		PostcardRSAKEMCipherData postcardRSAKEMCipherData = (PostcardRSAKEMCipherData) cipherData;
		LOG.debug("CA signing verification started.. ");
		postcardCertValidator.verify(postcardRSAKEMCipherData.getEntityPemCertificate());
	}

	private List<KeySignatureData> populatePostcardKeySignatureData(PostcardContent postcardContent, String postcardJson) throws PostcardNonRetriableException {
		String signedInfo = postcardParser.getNodeValue(postcardJson, "postcard_signed_info");
		String canonizedSignedInfo = PostcardUtility.canonize(signedInfo);
		LOG.trace("canonized signed info: {}", canonizedSignedInfo);
		byte[] hash = PostcardUtility.generateSHA256Hash(canonizedSignedInfo.getBytes(), -1); 
		LOG.debug("Postcard Hash generated: {}, of size: {} bytes", PostcardUtility.returnFormattedText(hash), hash.length);
		
		if(!Arrays.equals(hash, Base64.decodeBase64(postcardContent.getHash()))) {		
			throw new PostcardHashMismatchException("Hash validation failed.");
		}
		
		List<KeySignatureData> keySignatureDatas = new ArrayList<>();
		
		List<PostcardContent.Signatures> keySignatures = postcardContent.getSignatures();
		List<PostcardKey> keys = postcardContent.getPostcardSignedInfo().getKeys();
		
		for (PostcardContent.Signatures keySignature : keySignatures) {
			for (PostcardKey key : keys) {
				if (PostcardUtility.isSupportedDomain(key.getDomain())) {
					byte[] entityPemCertificate = null;
					if (!StringUtils.isBlank(key.getEntitySignature().getPemCertificate())) {
						entityPemCertificate = key.getEntitySignature().getPemCertificate().getBytes();
					} else if (!StringUtils.isBlank(key.getEntitySignature().getEntityCertificateKeyId())) {
						PostcardCertificateInfoEntity postcardCertificateInfoEntity = postcardDao.getPostcardCertificateInfo(key.getEntitySignature().getEntityCertificateKeyId());
						entityPemCertificate = postcardCipher.decrypt(postcardCertificateInfoEntity.getCertificateData(), postcardConfig.getSharedSecretEncryptionKey(), postcardConfig.getSharedSecretEncryptionIV());
					} else {
						throw new InvalidPostcardException("Entity public key certificate not found in request or certificate not found in database for public key serial number="+ key.getEntitySignature().getEntityCertificateKeyId());
					}
					KeySignatureData keySignatureData = new KeySignatureData();
					keySignatureData.setKey(entityPemCertificate);
					keySignatureData.setSignatureTobeVerified(Base64.decodeBase64(keySignature.getSignature()));
					keySignatureData.setKeyId(key.getPostcardSecretKeyId());
					keySignatureData.setData(hash);

					// TODO this is to provide backward compatability. This has
					// to be moved to common place
					if ("1.0".equals(postcardContent.getPostcardSignedInfo().getVersion()) && keySignature.getSignatureScheme() == null) {
						keySignatureData.setSignatureScheme(PostcardSignatureScheme.sha256_with_rsa_and_mfg1.name());
					} else if (keySignature.getSignatureScheme() != null) {
						keySignatureData.setSignatureScheme(keySignature.getSignatureScheme().name());
					}

					keySignatureDatas.add(keySignatureData);

				} else {
					throw new UnsupportedPostcardException("key signatures generated with unsupported crypto domain.");
				}
			}
		}
		return keySignatureDatas;
	}

	private SignatureData populateEntitySignatureData(PostcardContent postcardContent, String postcardJson) throws InvalidPostcardException {
		SignatureData entitySignatureData = null;
		List<PostcardKey> keys = postcardContent.getPostcardSignedInfo().getKeys();
		for (PostcardKey key : keys) {
			if(PostcardUtility.isSupportedDomain(key.getDomain())) {
				String entitySignature = postcardParser.getNodeValue(postcardJson, "entity_signature");
				LOG.debug("Entity Signature in the request: {}", key.getKeyAgreement().getRsaKem().getEntitySignatureValidation());
				String canonizedDeviceSignature = PostcardUtility.canonize(entitySignature);
				LOG.trace("canonized deviceSignature: {}", canonizedDeviceSignature);
				entitySignatureData = new SignatureData();
				entitySignatureData.setData(canonizedDeviceSignature.getBytes());
				entitySignatureData.setSignatureTobeVerified(Base64.decodeBase64(key.getKeyAgreement().getRsaKem().getEntitySignatureValidation()));
			}
		}
		return entitySignatureData;
	}
	
	private void verifyKeySignature(PostcardContent postcardContent, List<KeySignatureData> keySignatureDatas) throws PostcardNonRetriableException {
		
		for (KeySignatureData keySignatureData : keySignatureDatas) {
			//TODO hmac sha256 need mak for validation. So this integration check is validate before deriving DK
			if(!PostcardSignatureScheme.hmac_sha256.name().equalsIgnoreCase(keySignatureData.getSignatureScheme())) {
				ByteArrayCipherData cipherData = new ByteArrayCipherData();
				cipherData.setCipherData(keySignatureData.getData());
				
				RSACipherKey cipherKey = new RSACipherKey();
				cipherKey.setPublicKey(certificateManager.generatePublicKeyFromCertificate(new String(keySignatureData.getKey())));
				
				PostcardSignatureScheme postcardSignatureScheme = PostcardSignatureScheme.fromValue(keySignatureData.getSignatureScheme());
				
				postcardVersionFactory.getSignatureSchemeStrategy(postcardContent.getPostcardSignedInfo().getVersion()).verifySignature(cipherData, cipherKey, keySignatureData.getSignatureTobeVerified(), postcardSignatureScheme);
			}
		}
	}
	
	private void verifyEntitySignature(PostcardContent postcardContent, SignatureData entitySignatureData) throws PostcardNonRetriableException {
		ByteArrayCipherData byteArrayCipherData = new ByteArrayCipherData();
		byteArrayCipherData.setCipherData(entitySignatureData.getData());
		
		ByteArrayCipherKey byteArrayCipherKey = new ByteArrayCipherKey();
		byteArrayCipherKey.setCipherKey(entitySignatureData.getKey());
		
		postcardVersionFactory.getSignatureSchemeStrategy(postcardContent.getPostcardSignedInfo().getVersion()).verifySignature(byteArrayCipherData, byteArrayCipherKey, entitySignatureData.getSignatureTobeVerified(), PostcardSignatureScheme.hmac_sha256);
	}
	
	private CipherData getRSAKEMCipherData(PostcardContent postcardContent) throws UnsupportedPostcardException {
		PostcardRSAKEMCipherData cipherData = null;
		List<PostcardKey> keys = postcardContent.getPostcardSignedInfo().getKeys();
		for (PostcardKey key : keys) {
			if (PostcardUtility.isSupportedDomain(key.getDomain())) {
				cipherData = new PostcardRSAKEMCipherData();
				cipherData.setKeyData(Base64.decodeBase64(key.getKeyAgreement().getRsaKem().getKeyData()));
				cipherData.setServerPublicKeyId(key.getKeyAgreement().getRsaKem().getServerPublicKeyId());
				cipherData.setEntityGeneratedSignature(key.getKeyAgreement().getRsaKem().getEntitySignatureValidation());
				cipherData.setEntityPemCertificate(key.getEntitySignature().getPemCertificate());
				return cipherData;
			}
		}
		throw new UnsupportedPostcardException("Unsupported crypto domain received in the request.");
		
	}

	@Override
	public PostcardContent generateKeysAndSignatures(String postcardVersion, byte[] salt, ClientType clientType, EnvironmentType environmentType) throws PostcardNonRetriableException {
		PostcardContent postcardContent = new PostcardContent();
		PostcardSignedInfo postcardSignedInfo = new PostcardSignedInfo();
		PostcardKey postcardKey = new PostcardKey();
		
		EntitySignature entitySignature = new EntitySignature();
		try {
			entitySignature.setPemCertificate(new String(clientType.getPublicCertificate()));
			
			String deviceSign = postcardParser.serialize(entitySignature);
			String canonizedDeviceSign = PostcardUtility.canonize(deviceSign);
			LOG.trace("canonized EntitySignature: {}", canonizedDeviceSign);
			
			ByteArrayCipherData byteArrayCipherData = new ByteArrayCipherData();
			byteArrayCipherData.setCipherData(canonizedDeviceSign.getBytes());
			
			ByteArrayCipherKey byteArrayCipherKey = new ByteArrayCipherKey();
			byteArrayCipherKey.setCipherKey(salt);
			
			postcardSignedInfo.setVersion(postcardVersion);
			String entitySignatureValidation =  Base64.encodeBase64String(postcardVersionFactory.getSignatureSchemeStrategy(postcardSignedInfo.getVersion()).generateSignature(byteArrayCipherData, byteArrayCipherKey, PostcardSignatureScheme.hmac_sha256));
			
			String keyId = UUID.randomUUID().toString().replace("-", "");
			postcardKey.setPostcardSecretKeyId(keyId);
			
			postcardKey.setDomain(PostcardDomain.fromValue(postcardConfig.getSupportedDomain()));
			KeyAgreement keyAgreement = new KeyAgreement();
			RsaKem rsaKem = new RsaKem();
			
			PublicKey publicKey = certificateManager.generatePublicKey(new String(environmentType.getPublicKey()));
			RSACipherKey cipherKey = new RSACipherKey();
			cipherKey.setPublicKey(publicKey);
			byte[] key_data = postcardCipher.encrypt(salt, cipherKey);
			rsaKem.setKeyData(Base64.encodeBase64String(key_data));
			
	        X509Certificate cer = (X509Certificate) certificateManager.generateCertificate(new String(environmentType.getPublicCertificate()));
	        String serverPublicKeyId = cer.getSerialNumber().toString(16);
			rsaKem.setServerPublicKeyId(serverPublicKeyId);
			rsaKem.setEntitySignatureValidation(entitySignatureValidation);
			keyAgreement.setRsaKem(rsaKem);
			
			postcardKey.setKeyAgreement(keyAgreement);
			postcardKey.setEntitySignature(entitySignature);
			
			postcardSignedInfo.setApplicationId(String.valueOf(PostcardEnums.ApplicationType.AVATAR_REGISTRATION.getApplicationId()));
			postcardSignedInfo.setKeyId(keyId);
			postcardSignedInfo.getKeys().add(postcardKey);
			postcardContent.setPostcardSignedInfo(postcardSignedInfo);
		} catch (IOException e) {
			throw new KeyCertificateException("Exception occured while generating keys info",e);
		} 
		return postcardContent;
	}
}	
