package com.hp.wpp.avatar.restapp.resources;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
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
import com.hp.wpp.avatar.restapp.security.EntityValidator;
import com.hp.wpp.avatar.restapp.security.MessageValidator;
import com.hp.wpp.avatar.restapp.security.ValidatorBean;
import com.hp.wpp.logger.MDCConstants;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

@Path("/v1")
@Component
public class MessageValidationResource {

	@Autowired
	@Qualifier("messageValidator")
	private EntityValidator messageValidator;

	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(MessageValidationResource.class);
	
	public void setEntityValidator(MessageValidator messageValidator) {
		this.messageValidator = messageValidator;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/entities/message_validation/application_id/cloud_id/{application_id}/{cloud_id}")
	public Response validateMessage(
			@PathParam("application_id") String applicationId,
			@PathParam("cloud_id") String cloudId, String body) {

		String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		LOG.info("api=messageValidation; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=STARTED",cloudId, applicationId);
		if (StringUtils.isBlank(cloudId) || StringUtils.isBlank(applicationId)
				|| StringUtils.isBlank(body)) {
			LOG.error("api=messageValidation; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"Received empty cloudId, applicationId or body\";", cloudId,applicationId);
			return Response.status(Status.BAD_REQUEST).build();
		}

	

		try {

			ValidatorBean validatorBean = new ValidatorBean();
			validatorBean.setMessgae(body);
			messageValidator.validate(cloudId, applicationId,validatorBean);
			
			LOG.info("api=messageValidation; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=SUCCESS",cloudId, applicationId);
			return Response.status(Status.NO_CONTENT).build();

		} catch (Exception e) {
			
			LOG.error("api=messageValidation; method=POST; executionType=SyncFlow; cloudId={}; applicationId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId,applicationId,e);

			if (e instanceof InvalidRequestException
					|| e instanceof EntityValidationException
					|| e instanceof EntityNotRegisteredException
					|| e instanceof EntityRegistrationNonRetriableException) {
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}

		}finally {
            MDC.clear();
        }

	}
	
	 private String getLogUUID() {
			return UUID.randomUUID().toString();
		}
}
