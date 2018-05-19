package com.hp.wpp.ssnclaim.restapp.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hp.wpp.ssnc.common.util.JSONUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.restmodel.json.schema.Link;
import com.hp.wpp.ssnclaim.restmodel.json.schema.PrinterGenericInfo;


@Path("/v1")
@Component
public class PrinterGenericInfoResource {
	
	private static final WPPLogger LOG = WPPLoggerFactory
			.getLogger(PrinterGenericInfoResource.class);

	@Autowired
	private JSONUtility jsonUtility;
	
	@GET
	@Path("/printers/generic/{domain-id}/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrinterGenericInfo(@PathParam("domain-id") String domainIndex){
		LOG.debug("Printer Generic info request with domain-Id: {}",domainIndex);
		
		 String responsePayload = getResponse(domainIndex);
		LOG.debug("response payload: {}",responsePayload);
		return Response.status(Status.OK)
				.entity(responsePayload).build();
	}

	private String getResponse(String domainIndex) {
		PrinterGenericInfo info=new PrinterGenericInfo();
		info.setMakeAndModelFamily("Officejet Pro 6830");
		List<String> images = new ArrayList();
		images.add("https://s3-us-west-2.amazonaws.com/printers-images/palermo/palermo-42x26.png");
		images.add("https://s3-us-west-2.amazonaws.com/printers-images/palermo/palermo-130x102.png");
		images.add("https://s3-us-west-2.amazonaws.com/printers-images/palermo/palermo-170x128.png");
		images.add("https://s3-us-west-2.amazonaws.com/printers-images/palermo/palermo-300x245.png");
		
		info.setImages(images);
		
		List<Link> links =  new ArrayList();
		Link link =  new Link();
		link.setHref(String.format("href://https://vp.hpeprint.com/virtualprinter/v1/printers/generic/%s/info",domainIndex));
		link.setRel("self");
		links.add(link);
		info.setLinks(links );
		return jsonUtility.marshal(info);
	}
	
	@GET
	@Path("/printers/{domain-id}/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrinterInfo(@PathParam("domain-id") String domainIndex){
		LOG.debug("Printer Generic info request with domain-Id: {}",domainIndex);
		
		 String responsePayload = getResponse(domainIndex);
		LOG.debug("response payload: {}",responsePayload);
		return Response.status(Status.OK)
				.entity(responsePayload).build();
	}
}
