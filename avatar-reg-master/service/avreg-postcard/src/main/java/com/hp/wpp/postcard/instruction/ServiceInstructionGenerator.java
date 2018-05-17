/**
 * 
 */
package com.hp.wpp.postcard.instruction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardCompression;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardContent.PostcardSignedInfo;
import com.hp.wpp.postcard.json.schema.PostcardCreator;
import com.hp.wpp.postcard.json.schema.PostcardEncryption;
import com.hp.wpp.postcard.json.schema.PostcardMessage;
import com.hp.wpp.postcard.json.schema.PostcardServiceInstruction;
import com.hp.wpp.postcard.json.schema.PostcardServiceInstruction.Commands;
import com.hp.wpp.postcard.json.schema.PostcardSignatureScheme;
import com.hp.wpp.postcard.key.generator.DKGenerator;
import com.hp.wpp.postcard.key.generator.IntegrityValidator;
import com.hp.wpp.postcard.key.generator.PostcardSignature;
import com.hp.wpp.postcard.key.generator.SecretGenerator;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.service.PostcardCryptoService;
import com.hp.wpp.postcard.util.PostcardUtility;

/**
 * @author mahammad
 *
 */
public class ServiceInstructionGenerator implements PostcardInstructionGenerator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(ServiceInstructionGenerator.class);
	
	@Autowired
	private PostcardParser postcardParser;
	@Autowired
	private DKGenerator dkGenerator;
	@Autowired
	private PostcardCryptoService postcardCryptoService;
	@Autowired
	private IntegrityValidator integrityValidator;
	@Autowired
	private PostcardDao postcardDao;
	@Autowired
	private SecretGenerator secretGenerator;

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.instruction.PostcardInstructionGenerator#generateServiceInstruction(com.hp.wpp.postcard.entities.PostcardEntity)
	 */
	@Override
	public String generateServiceInstruction(PostcardData postcardData) throws PostcardNonRetriableException {
		LOG.debug("Generating service instaruction postcard with generate new secret command for entityId: {}", postcardData.getEntityId());
		PostcardEntity postcardEntity = secretGenerator.getSecret(postcardData.getEntityId());
		
		if(postcardEntity == null) {
			throw new PostcardEntityNotFoundException("Postcard Entity not found");
		}
		
		byte[] postcardId = PostcardUtility.generateRandomBytes(PostcardConstants.POSTCARD_ID_LENGTH);
		LOG.debug("PostcardId generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(postcardId), postcardId.length);
		byte[] dk = dkGenerator.generateDK(postcardEntity.getSecret(), postcardId);
		// encrypt content
		PostcardContent postcardContent = new PostcardContent();

		PostcardContent.PostcardSignedInfo signedInfo =  new PostcardSignedInfo();
		signedInfo.setVersion(postcardData.getVersion());
		signedInfo.setMimeType(PostcardConstants.POSTCARD_MIME_TYPE);
		signedInfo.setCreator(PostcardCreator.valueOf(Creator.SERVICE.getCreatorName()));
		signedInfo.setApplicationId(String.valueOf(ApplicationType.AVATAR_REGISTRATION.getApplicationId()));
		
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = postcardDao.getPostcardAdditionalInfo(postcardEntity, String.valueOf(ApplicationType.AVATAR_REGISTRATION.getApplicationId()));
		long seqNum = 0;
		if(postcardAdditionalInfoEntity != null) {
			seqNum = postcardAdditionalInfoEntity.getServiceSeqNum();
		}
		signedInfo.setSeqNum(BigInteger.valueOf(seqNum + 1));
		signedInfo.setKeyId(postcardEntity.getKeyId());
		signedInfo.setTimestamp(PostcardUtility.getDate());
		signedInfo.setEntityId(postcardEntity.getEntityId());
		postcardContent.setPostcardSignedInfo(signedInfo);
		
		postcardContent.getPostcardSignedInfo().setPostcardId(Base64.encodeBase64String(postcardId));
		
		PostcardServiceInstruction postcardServiceInstruction = new PostcardServiceInstruction();
		Commands commands = new Commands();
		commands.setCommand(PostcardConstants.GENERATE_NEW_SECRET_COMMAND);
		commands.getOptions().add(PostcardConstants.SERVICE_INSTRUCTION_OPTION);
		postcardServiceInstruction.getCommands().add(commands);
		String postcardServiceInstructionJson = postcardParser.serialize(postcardServiceInstruction);
		
		postcardData.setEntityId(postcardEntity.getEntityId());
		PostcardData.Message message = new PostcardData().new Message();
		message.setContentType(ContentType.APPLICATION_SERVICE_INSTRUCTION);
		// TODO need to identify way to remove hardcoding
		if ("1.0".equals(postcardData.getVersion()))
			message.setCompression(PostcardCompression.gzip);
		else
			message.setCompression(PostcardCompression.http_gzip);

		message.setEncryption(PostcardEncryption.aes_128);
		message.setContent(postcardServiceInstructionJson.getBytes());
		postcardData.getMessages().add(message);
		
		List<PostcardMessage> postcardMessages = postcardCryptoService.encryptAndCompressMessageContents(postcardData.getVersion(), postcardData, dk);
		postcardContent.getPostcardSignedInfo().setControl(postcardMessages.get(0));
		
		integrityFieldsGeneration(postcardContent, dk);
		// serialize
		return postcardParser.serialize(postcardContent);
	}
	
	private void integrityFieldsGeneration(PostcardContent postcardContent, byte[] dk) throws PostcardNonRetriableException {
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

}
