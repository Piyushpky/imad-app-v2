package com.hp.wpp.ssnclaim.restmodel.json.schema;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "make_and_model_family", "images", "links" })
public class PrinterGenericInfo {
	
	@JsonProperty("make_and_model_family")
	private String makeAndModelFamily;
	
	private List<String> images;
	
	private List<Link> links;

	public String getMakeAndModelFamily() {
		return makeAndModelFamily;
	}

	public void setMakeAndModelFamily(String makeAndModelFamily) {
		this.makeAndModelFamily = makeAndModelFamily;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	

}
