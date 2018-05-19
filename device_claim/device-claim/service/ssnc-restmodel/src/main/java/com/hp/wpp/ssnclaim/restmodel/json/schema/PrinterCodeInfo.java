package com.hp.wpp.ssnclaim.restmodel.json.schema;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kumaniti on 1/12/2017.
 */
public class PrinterCodeInfo {
    @JsonProperty("version")
    protected String version;

    @JsonProperty("sn_key")
    protected String snKey;

    @JsonProperty("cloud_id")
    protected String cloudId;

    @JsonProperty("is_registered")
    protected Boolean isPrinterRegistered;

    public PrevPrinterCode getPrevPrinterCode() {
        return prevPrinterCode;
    }

    public CurrentPrinterCode getCurrentPrinterCode() {
        return currentPrinterCode;
    }

    public void setCurrentPrinterCode(CurrentPrinterCode currentPrinterCode) {
        this.currentPrinterCode = currentPrinterCode;
    }

    public void setPrevPrinterCode(PrevPrinterCode prevPrinterCode) {
        this.prevPrinterCode = prevPrinterCode;
    }

    @JsonProperty("printer_code")
    public PrinterCodeInfo.CurrentPrinterCode currentPrinterCode;

    @JsonProperty("prev_printer_code")
    public PrinterCodeInfo.PrevPrinterCode prevPrinterCode;

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    @JsonProperty("claim_code")
    protected String claimCode;


    @JsonProperty("printer_uuid")
    protected String deviceUUID;

    @JsonProperty("links")
    protected List<Link> configurations;


    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getVersion() {
        return version;
    }

    public void setConfigurations(List<Link> configurations) {
        this.configurations = configurations;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String value) {
        this.cloudId = value;
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


    public Boolean getIsPrinterRegistered() {
        return isPrinterRegistered;
    }

    public void setIsPrinterRegistered(Boolean isPrinterRegistered) {
        this.isPrinterRegistered = isPrinterRegistered;
    }

    public static   class CurrentPrinterCode {

        @JsonProperty("is_ink_capable")
        protected boolean isInkCapable;

        @JsonProperty("ownership_counter")
        protected int ownershipCounter;

        @JsonProperty("issuance_counter")
        protected int issuanceCounter;

        @JsonProperty("overrun_bit")
        protected boolean overrunBit;

        public int getOwnershipCounter() {
            return ownershipCounter;
        }

        public void setOwnershipCounter(int ownershipCounter) {
            this.ownershipCounter = ownershipCounter;
        }

        public int getIssuanceCounter() {
            return issuanceCounter;
        }

        public void setIssuanceCounter(int issuanceCounter) {
            this.issuanceCounter = issuanceCounter;
        }

        public boolean isOverrunBit() {
            return overrunBit;
        }

        public void setOverrunBit(boolean overrunBit) {
            this.overrunBit = overrunBit;
        }


        public Boolean getIsInkCapable() {
            return isInkCapable;
        }

        public void setIsInkCapable(Boolean isInkCapable) {
            this.isInkCapable = isInkCapable;
        }

        }

    public static class PrevPrinterCode {

        @JsonProperty("is_ink_capable")
        protected boolean isInkCapable;

        @JsonProperty("ownership_counter")
        protected int ownershipCounter;

        @JsonProperty("issuance_counter")
        protected int issuanceCounter;

        @JsonProperty("overrun_bit")
        protected boolean overrunBit;

        public int getOwnershipCounter() {
            return ownershipCounter;
        }

        public void setOwnershipCounter(int ownershipCounter) {
            this.ownershipCounter = ownershipCounter;
        }

        public int getIssuanceCounter() {
            return issuanceCounter;
        }

        public void setIssuanceCounter(int issuanceCounter) {
            this.issuanceCounter = issuanceCounter;
        }

        public boolean isOverrunBit() {
            return overrunBit;
        }

        public void setOverrunBit(boolean overrunBit) {
            this.overrunBit = overrunBit;
        }


        public Boolean getIsInkCapable() {
            return isInkCapable;
        }

        public void setIsInkCapable(Boolean isInkCapable) {
            this.isInkCapable = isInkCapable;
        }

    }
}

