package com.hp.wpp.avatar.restapp.resources;

import com.hp.wpp.avatar.framework.exceptions.DeviceBlacklistException;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.restapp.DeviceBlacklist.DeviceBlacklist;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.avatar.restapp.security.ValidationHelper;
import com.hp.wpp.avatar.restapp.util.JSONUtility;
import com.hp.wpp.avatar.restmodel.errors.AVRegErrors;
import com.hp.wpp.logger.MDCConstants;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.exception.PostcardEntityNotFoundException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
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
public class ResetCredentialResource {

	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(ResetCredentialResource.class);

	@Autowired
	private Postcard postcard;

	@Autowired
	private AvatarApplicationConfig avatarApplicationConfig;

	@Autowired
	private DeviceBlacklist deviceBlacklist;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@POST
	@Path("/entities/credentials/{cloud_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRefresh(@PathParam("cloud_id") String cloudId,
			String credentialRefresh,@HeaderParam("User-Agent")String userAgent) {
		
		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		boolean isBlackListEnabled = deviceBlacklist.isBlackListEnabled(avatarApplicationConfig.isBlacklistEnabled());
		LOG.info("api=resetCredentials method=POST; executionType=SyncFlow; cloudId={}; executionState=STARTED",cloudId);

		try {

			if(isBlackListEnabled && !StringUtils.isBlank(userAgent))
			{
				 deviceBlacklist.isBlacklist(userAgent,cloudId);
			}

			if (StringUtils.isBlank(credentialRefresh)) {
				LOG.error("api=resetCredentials method=POST; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"Empty Credentials\";", cloudId);
				return Response.status(Status.BAD_REQUEST).build();
			}
			LOG.debug("Checking Valid Cloudid");
			validationHelper.validateCloudId(cloudId);
			LOG.debug("Refreshing credentials.");
			postcard.refreshSharedSecret(credentialRefresh);
			
			LOG.info("api=resetCredentials method=POST; executionType=SyncFlow; cloudId={}; executionState=COMPLETED status=SUCCESS;",cloudId);
			return Response.ok().build();
		} catch(Exception e) {
			LOG.error("api=resetCredentials method=POST; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, e);
			if (e instanceof PostcardEntityNotFoundException || e instanceof EntityValidationException || e instanceof EntityNotRegisteredException) {				
				return Response.status(Status.NOT_FOUND).build();
			} else if (e instanceof PostcardNonRetriableException) {				
				return Response.status(Status.BAD_REQUEST).build();
			}else if(e instanceof DeviceBlacklistException){
				String error= JSONUtility.marshal(AVRegErrors.AVR000017.getWPPErrorModel());
				return Response.status(Status.FORBIDDEN).header("Internal-Error-Code", AVRegErrors.AVR000017.getWPPErrorModel().getErrors().get(0).getCode()).entity(error).build();
			}
			else {
				return Response.status(Status.SERVICE_UNAVAILABLE).build();
			}
		}finally {
            MDC.clear();
        }
	}
	
	 private String getLogUUID() {
			return UUID.randomUUID().toString();
		}
}