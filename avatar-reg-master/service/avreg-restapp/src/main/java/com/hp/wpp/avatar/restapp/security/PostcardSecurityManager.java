package com.hp.wpp.avatar.restapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.PostcardData.Message;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

@Component
public class PostcardSecurityManager {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardSecurityManager.class);
	
	@Autowired
	private Postcard postcard;
	
	public void setPostcard(Postcard postcard) {
		this.postcard = postcard;
	}
	
	public String decryptPostcardPayload(String regPayload, PostcardData requestPostcardData) throws PostcardNonRetriableException {
		String entityIdentificationJson;
		entityIdentificationJson = new String(requestPostcardData.getMessages().get(0).getContent());
		LOG.debug("Entity identification payload: {}", entityIdentificationJson);
		return entityIdentificationJson;
	}
	
	public String encryptPostcardData(
			EntityIdentificationBO entityIdentificationBo,
			String responsePayload,PostcardData requestPostcardData) throws PostcardNonRetriableException {
		PostcardData responsePostcardData = preparePostcardData(entityIdentificationBo.getEntityUUID());
		Message message = preparePostcardMessage(responsePayload,requestPostcardData);
		responsePostcardData.getMessages().add(message);
		responsePostcardData.setVersion(requestPostcardData.getVersion());
		responsePayload = postcard.encryptPostcard(responsePostcardData);
		return responsePayload;
	}
	
	private PostcardData preparePostcardData(String printerUUID) {
		PostcardData responsePostcardData = new PostcardData();
		responsePostcardData.setEntityId(printerUUID);
		responsePostcardData.setApplicationType(ApplicationType.AVATAR_REGISTRATION);
		return responsePostcardData;
	}

	private Message preparePostcardMessage(String printerConfigJson,PostcardData requestPostcardData) {
		Message message = new PostcardData().new Message();
		message.setCompression(requestPostcardData.getMessages().get(0).getCompression());
		message.setContentType(ContentType.APPLICATION_JSON);
		message.setEncryption(requestPostcardData.getMessages().get(0).getEncryption());
		message.setContent(printerConfigJson.getBytes());
		return message;
	}

}
