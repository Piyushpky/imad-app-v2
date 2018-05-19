package com.hp.wpp.ssnclaim.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ssn_reference_url")
public class LinksEntity {

	private String urlType;
	private String urlValue;

	public LinksEntity() {
	}

	public LinksEntity(String urlType, String urlValue) {
		this.urlType = urlType;
		this.urlValue = urlValue;
	}

	@DynamoDBHashKey(attributeName = "url_type")
	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public void setUrlValue(String urlValue) {
		this.urlValue = urlValue;
	}

	//@DynamoDBAttribute(attributeName = "url_value")
    @DynamoDBIndexHashKey(attributeName = "url_value", globalSecondaryIndexName = "url_value-index")
	public String getUrlValue() {
		return urlValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urlType == null) ? 0 : urlType.hashCode());
		result = prime * result
				+ ((urlValue == null) ? 0 : urlValue.hashCode());
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
		LinksEntity other = (LinksEntity) obj;
		if (urlType == null) {
			if (other.urlType != null)
				return false;
		} else if (!urlType.equals(other.urlType))
			return false;
		if (urlValue == null) {
			if (other.urlValue != null)
				return false;
		} else if (!urlValue.equals(other.urlValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LinksEntity [urlType=" + urlType + ", urlValue=" + urlValue
				+ "]";
	}

}
