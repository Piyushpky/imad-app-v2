package com.hp.wpp.ssnclaim.restapp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hp.wpp.ssnclaim.restmodel.json.schema.RequestUUID;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ResponseUUID;

import com.hp.wpp.ssnc.common.util.JSONUtility;
import com.hp.wpp.ssnclaim.restmodel.json.schema.errors.Error;
import com.hp.wpp.ssnclaim.service.response.LookUpResponseGenerator;
import com.hp.wpp.ssnclaim.service.ssn.service.DeviceLookUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

import static com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource.getErrorObject;

/**
 * Created by mvsnbharath on 19/12/2017.
 */

@Path("/v1")
@Component

public class UUIDLookupResource {
    @Autowired
    private DeviceLookUpService deviceLookUpService;

    @Autowired
    private LookUpResponseGenerator lookUpResponse;

    private static final WPPLogger logger = WPPLoggerFactory
            .getLogger(UUIDLookupResource.class);

    @POST
    @Path("/printer/discovery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response getcloudId(RequestUUID requestUUID) {
        try {
                logger.info("api=cloudIdLookupUpApi , method=POST, uuid=\"{}\", executionType=SyncFlow, executionState=STARTED", requestUUID.getUuid());
                ResponseUUID responseUUID = deviceLookUpService.getPrinterInfo(requestUUID.getUuid());
                String resPayload = JSONUtility.marshal(responseUUID);
                logger.info("api=cloudIdLookupUpApi, method=POST, executionType=SyncFlow,  uuid=\"{}\"  , executionState=COMPLETED , status=SUCCESS", requestUUID.getUuid());
                return Response.status(200).entity(resPayload).build();
        }
        catch(Exception e) {
            Response response = PrinterClaimResourceHelper.actOnValidationFailure(e);
            Error error= getErrorObject(response);
            logger.error("api=cloudIdLookupUpApi , method=POST , uuid=\"{}\" , executionType=SyncFlow, executionState=COMPLETED, statusCode={}, internalErrorCode={}, failureReason=\"{}\",  status=FAILURE",requestUUID.getUuid(), response.getStatus(), (error!=null)?error.getCode():"none",  e.getMessage());
            return response;
        }
    }
}
