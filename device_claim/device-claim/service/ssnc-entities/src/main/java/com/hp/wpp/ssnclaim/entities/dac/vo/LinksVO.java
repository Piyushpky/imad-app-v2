package com.hp.wpp.ssnclaim.entities.dac.vo;

public class LinksVO {

	private final String urlType;
	private final String urlValue;
	
	public LinksVO(String urlType, String urlValue) {
        this.urlType = urlType;
        this.urlValue = urlValue;
}

	

	public String getUrlType() {
		return urlType;
	}



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
		LinksVO other = (LinksVO) obj;
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
		return "LinksVO [urlType=" + urlType + ", urlValue=" + urlValue + "]";
	}
	
}

