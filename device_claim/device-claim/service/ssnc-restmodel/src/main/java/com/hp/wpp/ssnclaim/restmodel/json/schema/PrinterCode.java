package com.hp.wpp.ssnclaim.restmodel.json.schema;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;

public class PrinterCode {
	@NotNull
	@JsonProperty("version")
	private String version;
		
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@NotNull	
	@JsonProperty("printer_code")
	private String printerCode;
	
	public String getPrinterCode() {
		return printerCode;
	}

	public void setPrinterCode(String printerCode) {
		this.printerCode = printerCode;
	}

}
