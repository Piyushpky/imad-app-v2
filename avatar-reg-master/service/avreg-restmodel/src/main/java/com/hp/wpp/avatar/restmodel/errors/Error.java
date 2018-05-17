/**
 * 
 */
package com.hp.wpp.avatar.restmodel.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mahammad
 *
 */
@JsonPropertyOrder({ "service_name", "version", "http_code", "code", "description", "description_link", "service_build", "details" })
@JsonInclude(Include.NON_DEFAULT)
public class Error {

	@JsonProperty("service_name")
	protected String serviceName;
	
	protected String version;
	
	protected String code;
	
	@JsonProperty("http_code")
	protected String httpCode;
	
	protected String description;
	
	@JsonProperty("description_link")
	protected String descriptionLink;
	
	@JsonProperty("service_build")
	protected String serviceBuild;
	
	protected Error.Details details;

	/**
	 * Gets the value of the serviceName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the value of the serviceName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setServiceName(String value) {
		this.serviceName = value;
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCode(String value) {
		this.code = value;
	}

	/**
	 * Gets the value of the httpCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getHttpCode() {
		return httpCode;
	}

	/**
	 * Sets the value of the httpCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setHttpCode(String value) {
		this.httpCode = value;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the descriptionLink property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescriptionLink() {
		return descriptionLink;
	}

	/**
	 * Sets the value of the descriptionLink property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescriptionLink(String value) {
		this.descriptionLink = value;
	}

	/**
	 * Gets the value of the serviceBuild property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getServiceBuild() {
		return serviceBuild;
	}

	/**
	 * Sets the value of the serviceBuild property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setServiceBuild(String value) {
		this.serviceBuild = value;
	}

	/**
	 * Gets the value of the details property.
	 * 
	 * @return possible object is {@link Errors.Details }
	 * 
	 */
	public Error.Details getDetails() {
		return details;
	}

	/**
	 * Sets the value of the details property.
	 * 
	 * @param value
	 *            allowed object is {@link Errors.Details }
	 * 
	 */
	public void setDetails(Error.Details value) {
		this.details = value;
	}

	public static class Details {

		protected List<Object> any;

		/**
		 * Gets the value of the any property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live listnot a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the any property.
		 * 
		 * <p>
		 * For exampleto add a new itemdo as follows:
		 * 
		 * <pre>
		 * getAny().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Object }
		 * 
		 * 
		 */
		public List<Object> getAny() {
			if (any == null) {
				any = new ArrayList<Object>();
			}
			return this.any;
		}

	}

}
