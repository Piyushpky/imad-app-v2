package com.hp.wpp.avatar.restapp.resources;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restapp.util.BeanConverterUtil;
import com.hp.wpp.avatar.restapp.util.JSONUtility;
import com.hp.wpp.avatar.restmodel.json.schema.EntityInformation;
import com.hp.wpp.logger.MDCConstants;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

@Path("/v1")
@Component
public class EntityInfoResource {
    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(EntityInfoResource.class);
    private static final String ENTITY_INFO_URL = "/entities/entity_info/{cloud_id}";

    @Autowired
    private EntityTypeProcessorMap entityTypeProcessorMap;

    public void setEntityTypeProcessorMap(EntityTypeProcessorMap entityTypeProcessorMap) {
        this.entityTypeProcessorMap = entityTypeProcessorMap;
    }

    @GET
    @Path(ENTITY_INFO_URL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntityInfo(@PathParam("cloud_id") String cloudId){
    	
    	String traceId = getLogUUID();
		MDC.put(MDCConstants.UID, traceId);
		
    	LOG.info("api=entityInfo; method=GET; executionType=SyncFlow; cloudId={}; executionState=STARTED",cloudId);

        try {
            EntityIdentificationBO entityIdentificationBO = (entityTypeProcessorMap.getEntityRegistrationProcessor(EntityType.devices)).getEntityIdentificationBO(cloudId);
            EntityInformation entityInformation = BeanConverterUtil.createEntityInformationFromBO(entityIdentificationBO);
            entityInformation.setEntityType(EntityType.devices);
            String responsePayload = JSONUtility.marshal(entityInformation);
            
            LOG.info("api=entityInfo; method=GET; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=SUCCESS", cloudId);
            return Response.ok().entity(responsePayload).build();
        } catch (Exception e) {
        	
        	LOG.error("api=entityInfo; method=GET; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, e);
            if(e instanceof EntityNotRegisteredException)
                return Response.status(Response.Status.NOT_FOUND).build();

            return Response.serverError().build();
        }finally {
            MDC.clear();
        }
    }
    
    
    private String getLogUUID() {
		return UUID.randomUUID().toString();
	}

}
