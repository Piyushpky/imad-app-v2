package com.hp.wpp.ssnclaim.restapp.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hp.wpp.ssnc.common.util.JSONUtility;
import com.hp.wpp.ssnclaim.exception.InvalidNotificationPayloadException;
import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.Error;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource.getErrorObject;

@Path("/v1")
@Component
public class PrinterResource {

	private static final WPPLogger logger = WPPLoggerFactory
			.getLogger(PrinterResource.class);

	@Autowired
	private DeviceLookUpService deviceLookUpService;

	@Autowired
	private JSONUtility jsonUtility;

	@PUT
	@Path("/notification/printer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response processPrinter(String inputPayload) {
		try {
			RegisterPrinter regDeviceClaim = (RegisterPrinter) jsonUtility
					.unmarshal(RegisterPrinter.class, inputPayload);
			logger.info("api=printerNotificationApi , method=PUT, executionType=SyncFlow , executionState=STARTED , cloudId={}", regDeviceClaim.getCloudId());
			Validator validator = Validation.buildDefaultValidatorFactory()
					.getValidator();
			Set<ConstraintViolation<RegisterPrinter>> violations = validator
					.validate(regDeviceClaim);
			if (!violations.isEmpty()) {
				throw new InvalidNotificationPayloadException("PrinterClainmFromReg payload validation failed");
			}
			deviceLookUpService.processPrinter(regDeviceClaim);
			logger.info("api=printerNotificationApi , method=PUT, executionType=SyncFlow , executionState=COMPLETED , status=SUCCESS,  cloudId={}", regDeviceClaim.getCloudId());
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=printerNotificationApi,  method=PUT, executionState=COMPLETED, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE",response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}
}
