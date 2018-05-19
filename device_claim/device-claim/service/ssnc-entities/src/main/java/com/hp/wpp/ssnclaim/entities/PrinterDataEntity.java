package com.hp.wpp.ssnclaim.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "printer_data")
public class PrinterDataEntity {

	private  String snKey;
	private  int domainIndex;
	private  Boolean isInkCapable;
	private  String cloudId;
	private String printerCode;
	private int issuanceCounter;
	private int ownershipCounter;
	private Boolean overRunBit;
	private Boolean isRegistered;
	private String deviceUUID;
	/**
	 * The default constructor. This is needed by AWSDynamoDBClient for object
	 * creation.
	 */
	public PrinterDataEntity() {
	}

	/**
	 * Claim entity used for DynamoDB
	 *
	 * @param snKey
	 *            The Unique identifier for a claim
	 * @param userId
	 *            The user trying to claim
	 * @param createdAt
	 *            Date when the claim is created
	 * @param name
	 *            Alias name configured for the claim
	 */
	public PrinterDataEntity(String snKey, int domainIndex,
							 Boolean isInkCapable, String printerId) {
		this.snKey = snKey;
		this.domainIndex = domainIndex;
		this.isInkCapable = isInkCapable;
		this.cloudId = printerId;
	}

	@DynamoDBHashKey(attributeName = "sn_key")
	public String getSnKey() {
		return snKey;
	}

	public void setSnKey(String snKey) {
		this.snKey = snKey;
	}

	@DynamoDBAttribute(attributeName = "domain_index")
	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}

	@DynamoDBAttribute(attributeName = "is_ink_capable")
	public boolean isInkCapable() {
		return isInkCapable;
	}

	public void setInkCapable(boolean isInkCapable) {
		this.isInkCapable = isInkCapable;
	}
	@DynamoDBIndexHashKey(attributeName = "cloud_id", globalSecondaryIndexName = "cloud_id-index")
	//@DynamoDBAttribute(attributeName = "cloud_id")
	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	@DynamoDBAttribute(attributeName = "printer_code")
	public String getPrinterCode() {
		return printerCode;
	}

	public void setPrinterCode(String printerCode) {
		this.printerCode = printerCode;
	}

	@DynamoDBAttribute(attributeName = "issuance_counter")
	public int getIssuanceCounter() {
		return issuanceCounter;
	}

	public void setIssuanceCounter(int issuanceCounter) {
		this.issuanceCounter = issuanceCounter;
	}

	@DynamoDBAttribute(attributeName="ownership")
	public int getOwnershipCounter() {
		return ownershipCounter;
	}

	public void setOwnershipCounter(int ownershipCounter) {
		this.ownershipCounter = ownershipCounter;
	}

	@DynamoDBAttribute(attributeName="overrun")
	public Boolean getOverRunBit() {
		return overRunBit;
	}

	public void setOverRunBit(Boolean overRunBit) {
		this.overRunBit = overRunBit;
	}

	@DynamoDBAttribute(attributeName="is_registered")
	public Boolean getIsRegistered() {
		return isRegistered==null? cloudId!=null:isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((snKey == null) ? 0 : snKey.hashCode());
		return result;
	}

	@DynamoDBIndexHashKey(attributeName = "uuid", globalSecondaryIndexName = "uuid-index")
	public String getdeviceUUID() {
		return deviceUUID;
	}

	public void setdeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrinterDataEntity other = (PrinterDataEntity) obj;
		if (snKey == null) {
			if (other.snKey != null)
				return false;
		} else if (!snKey.equals(other.snKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrinterDataLookUp [snKey=" + snKey + ", domainIndex="
				+ domainIndex + ", isInkCapable=" + isInkCapable
				+ ", printerId=" + cloudId + ", printerCode=" + printerCode
				+ ", deviceUUID=" + deviceUUID + "]";
	}
}