package com.hp.wpp.ssnclaim.restapp.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hp.wpp.ssnc.common.util.JSONUtility;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ClaimInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.Error;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;

import static com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource.getErrorObject;


@Path("/v1")
@Component
public class SNKeyLookUpResource {

	@Autowired
	private DeviceLookUpService deviceLookUpService;

	@Autowired
	private LookUpResponseGenerator lookUpResponse;


	private static final WPPLogger logger = WPPLoggerFactory
			.getLogger(SNKeyLookUpResource.class);


	@GET
	@Path("/printer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwnership(@QueryParam("SN_key") String snKey) {
		logger.info("api=snKeyLookUpApi , method=GET, snKey=\"{}\", executionType=SyncFlow, executionState=STARTED", snKey);
		try {
			PrinterDataEntity printerDataLookUpEntity = deviceLookUpService.snKeyValidation(snKey);
			PrinterInfo ssnClaimInfo = createResponse(printerDataLookUpEntity, true);

			String resPayload = JSONUtility.marshal(ssnClaimInfo);
			logger.info("api=snKeyLookUpApi, method=GET, executionType=SyncFlow,  snKey=\"{}\"  , executionState=COMPLETED , status=SUCCESS",snKey);
			return Response.status(Status.OK).entity(resPayload).build();
		} catch (Exception e) {
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=snKeyLookUpApi ,method=GET, snKey=\"{}\", executionType=SyncFlow, executionState=COMPLETED, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE",snKey,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}

	@POST
	@Path(("/printer/claim_code/{printer_id}"))
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClaimCodeByPrinterId(@PathParam("printer_id") String printerId) {
		ClaimInfo claimInfo= null;
		try {
			logger.info("api=generateClaimCodeApi , method=POST, executionType=SyncFlow, cloudId={}, executionState=STARTED", printerId);
			claimInfo = deviceLookUpService.createClaimResponse(printerId);
			String resPayload=JSONUtility.marshal(claimInfo);
			logger.info("api=generateClaimCodeApi, method=POST, executionType=SyncFlow, cloudId={} ,claimCode=\"{}\" , executionState=COMPLETED , status=SUCCESS",printerId,claimInfo.getClaimCode());
			return Response.status(200).entity(resPayload).build();
		}  catch(Exception e){
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=generateClaimCodeApi , method=POST , cloudId={} , executionType=SyncFlow, executionState=COMPLETED, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE",printerId,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}

	@GET
	@Path("/printer/claim_code/{printer_id}/{claim_code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateClaimCode(@PathParam("printer_id") String printerId,@PathParam("claim_code") String claimCode) {
		logger.info("api=claimCodeValidationApi , method=GET, executionType=SyncFlow, cloudId={}, claimCode=\"{}\" ,executionState=STARTED", printerId,claimCode);
		try {
			PrinterDataEntity printerDataLookUpEntity = deviceLookUpService.getPrinterCodeData(printerId,claimCode);
			PrinterInfo ssnClaimInfo = createResponse(printerDataLookUpEntity, false);

			String resPayload = JSONUtility.marshal(ssnClaimInfo);
			logger.info("api=claimCodeValidationApi, method=GET, executionType=SyncFlow, cloudId={} ,claimCode=\"{}\" , executionState=COMPLETED , status=SUCCESS",printerId,claimCode);
			return Response.status(Status.OK).entity(resPayload).build();
		}  catch(Exception e){
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=claimCodeValidationApi , method=GET , cloudId={} , claimCode=\"{}\" , executionType=SyncFlow, executionState=COMPLETED, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE",printerId,claimCode,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}

	private PrinterInfo createResponse(PrinterDataEntity printerDataLookUpEntity, boolean snKeyBased) {
		return lookUpResponse.createResponse(printerDataLookUpEntity, snKeyBased);
	}

	
}
