/**
 * 
 */
package com.hp.wpp.postcard.validator;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.entities.PostcardRenegotiationInfoEntity;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.parser.PostcardParser;

/**
 * @author mahammad
 *
 */
public class PostcardValidatorImpl implements PostcardValidator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardValidatorImpl.class);
	
	@Autowired
	private PostcardDao postcardDao;
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private PostcardConfig postcardConfig;

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.validator.Validator#validatePostcardAndAdditionalInfo(com.hp.wpp.postcard.entities.PostcardEntity)
	 */
	@Override
	public void validatePostcardAdditionalInfo(PostcardContent postcardContent, PostcardEntity postcardEntity) throws PostcardNonRetriableException {
		long lastValidSeqNum = 0;
		long seqNum = 0;
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = postcardDao.getPostcardAdditionalInfo(postcardEntity, postcardContent.getPostcardSignedInfo().getApplicationId());
		if(postcardAdditionalInfoEntity == null) {
			LOG.debug("postcard additional info not found.. going forward with default entity sequence number.");
			//TODO need to check whether to discard request from here.
			postcardAdditionalInfoEntity = new PostcardAdditionalInfoEntity();
		} 
		
		Creator creator = Creator.getCreator(postcardContent.getPostcardSignedInfo().getCreator().value());
		if(creator == Creator.SERVICE) {
			lastValidSeqNum = postcardAdditionalInfoEntity.getServiceSeqNum();
		} else {
			lastValidSeqNum = postcardAdditionalInfoEntity.getEntitySeqNum();
		}
		seqNum = postcardContent.getPostcardSignedInfo().getSeqNum().longValue();
		if(postcardConfig.isEntitySeqNumValidationRequired() && !(seqNum > lastValidSeqNum)) {
			LOG.warning("Sequence number validation failed. received={}; lastValidSeqNum={}", seqNum, lastValidSeqNum);
		}
	}

	@Override
	public void populateAndStorePostcardAdditionalInfo(PostcardEntity postcardEntity, PostcardContent postcardContent, Creator creator, boolean isSecretRefresh, String entityInstruction) throws PostcardNonRetriableException {
		PostcardEntity dbPostcardEntity = postcardDao.getPostcard(postcardEntity.getEntityId());
		
		//TODO NULL check for to support key negotiation request generation
		if(dbPostcardEntity == null) {
			dbPostcardEntity = postcardEntity;
			postcardDao.createPostcard(dbPostcardEntity);
		} else {
			dbPostcardEntity.setSecret(postcardEntity.getSecret());
			dbPostcardEntity.setKeyId(postcardEntity.getKeyId());
			postcardDao.updatePostcard(dbPostcardEntity);
		}
		
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = postcardDao.getPostcardAdditionalInfo(dbPostcardEntity, postcardContent.getPostcardSignedInfo().getApplicationId());
		if(postcardAdditionalInfoEntity == null) {
			postcardAdditionalInfoEntity = new PostcardAdditionalInfoEntity();
			if(Creator.SERVICE == creator) {
				postcardAdditionalInfoEntity.setServiceSeqNum(1);
			} else if(Creator.ENTITY == creator) {
				// we are incrementing further so we are decrementing here
				postcardAdditionalInfoEntity.setEntitySeqNum(postcardContent.getPostcardSignedInfo().getSeqNum().longValue() - 1);
			}
		}
		postcardAdditionalInfoEntity.setPostcardEntity(dbPostcardEntity);
		if(Creator.SERVICE == creator) {
			if(isSecretRefresh)
				postcardAdditionalInfoEntity.setServiceSeqNum(postcardContent.getPostcardSignedInfo().getSeqNum().longValue() - 1);
			populateServiceAdditionalInfo(postcardContent, postcardAdditionalInfoEntity);
		} else if(Creator.ENTITY == creator) {
			if(isSecretRefresh)
				postcardAdditionalInfoEntity.setEntitySeqNum(postcardContent.getPostcardSignedInfo().getSeqNum().longValue() - 1);
			populateEntityAdditionalInfo(postcardContent, postcardAdditionalInfoEntity);
		}
		
		if(!StringUtils.isBlank(entityInstruction))
			postcardAdditionalInfoEntity.setEntityInstruction(entityInstruction);
		
		postcardDao.updatePostcardAdditionalInfo(postcardAdditionalInfoEntity);
	}

	private void populateServiceAdditionalInfo(PostcardContent postcardContent, PostcardAdditionalInfoEntity postcardAdditionalInfoEntity) {
		postcardAdditionalInfoEntity.setApplicationId(postcardContent.getPostcardSignedInfo().getApplicationId());
		postcardAdditionalInfoEntity.setServiceMessageId(postcardContent.getPostcardSignedInfo().getPostcardId());
		long serviceSeqNum = postcardAdditionalInfoEntity.getServiceSeqNum();
		postcardAdditionalInfoEntity.setServiceSeqNum(serviceSeqNum + 1);
		String keyId = postcardContent.getPostcardSignedInfo().getKeyId();
		List<PostcardContent.Signatures> signatures = postcardContent.getSignatures();
		for (PostcardContent.Signatures signature : signatures) {
			if(keyId.equals(signature.getKeyId())) {
				postcardAdditionalInfoEntity.setServiceSignatureHash(signature.getSignature());
			}
		}
		
		postcardContent.getPostcardSignedInfo().setSeqNum(BigInteger.valueOf(postcardAdditionalInfoEntity.getServiceSeqNum()));
	}
	
	private void populateEntityAdditionalInfo(PostcardContent postcardContent, PostcardAdditionalInfoEntity postcardAdditionalInfoEntity) {
		postcardAdditionalInfoEntity.setApplicationId(postcardContent.getPostcardSignedInfo().getApplicationId());
		postcardAdditionalInfoEntity.setEntityMessageId(postcardContent.getPostcardSignedInfo().getPostcardId());
		long entitySeqNum = postcardAdditionalInfoEntity.getEntitySeqNum();
		postcardAdditionalInfoEntity.setEntitySeqNum(entitySeqNum + 1);
		try {
			postcardAdditionalInfoEntity.setEntityInstruction(Base64.encodeBase64String(postcardParser.serialize(postcardContent.getPostcardSignedInfo().getControl()).getBytes()));
		} catch (InvalidPostcardException e) { }
		String keyId = postcardContent.getPostcardSignedInfo().getKeyId();
		List<PostcardContent.Signatures> signatures = postcardContent.getSignatures();
		for (PostcardContent.Signatures signature : signatures) {
			if(keyId.equals(signature.getKeyId())) {
				postcardAdditionalInfoEntity.setEntitySignatureHash(signature.getSignature());
			}
		}
		
		postcardContent.getPostcardSignedInfo().setSeqNum(BigInteger.valueOf(postcardAdditionalInfoEntity.getEntitySeqNum()));
	}

	@Override
	public void storePostcardRenegotiationInfo(PostcardEntity postcardEntity, PostcardData postcardData) throws PostcardNonRetriableException {
		PostcardRenegotiationInfoEntity postcardRenegotiationInfoEntity = new PostcardRenegotiationInfoEntity();
		postcardRenegotiationInfoEntity.setApplicationId(String.valueOf(postcardData.getApplicationType().getApplicationId()));
		postcardRenegotiationInfoEntity.setPostcardEntity(postcardEntity);
		postcardRenegotiationInfoEntity.setCredentialRefreshInfo(postcardData.getMessages().get(0).getContent());
		postcardDao.store(postcardRenegotiationInfoEntity);
	}

}
