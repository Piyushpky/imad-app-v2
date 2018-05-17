package com.hp.wpp.postcard.json.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "postcard_signed_info", "hash", "signatures" })
public class PostcardContent {
	
	@Valid
	@JsonProperty("postcard_signed_info")
	private PostcardContent.PostcardSignedInfo postcardSignedInfo;
	
	@NotBlank
	private String hash;
	
	@Valid
	private List<PostcardContent.Signatures> signatures;

	/**
	 * Gets the value of the postcardSignedInfo property.
	 * 
	 * @return possible object is {@link PostcardContent.PostcardSignedInfo }
	 * 
	 */
	public PostcardContent.PostcardSignedInfo getPostcardSignedInfo() {
		return postcardSignedInfo;
	}

	/**
	 * Sets the value of the postcardSignedInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link PostcardContent.PostcardSignedInfo }
	 * 
	 */
	public void setPostcardSignedInfo(PostcardContent.PostcardSignedInfo value) {
		this.postcardSignedInfo = value;
	}

	/**
	 * Gets the value of the hash property.
	 * 
	 * @return possible object is byte[]
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Sets the value of the hash property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setHash(String value) {
		this.hash = value;
	}

	/**
	 * Gets the value of the signatures property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the signatures property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSignatures().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PostcardContent.Signatures }
	 * 
	 * 
	 */
	public List<PostcardContent.Signatures> getSignatures() {
		if (signatures == null) {
			signatures = new ArrayList<PostcardContent.Signatures>();
		}
		return this.signatures;
	}

	@JsonPropertyOrder({ "version", "mime_type", "application_id", "entity_id", "creator", "postcard_id", "timestamp", "seq_num", "key_id", "keys", "messages", "control" })
	public static class PostcardSignedInfo {
		
		@NotBlank
		private String version;
		
		@NotBlank
		@Pattern(regexp = "application\\/vnd\\.hpi\\.postcard\\+json")
		@JsonProperty("mime_type")
		private String mimeType;
		
		@NotBlank
		@Range(min = 1, max = 3, message = "Applications expected are 1: Gen2 Avatar Registration service 2: Instant Ink Gemini or 3: OOBE")
		@JsonProperty("application_id")
		private String applicationId;
		
		@NotBlank
		@JsonProperty("entity_id")
		private String entityId;
		
		@NotNull
		@JsonProperty("creator")
		private PostcardCreator creator;
		
		@NotBlank
		@JsonProperty("postcard_id")
		private String postcardId;
		
		@JsonProperty("timestamp")
		private String timestamp;
		
		@JsonProperty("seq_num")
		private BigInteger seqNum;
		
		@NotBlank
		@JsonProperty("key_id")
		private String keyId;
		
		private List<PostcardKey> keys;
		
		private List<PostcardMessage> messages;
		
		private PostcardMessage control;

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
		 * Gets the value of the mimeType property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getMimeType() {
			return mimeType;
		}

		/**
		 * Sets the value of the mimeType property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setMimeType(String value) {
			this.mimeType = value;
		}

		/**
		 * Gets the value of the applicationId property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getApplicationId() {
			return applicationId;
		}

		/**
		 * Sets the value of the applicationId property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setApplicationId(String value) {
			this.applicationId = value;
		}

		/**
		 * Gets the value of the deviceId property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getEntityId() {
			return entityId;
		}

		/**
		 * Sets the value of the deviceId property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setEntityId(String value) {
			this.entityId = value;
		}

		/**
		 * Gets the value of the creator property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public PostcardCreator getCreator() {
			return creator;
		}

		/**
		 * Sets the value of the creator property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setCreator(PostcardCreator value) {
			this.creator = value;
		}

		/**
		 * Gets the value of the postcardId property.
		 * 
		 * @return possible object is byte[]
		 */
		public String getPostcardId() {
			return postcardId;
		}

		/**
		 * Sets the value of the postcardId property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setPostcardId(String value) {
			this.postcardId = value;
		}

		/**
		 * Gets the value of the timestamp property.
		 * 
		 * @return possible object is {@link XMLGregorianCalendar }
		 * 
		 */
		public String getTimestamp() {
			return timestamp;
		}

		/**
		 * Sets the value of the timestamp property.
		 * 
		 * @param value
		 *            allowed object is {@link XMLGregorianCalendar }
		 * 
		 */
		public void setTimestamp(String value) {
			this.timestamp = value;
		}

		/**
		 * Gets the value of the seqNum property.
		 * 
		 * @return possible object is {@link BigInteger }
		 * 
		 */
		public BigInteger getSeqNum() {
			return seqNum;
		}

		/**
		 * Sets the value of the seqNum property.
		 * 
		 * @param value
		 *            allowed object is {@link BigInteger }
		 * 
		 */
		public void setSeqNum(BigInteger value) {
			this.seqNum = value;
		}

		/**
		 * Gets the value of the keyId property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getKeyId() {
			return keyId;
		}

		/**
		 * Sets the value of the keyId property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setKeyId(String value) {
			this.keyId = value;
		}

		/**
		 * Gets the value of the keys property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the keys property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getKeys().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link PostcardKey }
		 * 
		 * 
		 */
		public List<PostcardKey> getKeys() {
			if (keys == null) {
				keys = new ArrayList<PostcardKey>();
			}
			return this.keys;
		}

		/**
		 * Gets the value of the messages property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the messages property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getMessages().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link PostcardMessage }
		 * 
		 * 
		 */
		public List<PostcardMessage> getMessages() {
			if (messages == null) {
				messages = new ArrayList<PostcardMessage>();
			}
			return this.messages;
		}

		/**
		 * Gets the value of the control property.
		 * 
		 * @return possible object is {@link PostcardMessage }
		 * 
		 */
		public PostcardMessage getControl() {
			return control;
		}

		/**
		 * Sets the value of the control property.
		 * 
		 * @param value
		 *            allowed object is {@link PostcardMessage }
		 * 
		 */
		public void setControl(PostcardMessage value) {
			this.control = value;
		}

	}
	
	@JsonPropertyOrder({ "key_id", "signature", "signature_scheme" })
	public static class Signatures {
		
		@NotBlank
		@JsonProperty("key_id")
		private String keyId;
		
		@NotBlank
		private String signature;
		
		@JsonProperty("signature_scheme")
        protected PostcardSignatureScheme signatureScheme;

		public PostcardSignatureScheme getSignatureScheme() {
			return signatureScheme;
		}

		public void setSignatureScheme(PostcardSignatureScheme signatureScheme) {
			this.signatureScheme = signatureScheme;
		}

		/**
		 * Gets the value of the keyId property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getKeyId() {
			return keyId;
		}

		/**
		 * Sets the value of the keyId property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setKeyId(String value) {
			this.keyId = value;
		}

		/**
		 * Gets the value of the signature property.
		 * 
		 * @return possible object is byte[]
		 */
		public String getSignature() {
			return signature;
		}

		/**
		 * Sets the value of the signature property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setSignature(String value) {
			this.signature = value;
		}

	}

}
