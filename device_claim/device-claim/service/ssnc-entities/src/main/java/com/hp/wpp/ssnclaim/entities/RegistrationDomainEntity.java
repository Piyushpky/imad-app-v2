package com.hp.wpp.ssnclaim.entities;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="registration_domain")
public class RegistrationDomainEntity {
	
	private  int domainIndex;
	private  String domainKey;

	
	public RegistrationDomainEntity() {
    }
	
	   
	    public RegistrationDomainEntity(int domainIndex, String domainKey) {
	    	  
	          this.domainIndex = domainIndex;
	          this.domainKey = domainKey;
	          
	    }

	    @DynamoDBHashKey(attributeName="domain_index")
	    public int getDomainIndex() {
			return domainIndex;
		}
		public void setDomainIndex(int domainIndex) {
			this.domainIndex = domainIndex;
		}
		@DynamoDBIndexHashKey(attributeName = "domain_key", globalSecondaryIndexName = "domain_key-index")	
		public String getDomainKey() {
			return domainKey;
		}
		public void setDomainKey(String domainKey) {
			this.domainKey = domainKey;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + domainIndex;
			result = prime * result
					+ ((domainKey == null) ? 0 : domainKey.hashCode());
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
			RegistrationDomainEntity other = (RegistrationDomainEntity) obj;
			if (domainIndex != other.domainIndex)
				return false;
			if (domainKey == null) {
				if (other.domainKey != null)
					return false;
			} else if (!domainKey.equals(other.domainKey))
				return false;
			return true;
		}


		@Override
		public String toString() {
			return "RegistrationDomain [domainIndex=" + domainIndex
					+ ", domainKey=" + domainKey + "]";
		}
	
}
