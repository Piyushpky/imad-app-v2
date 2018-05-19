package com.hp.wpp.ssnclaim.entities.dac.vo;

/**
 * VO created to exchange Claim details between layers
 *
 */
public class PrinterDataLookUpVO {
	 
	private final String snKey;
	private final int domainIndex;
    private final Boolean isInkCapable;
    private final String printerId;
	private final String printerCode;
	private final int issuanceCounter;
	private final int ownership;
    private final Boolean overrun;


	/**
     * Value Object used to pass claim to the underlying service implementation
     *
     * @param snKey
     *         The Unique identifier for a claim
     * @param userId
     *         The user trying to claim
     * @param createdAt
     *         Date when the claim is created
     * @param name
     *         Alias name configured for the claim
     */
    public PrinterDataLookUpVO(String snKey, int domainIndex, Boolean isInkCapable, String printerId, String printerCode,int issuanceCounter,int ownership, Boolean overrun) {
        this.snKey = snKey;
        this.domainIndex = domainIndex;
        this.isInkCapable=isInkCapable;
        this.printerId = printerId;
         this.printerCode = printerCode;
        this.issuanceCounter = issuanceCounter;
        this.ownership = ownership;
        this.overrun = overrun;
        
    }
    
    public String getSnKey() {
		return snKey;
	}
	public int getDomainIndex() {
		return domainIndex;
	}

	public Boolean getIsInkCapable() {
		return isInkCapable;
	}

	public String getPrinterId() {
		return printerId;
	}
	
    public String getPrinterCode() {
		return printerCode;
	}

	public int getIssuanceCounter() {
		return issuanceCounter;
	}
  public int getOwnership() {
		return ownership;
	}

	public Boolean getOverrun() {
		return overrun;
	}

		
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + domainIndex;
		result = prime * result + ((isInkCapable == null) ? 0 : isInkCapable.hashCode());
		result = prime * result + issuanceCounter;
		result = prime * result + ((overrun == null) ? 0 : overrun.hashCode());
		result = prime * result + ownership;
		result = prime * result + ((printerCode == null) ? 0 : printerCode.hashCode());
		result = prime * result + ((printerId == null) ? 0 : printerId.hashCode());
		result = prime * result + ((snKey == null) ? 0 : snKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrinterDataLookUpVO other = (PrinterDataLookUpVO) obj;
		if (domainIndex != other.domainIndex)
			return false;
		if (isInkCapable == null) {
			if (other.isInkCapable != null)
				return false;
		} else if (!isInkCapable.equals(other.isInkCapable))
			return false;
		if (issuanceCounter != other.issuanceCounter)
			return false;
		if (overrun == null) {
			if (other.overrun != null)
				return false;
		} else if (!overrun.equals(other.overrun))
			return false;
		if (ownership != other.ownership)
			return false;
		if (printerCode == null) {
			if (other.printerCode != null)
				return false;
		} else if (!printerCode.equals(other.printerCode))
			return false;
		if (printerId == null) {
			if (other.printerId != null)
				return false;
		} else if (!printerId.equals(other.printerId))
			return false;
		if (snKey == null) {
			if (other.snKey != null)
				return false;
		} else if (!snKey.equals(other.snKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrinterDataLookUpVO [snKey=" + snKey + ", domainIndex=" + domainIndex
				+ ", isInkCapable=" + isInkCapable + ", printerId=" + printerId
				+ ", printerCode=" + printerCode + "]";
	}
}
