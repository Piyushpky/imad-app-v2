package com.hp.wpp.ssnclaim.restapp.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.exception.*;

import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.AVDlsErrors;


public class PrinterClaimResourceHelper {
	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(PrinterClaimResourceHelper.class);
	private PrinterClaimResourceHelper(){};
	public static Response actOnValidationFailure(Throwable e) {
		Status httpStatusCode = null;
		if (e instanceof InvalidHeaderSignatureException || e instanceof InvalidPrinterCodeException ){
			return buildResponse(Status.NOT_FOUND, AVDlsErrors.AVDLS000003);
		}else if(e instanceof ValidationDataMismatchException){
			return buildResponse(Status.BAD_REQUEST, AVDlsErrors.AVDLS000304);
		}else if(e instanceof InvalidClaimCodeException){
			return buildResponse(Status.BAD_REQUEST, AVDlsErrors.AVDLS000303);
		}
		else if(e instanceof InvalidNotificationPayloadException){
			return buildResponse(Status.BAD_REQUEST, AVDlsErrors.AVDLS000202);
		}
		else if(e instanceof InvalidPrinterCodeLengthException){
			return buildResponse(Status.BAD_REQUEST, AVDlsErrors.AVDLS000002);
		}
		else if( e instanceof CloudIdNotFoundException ) {
			return buildResponse(Status.NOT_FOUND, AVDlsErrors.AVDLS000302);
		} else if (e instanceof SNKeyNotFoundException) {
			return buildResponse(Status.NOT_FOUND, AVDlsErrors.AVDLS000102);
		}else if (e instanceof AvDlsDynamoDBException) {
			return buildResponse(Status.SERVICE_UNAVAILABLE, AVDlsErrors.AVDLS100001);
		}
		else if( e instanceof UUIDNotFoundException ) {
			return buildResponse(Status.NOT_FOUND, AVDlsErrors.AVDLS000601);
		}
		else if( e instanceof InvalidUUIDException) {
			return buildResponse(Status.BAD_REQUEST, AVDlsErrors.AVDLS000602);
		}
		else {
			httpStatusCode = Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpStatusCode).build();
	}

	private static Response buildResponse(Status httpStatus, AVDlsErrors avDlsErrors) {
		if(avDlsErrors != null)
			try {
				return Response.status(httpStatus.getStatusCode()).header("Internal-Error-Code",avDlsErrors.getWPPErrorModel().getErrors().get(0).getCode()).entity(avDlsErrors.getWPPErrorModel().toJsonAsString()).build();
			} catch (JsonProcessingException e) {
				LOG.error("Error while converting AVDLSError Object.ErrorMsg=\"{}\" "+e.getMessage());
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		else
			return Response.status(httpStatus.getStatusCode()).build();
	}
}
