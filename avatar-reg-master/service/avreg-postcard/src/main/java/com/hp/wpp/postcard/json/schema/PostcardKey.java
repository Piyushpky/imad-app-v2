package com.hp.wpp.postcard.json.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "domain", "postcard_secret_key_id", "key_agreement", "entity_signature" })
public class PostcardKey {
	
	@JsonProperty("domain")
	private PostcardDomain domain;
	
	@JsonProperty("postcard_secret_key_id")
	private String postcardSecretKeyId;
	
	@JsonProperty("key_agreement")
	private PostcardKey.KeyAgreement keyAgreement;
	
	@JsonProperty("entity_signature")
	protected PostcardKey.EntitySignature entitySignature;

	/**
	 * Gets the value of the domain property.
	 * 
	 * @return possible object is {@link PostcardDomain }
	 * 
	 */
	public PostcardDomain getDomain() {
		return domain;
	}

	/**
	 * Sets the value of the domain property.
	 * 
	 * @param value
	 *            allowed object is {@link PostcardDomain }
	 * 
	 */
	public void setDomain(PostcardDomain value) {
		this.domain = value;
	}


	public String getPostcardSecretKeyId() {
		return postcardSecretKeyId;
	}

	public void setPostcardSecretKeyId(String postcardSecretKeyId) {
		this.postcardSecretKeyId = postcardSecretKeyId;
	}

	/**
	 * Gets the value of the keyAgreement property.
	 * 
	 * @return possible object is {@link PostcardKey.KeyAgreement }
	 * 
	 */
	public PostcardKey.KeyAgreement getKeyAgreement() {
		return keyAgreement;
	}

	/**
	 * Sets the value of the keyAgreement property.
	 * 
	 * @param value
	 *            allowed object is {@link PostcardKey.KeyAgreement }
	 * 
	 */
	public void setKeyAgreement(PostcardKey.KeyAgreement value) {
		this.keyAgreement = value;
	}


	public PostcardKey.EntitySignature getEntitySignature() {
		return entitySignature;
	}

	public void setEntitySignature(PostcardKey.EntitySignature entitySignature) {
		this.entitySignature = entitySignature;
	}


	public static class EntitySignature {
		
		@JsonProperty("entity_certificate_key_id")
        protected String entityCertificateKeyId;
		
		@JsonProperty("pem_certificate")
		private String pemCertificate;

		/**
		 * Gets the value of the pemCertificate property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getPemCertificate() {
			return pemCertificate;
		}

		/**
		 * Sets the value of the pemCertificate property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setPemCertificate(String value) {
			this.pemCertificate = value;
		}

		public String getEntityCertificateKeyId() {
			return entityCertificateKeyId;
		}

		public void setEntityCertificateKeyId(String entityCertificateKeyId) {
			this.entityCertificateKeyId = entityCertificateKeyId;
		}

	}
	
	@JsonPropertyOrder({ "rsa_kem" })
	public static class KeyAgreement {

		@JsonProperty("rsa_kem")
		private PostcardKey.KeyAgreement.RsaKem rsaKem;

		/**
		 * Gets the value of the rsaKem property.
		 * 
		 * @return possible object is {@link PostcardKey.KeyAgreement.RsaKem }
		 * 
		 */
		public PostcardKey.KeyAgreement.RsaKem getRsaKem() {
			return rsaKem;
		}

		/**
		 * Sets the value of the rsaKem property.
		 * 
		 * @param value
		 *            allowed object is {@link PostcardKey.KeyAgreement.RsaKem }
		 * 
		 */
		public void setRsaKem(PostcardKey.KeyAgreement.RsaKem value) {
			this.rsaKem = value;
		}
		
		@JsonPropertyOrder({ "server_public_key_id", "key_data", "entity_signature_validation" })
		public static class RsaKem {

			@JsonProperty("server_public_key_id")
			private String serverPublicKeyId;
			
			@JsonProperty("key_data")
			private String keyData;
			
			@JsonProperty("entity_signature_validation")
			private String entitySignatureValidation;

			/**
			 * Gets the value of the serverPublicKeyId property.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getServerPublicKeyId() {
				return serverPublicKeyId;
			}

			/**
			 * Sets the value of the serverPublicKeyId property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setServerPublicKeyId(String value) {
				this.serverPublicKeyId = value;
			}

			/**
			 * Gets the value of the keyData property.
			 * 
			 * @return possible object is String
			 */
			public String getKeyData() {
				return keyData;
			}

			/**
			 * Sets the value of the keyData property.
			 * 
			 * @param value
			 *            allowed object is String
			 */
			public void setKeyData(String value) {
				this.keyData = value;
			}

			public String getEntitySignatureValidation() {
				return entitySignatureValidation;
			}

			public void setEntitySignatureValidation(String entitySignatureValidation) {
				this.entitySignatureValidation = entitySignatureValidation;
			}

		}

	}

}
