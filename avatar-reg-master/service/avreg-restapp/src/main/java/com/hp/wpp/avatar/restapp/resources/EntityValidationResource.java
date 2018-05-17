package com.hp.wpp.avatar.restapp.resources;

import java.util.UUID;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRequestException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.security.AuthValidator;
import com.hp.wpp.avatar.restapp.security.ConnectivityAuthValidator;
import com.hp.wpp.avatar.restapp.security.EntityValidator;
import com.hp.wpp.avatar.restapp.security.ValidationHelper;
import com.hp.wpp.avatar.restapp.security.ValidatorBean;
import com.hp.wpp.logger.MDCConstants;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

@Path("/v1")
@Component
public class EntityValidationResource {
	
	public static final String WWW_AUTHENTICATE = "www-authenticate";

	public static final String  X_HP_APPLICATION_ID = "x-hp-application-id";

	public static final String X_HP_CUSTOM_AUTH = "x-hp-entity-auth";

	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(EntityValidationResource.class);

	private static final String X_HP_ENITTY_TYPE = "x-hp-entity-type";
	
	@Autowired
	private AvatarApplicationConfig avatarApplicationConfig;
	
	@Autowired
	@Qualifier("authValidator")
	private EntityValidator authValidator;

	@Autowired
	private ConnectivityAuthValidator connectivityAuthValidator;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	public void setEntityValidator(AuthValidator authValidator) {
		this.authValidator = authValidator;
	}

	public void setConnectivityAuthValidator(ConnectivityAuthValidator connectivityAuthValidator) {
		this.connectivityAuthValidator = connectivityAuthValidator;
	}

	public void setAvatarApplicationConfig(AvatarApplicationConfig avatarApplicationConfig) {
		this.avatarApplicationConfig = avatarApplicationConfig;
	}
	
	public void setValidationHelper(ValidationHelper validationHelper) {
		this.validationHelper = validationHelper;
	}
	
	@POST
	@Path("/entities/validation/application_id/{cloud_id}/{application_id}")
	public Response validateEntity( @HeaderParam("x-hp-entity-auth")String customAuth, @PathParam("cloud_id") String cloudId, @PathParam("application_id") String applicationId) {
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		
		LOG.debug("api=type1ValidateEntity; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=STARTED",cloudId, applicationId);
		try{
			
			if (StringUtils.isBlank(cloudId) || StringUtils.isBlank(applicationId)) {
				LOG.error("api=type1ValidateEntity; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"Received empty cloudId or applicationId \";", cloudId,applicationId);
				return Response.status(Status.NOT_FOUND).build();
			}
			
		 Response response = validateEntityRequest(customAuth,cloudId,applicationId);
		 
		 LOG.info("api=type1ValidateEntity; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=SUCCESS",cloudId, applicationId);
		 return response;
		  
		} catch(Exception e){
			LOG.error("api=type1ValidateEntity; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, applicationId, e);
			if(e instanceof EntityNotRegisteredException ||  e instanceof EntityValidationException){
				return Response.status(Status.NOT_FOUND).build();
			} else{
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}finally {
            MDC.clear();
        }
	}

	@Deprecated
	@POST
	@Path("/entities/validation/application_id")
	public Response validateEntityHeaders( @HeaderParam("x-hp-entity-auth")String customAuth, @HeaderParam("x-hp-cloud-id") String cloudId, @HeaderParam("x-hp-application-id") String applicationId) {
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		LOG.debug("api=validateEntityHeaders; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=STARTED",cloudId, applicationId);
		
		try{
			
			if (StringUtils.isBlank(customAuth))
			{
				LOG.error("api=validateEntityHeaders; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"CustomAuth header missing\";",cloudId,applicationId);
				return Response.status(Status.UNAUTHORIZED).build();
			}
			if (StringUtils.isBlank(cloudId)|| StringUtils.isBlank(applicationId)){
				LOG.error("api=validateEntityHeaders; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"cloudId or applicationId headers missing\";",cloudId,applicationId);
				return Response.status(Status.NOT_FOUND).build();
			}
			
			Response response =  validateEntityRequest(customAuth,cloudId,applicationId);
			
		    LOG.info("api=validateEntityHeaders; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=SUCCESS",cloudId, applicationId);
			return response;
		} catch(Exception e){
			LOG.error("api=validateEntityHeaders; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId,applicationId, e);
			if(e instanceof EntityNotRegisteredException ||  e instanceof EntityValidationException){
				return Response.status(Status.UNAUTHORIZED).build();
			} else{
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}finally {
            MDC.clear();
        }
	}

	@POST
	@Path("/entities/validation")
	public Response validateConnectivityEntityHeader(@HeaderParam("x-hp-entity-auth")String customAuth, @HeaderParam("x-hp-application-id") String applicationId) {
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		LOG.debug("api=type3ValidateConnectivityHeader; method=POST; executionType=SyncFlow; applicationId={}; executionState=STARTED",applicationId);
		
		try {
			
			if (StringUtils.isBlank(customAuth) || StringUtils.isBlank(applicationId)){
				LOG.error("api=type3ValidateConnectivityHeader; method=POST; executionType=SyncFlow; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"CustomAuth or applicationId headers missing\";",applicationId);
				return Response.status(Status.UNAUTHORIZED).build();
			}
			
			Response response = validateConnectivityEntityRequest(customAuth, applicationId);
			
			LOG.info("api=type3ValidateConnectivityHeader; method=POST; executionType=SyncFlow; applicationId={}; executionState=COMPLETED; status=SUCCESS", applicationId);
			return response;
		} catch(Exception e){
			LOG.error("api=type3ValidateConnectivityHeader; method=POST; executionType=SyncFlow; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", applicationId, e);
			if(e instanceof EntityNotRegisteredException ||  e instanceof EntityValidationException){
				return Response.status(Status.UNAUTHORIZED).build();
			} else{
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}finally {
            MDC.clear();
        }
	}




	@POST
    @Path("/entities/validation/application_id/{application_id}")
    public Response validateEntityApplicationId( @HeaderParam("x-hp-entity-auth")String customAuth, @HeaderParam("x-hp-cloud-id") String cloudId, @PathParam("application_id") String applicationId) {
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		LOG.debug("api=type2ValidateEntityApplicationID; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=STARTED",cloudId, applicationId);
		
		try{
			
			if (StringUtils.isBlank(cloudId) || StringUtils.isBlank(applicationId)){
				LOG.error("api=type2ValidateEntityApplicationID; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"Received empty cloudId or applicationId\";", cloudId,applicationId);
				return Response.status(Status.NOT_FOUND).build();
			}
			Response response = validateEntityRequest(customAuth,cloudId,applicationId);
			
			LOG.info("api=type2ValidateEntityApplicationID; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=SUCCESS",cloudId, applicationId);
			return response;
		} catch(Exception e){
			LOG.error("api=type2ValidateEntityApplicationID; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId,applicationId, e);
			if(e instanceof EntityNotRegisteredException ||  e instanceof EntityValidationException){
				return Response.status(Status.UNAUTHORIZED).build();
			} else{
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}finally {
            MDC.clear();
        }
    }
	private void validateURLParameters(String cloudId, String applicationId)throws EntityRegistrationNonRetriableException {
		 
		 if(applicationId.trim().isEmpty() || cloudId.trim().isEmpty())
				throw new EntityValidationException("url params shold not be empty");
		 
	}

	private Response validateConnectivityEntityRequest(String customAuth,String applicationId) throws EntityRegistrationNonRetriableException{
		RegisteredEntityBO registeredEntityBO = null;

		if(customAuth!=null){
			registeredEntityBO = connectivityAuthValidator.validateConnectivityCustomAuthHeader(customAuth, applicationId);
			if(registeredEntityBO.getEntityType() == null)
				throw new InvalidRequestException("entity type should not be empty");
			return Response.status(Status.OK).header(X_HP_ENITTY_TYPE, registeredEntityBO.getEntityType()).build();

		}
		else{
			if (avatarApplicationConfig.isEnvironmentProdOrStage()) {
				throw new EntityValidationException("Request not entertained, postcard flag set to true and custom auth header not found");
			}
		}
/*
		registeredEntityBO = entitySecurityManager.validateCloudId(cloudId);
*/
		return Response.status(Status.OK).header(X_HP_ENITTY_TYPE, registeredEntityBO.getEntityType()).build();
	}
	private Response validateEntityRequest(String customAuth,String cloudId,String applicationId) throws EntityRegistrationNonRetriableException {
		RegisteredEntityBO registeredEntityBO = null;

			validateURLParameters(cloudId, applicationId);
			if(customAuth!=null){
				ValidatorBean validatorBean = new ValidatorBean();
				validatorBean.setHeader(customAuth);
				registeredEntityBO = authValidator.validate(cloudId, applicationId, validatorBean);
				if(registeredEntityBO.getEntityType() == null)
					throw new InvalidRequestException("entity type should not be empty");
				return Response.status(Status.OK).header(X_HP_ENITTY_TYPE, registeredEntityBO.getEntityType()).build();

			}
			else{
				if (avatarApplicationConfig.isEnvironmentProdOrStage()) {
					throw new EntityValidationException("Request not entertained, postcard flag set to true and custom auth header not found");
				}
			}
			registeredEntityBO = validationHelper.validateCloudId(cloudId);
			
			return Response.status(Status.OK).header(X_HP_ENITTY_TYPE, registeredEntityBO.getEntityType()).build();
	}
	
	
	 private String getLogUUID() {
			return UUID.randomUUID().toString();
		}

}
