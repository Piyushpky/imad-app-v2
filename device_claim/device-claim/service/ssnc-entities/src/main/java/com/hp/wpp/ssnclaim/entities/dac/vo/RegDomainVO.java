package com.hp.wpp.ssnclaim.entities.dac.vo;

public class RegDomainVO {

	private final int domainIndex;
	private final String domainKey;
	
	 public RegDomainVO(int domainIndex, String domainKey) {
	        this.domainIndex = domainIndex;
	        this.domainKey = domainKey;
   }

	public int getDomainIndex() {
		return domainIndex;
	}

	public String getDomainKey() {
		return domainKey;
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
		RegDomainVO other = (RegDomainVO) obj;
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
		return "RegDomainVO [domainIndex=" + domainIndex + ", domainKey="
				+ domainKey + "]";
	}
}
