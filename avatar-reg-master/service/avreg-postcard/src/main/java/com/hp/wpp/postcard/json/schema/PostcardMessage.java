package com.hp.wpp.postcard.json.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "content_type", "compression", "encryption", "iv", "content" })
public class PostcardMessage {

	@JsonProperty("content_type")
	private String contentType;
	
	private PostcardCompression compression;
	
	private PostcardEncryption encryption;
	
	private String iv;
	
	private String content;

	/**
	 * Gets the value of the contentType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the value of the contentType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setContentType(String value) {
		this.contentType = value;
	}

	/**
	 * Gets the value of the compression property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public PostcardCompression getCompression() {
		return compression;
	}

	/**
	 * Sets the value of the compression property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCompression(PostcardCompression value) {
		this.compression = value;
	}

	/**
	 * Gets the value of the encryption property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public PostcardEncryption getEncryption() {
		return encryption;
	}

	/**
	 * Sets the value of the encryption property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEncryption(PostcardEncryption value) {
		this.encryption = value;
	}

	/**
	 * Gets the value of the iv property.
	 * 
	 * @return possible object is String
	 */
	public String getIv() {
		return iv;
	}

	/**
	 * Sets the value of the iv property.
	 * 
	 * @param value
	 *            allowed object is String
	 */
	public void setIv(String value) {
		this.iv = value;
	}

	/**
	 * Gets the value of the content property.
	 * 
	 * @return possible object is String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the value of the content property.
	 * 
	 * @param value
	 *            allowed object is String
	 */
	public void setContent(String value) {
		this.content = value;
	}

}
