package com.hp.wpp.avatar.restapp.resources;

import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.restapp.DeviceBlacklist.DeviceBlacklist;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restapp.security.PostcardSecurityManager;
import com.hp.wpp.avatar.restapp.util.BeanConverterUtil;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification;
import com.hp.wpp.logger.MDCConstants;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.stream.producer.AvregEventProducer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.UUID;

@Path("/v1")
@Component
public class EntityResource {

	public static final String LOCATION_HEADER = "Location";
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(EntityResource.class);

	@Autowired
	private AvatarApplicationConfig avatarApplicationConfig;

	@Autowired
	private EntityTypeProcessorMap entityTypeProcessorMap;

	@Autowired
	private DeviceBlacklist deviceBlacklist;

	@Autowired
	private PostcardSecurityManager postcardSecurityManager;
	
	@Autowired
	private Postcard postcard;

	@Autowired
	private AvregEventProducer registrationEventProducer;
	
	public void setPostcard(Postcard postcard) {
		this.postcard = postcard;
	}

	public void setAvatarApplicationConfig(
			AvatarApplicationConfig avatarApplicationConfig) {
		this.avatarApplicationConfig = avatarApplicationConfig;
	}

	public void setEntityTypeProcessorMap(
			EntityTypeProcessorMap entityTypeProcessorMap) {
		this.entityTypeProcessorMap = entityTypeProcessorMap;
	}

	public void setPostcardSecurityManager(PostcardSecurityManager postcardSecurityManager) {
		this.postcardSecurityManager = postcardSecurityManager;
		
	}

	@POST
	@Path("/entities")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerEntity(String regPayload,@HeaderParam("X-Forwarded-For") String xForwardedForHeader,@HeaderParam("With-Postcard")String withPostcardHeader, @HeaderParam("User-Agent")String userAgent) {
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
			
		boolean isPostcardEnabled = isPostcardEnabled(withPostcardHeader, avatarApplicationConfig.isPostcardEnabled());
		boolean isBlackListEnabled = deviceBlacklist.isBlackListEnabled(avatarApplicationConfig.isBlacklistEnabled());
		String hostIP = getHostIP(xForwardedForHeader);
		LOG.debug("requestPayload={}",regPayload);
		LOG.info("api=registerEntity; method=POST; executionType=SyncFlow; postcardEnabled={}; executionState=STARTED",isPostcardEnabled);
		EntityIdentification entityIdentification = null;
        EntityConfigurationBO entityConfigurationBO = null;
		PostcardData requestPostcardData =null;
		String entityIdentificationJson = regPayload;
		EntityIdentificationBO entityIdentificationBo = null;
		try {

			if(isBlackListEnabled && !StringUtils.isBlank(userAgent))
			{
				deviceBlacklist.isBlacklist(userAgent,null);

			}
			if (isPostcardEnabled) {
				requestPostcardData = postcard.validateAndDecryptPostcard(regPayload);
				entityIdentificationJson = postcardSecurityManager.decryptPostcardPayload(regPayload,requestPostcardData);
			}

			entityIdentification = EntityResourceHelper.createAndValidateEntityIdentification(entityIdentificationJson);
			if(!StringUtils.isBlank(hostIP)){
				entityIdentification.setHostIP(hostIP);
			}
			entityIdentificationBo = BeanConverterUtil.createAndValidateEntityIdentifierBO(entityIdentification, entityTypeProcessorMap);
			EntityRegistrationProcessor entityRegistrationProcessor = entityTypeProcessorMap.getEntityRegistrationProcessor(entityIdentification.getEntityType());
			entityConfigurationBO = entityRegistrationProcessor.registerEntity(entityIdentificationBo);
			String responsePayload = EntityResourceHelper.createEntityConfigJsonPayload(entityConfigurationBO);

			if (isPostcardEnabled)
				responsePayload = postcardSecurityManager.encryptPostcardData(entityIdentificationBo, responsePayload, requestPostcardData);

			Response response = Response.status(Status.CREATED).entity(responsePayload)
					.header(LOCATION_HEADER, avatarApplicationConfig.getAvatarRegistrationURL()).build();
			notifyEvent(entityIdentificationBo, entityConfigurationBO, response,null);

			LOG.debug("cloudId:{}; responsePayload={}",entityConfigurationBO.getCloudId(),responsePayload);
			LOG.info("api=registerEntity; method=POST; executionType=SyncFlow; entityId=\"{}\"; entityType={}; entityModel={}; cloudId={}; entityVersion={}; countryAndRegion={}; language={}; printerUUID={}; executionState=COMPLETED; status=SUCCESS", EntityResourceHelper.getLoggableEntityId(entityIdentification),(entityIdentification!=null?entityIdentification.getEntityType():null),(entityIdentification!=null?entityIdentification.getEntityModel():null),(entityConfigurationBO!=null?entityConfigurationBO.getCloudId():null),(entityIdentification!=null?entityIdentification.getEntityVersion():null),(entityIdentification!=null?entityIdentification.getCountryAndRegionName():null),(entityIdentification!=null?entityIdentification.getLanguage():null), (entityIdentification!=null?EntityResourceHelper.getPrinterUUID(entityIdentification):null));
			return response;
		} catch (Exception e) {
			Response response = EntityResourceHelper.actOnRegistrationFailure(entityIdentification, entityConfigurationBO, e);
			notifyEvent(entityIdentificationBo, entityConfigurationBO, response,response.getHeaderString("Intenal-Error-Code"));
				
			return response;
		}finally {
            MDC.clear();
        }
	}

	private String getHostIP(String xForwardedForHeader) {
		String hostIP = null;
		if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()){
			String hostIps[]=xForwardedForHeader.split(",");
			hostIP=hostIps[0];
			LOG.debug("Source IP={}",hostIP);
		}
		return hostIP;
	}


	private void notifyEvent(EntityIdentificationBO entityIdentificationBO, EntityConfigurationBO entityConfigurationBO, Response response, String errorCode) {
		try {
			//TODO: error number will be passed later
			if(avatarApplicationConfig.isEventEnabled()) {
				registrationEventProducer.sendRegistrationEvent(entityIdentificationBO, entityConfigurationBO, response.getStatus(), errorCode);
			}
		}catch (Exception e) {
			//making sure registration does not break if while error during send event
				LOG.debug("api=notifyRegistrationEvent; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", (entityConfigurationBO!=null?entityConfigurationBO.getCloudId():null), (e.getMessage()!=null?e.getMessage():"Received empty entityIdentification object"));
		}
	}

	private boolean isPostcardEnabled(String withPostcardHeader, boolean isAppconfigPostcardFlagEnabled) {
		boolean isProdEnv = avatarApplicationConfig.isEnvironmentProdOrStage(); 
		if(isProdEnv)
			return isAppconfigPostcardFlagEnabled;
		else{
			if(StringUtils.isNotEmpty(withPostcardHeader))
				return Boolean.valueOf(withPostcardHeader);
			else
				return isAppconfigPostcardFlagEnabled;
		}
	}



	 private String getLogUUID() {
			return UUID.randomUUID().toString();
		}

}
