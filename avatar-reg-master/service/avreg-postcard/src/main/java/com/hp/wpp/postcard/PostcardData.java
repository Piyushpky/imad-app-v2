/**
 * 
 */
package com.hp.wpp.postcard;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.json.schema.PostcardCompression;
import com.hp.wpp.postcard.json.schema.PostcardEncryption;


/**
 * @author mahammad
 *
 */
public class PostcardData {
	
	public PostcardData() {

	}
	
	@JsonProperty("version")
	private String version = "1.0";
	
	@NotBlank
	@JsonProperty("entity_id")
	private String entityId;
	
	@NotNull
	private ApplicationType applicationType;
	
	@NotNull
	@JsonProperty("messages")
	private List<Message> messages;
	
	/**
	 * @return the applicationType
	 */
	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param applicationType the applicationType to set
	 */
	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public List<Message> getMessages() {
		if (messages == null) {
			messages = new ArrayList<PostcardData.Message>();
		}
		return this.messages;
	}


	public class Message {
		
		@JsonCreator
		public Message() {
			// TODO Auto-generated constructor stub
		}
		
		@NotNull
		@JsonProperty("content_type")
		private ContentType contentType;
		@NotNull
		private byte[] content;

		private PostcardEncryption encryption;
		
		private PostcardCompression compression;

		/**
		 * @return the encryption
		 */
		public PostcardEncryption getEncryption() {
			return encryption;
		}

		/**
		 * @param encryption the encryption to set
		 */
		public void setEncryption(PostcardEncryption encryption) {
			this.encryption = encryption;
		}

		/**
		 * @return the compression
		 */
		public PostcardCompression getCompression() {
			return compression;
		}

		/**
		 * @param compression the compression to set
		 */
		public void setCompression(PostcardCompression compression) {
			this.compression = compression;
		}

		/**
		 * @return the content_type
		 */
		public ContentType getContentType() {
			return contentType;
		}

		/**
		 * @param contentType the content_type to set
		 */
		public void setContentType(ContentType contentType) {
			this.contentType = contentType;
		}

		/**
		 * @return the content
		 */
		public byte[] getContent() {
			return content;
		}

		/**
		 * @param content the content to set
		 */
		public void setContent(byte[] content) {
			this.content = content;
		}
	}

}
