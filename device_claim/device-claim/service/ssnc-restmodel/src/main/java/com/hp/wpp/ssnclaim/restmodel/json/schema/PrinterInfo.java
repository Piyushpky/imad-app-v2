package com.hp.wpp.ssnclaim.restmodel.json.schema;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class PrinterInfo {

	@JsonProperty("version")
    protected String version;
    
    @JsonProperty("sn_key")
    protected String snKey;
  
    @JsonProperty("printer_id")
    protected String printerId;
    
    @JsonProperty("is_ink_capable")
    protected Boolean isInkCapable;
    
    @JsonProperty("is_registered")
    protected Boolean isPrinterRegistered;

    @JsonProperty("claim_code")
    protected String claimCode;
    
    @JsonProperty("links")
    protected List<Link> configurations;

    public String getVersion() {
        return version;
    }

    public void setConfigurations(List<Link> configurations) {
		this.configurations = configurations;
	}

	public void setVersion(String value) {
        this.version = value;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String value) {
        this.printerId = value;
    }

    public List<Link> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList();
        }
        return this.configurations;
    }

	public String getSnKey() {
		return snKey;
	}

	public void setSnKey(String snKey) {
		this.snKey = snKey;
	}

	public Boolean getIsInkCapable() {
		return isInkCapable;
	}

	public void setIsInkCapable(Boolean isInkCapable) {
		this.isInkCapable = isInkCapable;
	}

	public Boolean getIsPrinterRegistered() {
		return isPrinterRegistered;
	}

	public void setIsPrinterRegistered(Boolean isPrinterRegistered) {
		this.isPrinterRegistered = isPrinterRegistered;
	}
    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

}
