package com.hp.wpp.ssnclaim.restapp.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnc.common.enums.CodeType;
import com.hp.wpp.ssnc.common.util.JSONUtility;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.exception.InvalidPrinterCodeLengthException;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterCodeInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.Error;
import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.Errors;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.response.PrinterCodeResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Path("/v1")
@Component
public class PrinterCodeResource {

	private static final WPPLogger logger = WPPLoggerFactory
			.getLogger(PrinterCodeResource.class);

	@Autowired
	private DeviceLookUpService deviceLookUpService;

	@Autowired
	private PrinterCodeResponseGenerator printerCodeResponse;

	private static final String REGEX_SEPERATOR = "[.]|_|-| ";

	@POST
	@Path("/SSN_info/{SSN}")
	// @Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response printerClaim(@PathParam("SSN") String code) {
		try {
			PrinterCodeData printerCodeFields = new PrinterCodeData();
			String resPayload = null;
			code = cleanseClaimCode(code);
			CodeType codeType = getCodeType(code);
			// TODO: remove SSN code after some time
			if (codeType == CodeType.SSNCODE) {
				logger.info("api=printerClaim, method=POST , executionType=SyncFlow, sSnCode={}, executionState=STARTED", code);
				SSNFields ssnFields = deviceLookUpService.validateSSNCode(code);
				printerCodeFields = getPrinterFieldsFromSsn(code, ssnFields);
			} else if (codeType == CodeType.PRINTERCODE) {
				logger.info("api=printerClaim, method=POST, executionType=SyncFlow, printerCode={}, executionState=STARTED", code);
				printerCodeFields = deviceLookUpService
						.validatePrinterCode(code);
			} else {
				throw new InvalidPrinterCodeLengthException("PrinterCode Length is Invalid");
			}
			PrinterDataEntity printerDataLookUpEntity = deviceLookUpService
					.processPrinterCodeData(printerCodeFields);
			PrinterInfo ssnClaimInfo = createResponse(printerCodeFields,
					printerDataLookUpEntity);
			resPayload = JSONUtility.marshal(ssnClaimInfo);
			logger.info("api=printerClaim , method=POST, executionType=SyncFlow , cloudId={}  ,snKey=\"{}\", printerCode={}, executionState=COMPLETED , status=SUCCESS",ssnClaimInfo.getPrinterId(),ssnClaimInfo.getSnKey(),code);
			return Response.status(Status.OK).entity(resPayload).build();
		} catch (Exception e) {
			 Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=printerClaim  , method=POST, executionType=SyncFlow, executionState=COMPLETED , printerCode={}, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE", code,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}

	}
	@GET
	@Path("/printer_code_info/{PrinterCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrinterClaimData(@PathParam("PrinterCode")String code)  {
		try {
			PrinterCodeData printerCodeFields = new PrinterCodeData();
			String resPayload = null;
			code = cleanseClaimCode(code);
			CodeType codeType = getCodeType(code);
			 if (codeType == CodeType.PRINTERCODE) {
				 logger.info("api=printerCodeInfo , method=GET, executionType=SyncFlow , printerCode={}, executionState=STARTED", code);
				printerCodeFields = deviceLookUpService
						.validatePrinterCode(code);
			} else {
				 throw new InvalidPrinterCodeLengthException("PrinterCode Length is Invalid");
			}
			PrinterDataEntity printerDataLookUpEntity = deviceLookUpService
					.getPrinterCodeData(printerCodeFields);
			PrinterCodeInfo ssnClaimInfo = printerCodeResponse.createPrinterCodeResponse(printerCodeFields,
					printerDataLookUpEntity);
			resPayload = JSONUtility.marshal(ssnClaimInfo);
			logger.info("api=printerCodeInfo, method=GET, executionType=SyncFlow, cloudId={} , snKey=\"{}\", printerCode={}, executionState=COMPLETED , status=SUCCESS",ssnClaimInfo.getCloudId(),ssnClaimInfo.getSnKey(),code);
			return Response.status(Status.OK).entity(resPayload).build();
		}catch (Exception e) {
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=printerCodeInfo, method=GET, executionType=SyncFlow, executionState=COMPLETED, printerCode={}, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE", code,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}
	/**
	 * @deprecated (used only for CTF)
	 */
    @Deprecated
	@DELETE
	@Path("/printer_code_info/{PrinterCode}")
	public Response deletePrinterData(@PathParam("PrinterCode")String code)  {
		logger.info("Printer Data delete request received for printercode={}", code);

		try {
			PrinterCodeData printerCodeFields = null;
			code = cleanseClaimCode(code);
			CodeType codeType = getCodeType(code);
			if (codeType == CodeType.PRINTERCODE) {
				logger.info("api=printerDataDeleteApi , method=DELETE, executionType=SyncFlow ,printerCode={}, executionState=STARTED", code);
				printerCodeFields = deviceLookUpService
						.validatePrinterCode(code);
			} else {
				throw new InvalidPrinterCodeLengthException("PrinterCode Length is Invalid");
			}

			deviceLookUpService
					.deletePrinterCodeData(printerCodeFields);

			logger.info("api=printerDataDeleteApi, method=DELETE, executionType=SyncFlow,printerCode={}, executionState=COMPLETED , status=SUCCESS",code);
			return Response.status(Status.OK).build();

		} catch (Exception e) {
			Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
			Error error= getErrorObject(response);
			logger.error("api=DeletePrinterClaim,  method=DELETE, executionState=COMPLETED, printerCode={}, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE", code,response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
			return response;
		}
	}

	private CodeType getCodeType(String code) {
		return CodeType.getCodeType(code.length());
	}

	private PrinterCodeData getPrinterFieldsFromSsn(String code,
			SSNFields ssnFields) {
		PrinterCodeData printerCodeFields = new PrinterCodeData();
		BeanUtils.copyProperties(ssnFields, printerCodeFields);
		printerCodeFields.setOwnership(0);
		printerCodeFields.setPrinterCode(code);
		return printerCodeFields;
	}

	private PrinterInfo createResponse(PrinterCodeData printerCodeFields,
			PrinterDataEntity printerDataLookUpEntity) {
		PrinterInfo printerInfo = printerCodeResponse.createResponse(
				printerCodeFields, printerDataLookUpEntity);
		return printerInfo;
	}

	private String cleanseClaimCode(String code) {
		String trimmedCode = code.replaceAll(REGEX_SEPERATOR, "");

		trimmedCode = trimmedCode.toUpperCase();
		return trimmedCode;

	}
	public static Error getErrorObject(Response response) {
		Error error = null;
		if (response.getEntity() != null) {
			String errorResponse = (String) response.getEntity();
			try {
				Errors errors = Errors.fromJsonAsBytes(errorResponse.getBytes());
				error = errors.getErrors().get(0);
			} catch (IOException e) {
				error = null;
			}
		}
		return error;
	}
}
