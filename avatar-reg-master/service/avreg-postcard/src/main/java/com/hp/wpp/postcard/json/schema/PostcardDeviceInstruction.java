package com.hp.wpp.postcard.json.schema;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;


public class PostcardDeviceInstruction {

	@JsonProperty("last_valid_postcard")
	private PostcardDeviceInstruction.LastValidPostcard lastValidPostcard;
	
	@JsonProperty("last_postcard_error")
	private PostcardDeviceInstruction.LastPostcardError lastPostcardError;
	
	private PostcardChallenge challenge;

	/**
	 * Gets the value of the lastValidPostcard property.
	 * 
	 * @return possible object is
	 *         {@link PostcardDeviceInstruction.LastValidPostcard }
	 * 
	 */
	public PostcardDeviceInstruction.LastValidPostcard getLastValidPostcard() {
		return lastValidPostcard;
	}

	/**
	 * Sets the value of the lastValidPostcard property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link PostcardDeviceInstruction.LastValidPostcard }
	 * 
	 */
	public void setLastValidPostcard(PostcardDeviceInstruction.LastValidPostcard value) {
		this.lastValidPostcard = value;
	}

	/**
	 * Gets the value of the lastPostcardError property.
	 * 
	 * @return possible object is
	 *         {@link PostcardDeviceInstruction.LastPostcardError }
	 * 
	 */
	public PostcardDeviceInstruction.LastPostcardError getLastPostcardError() {
		return lastPostcardError;
	}

	/**
	 * Sets the value of the lastPostcardError property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link PostcardDeviceInstruction.LastPostcardError }
	 * 
	 */
	public void setLastPostcardError(PostcardDeviceInstruction.LastPostcardError value) {
		this.lastPostcardError = value;
	}

	/**
	 * Gets the value of the challenge property.
	 * 
	 * @return possible object is {@link PostcardChallenge }
	 * 
	 */
	public PostcardChallenge getChallenge() {
		return challenge;
	}

	/**
	 * Sets the value of the challenge property.
	 * 
	 * @param value
	 *            allowed object is {@link PostcardChallenge }
	 * 
	 */
	public void setChallenge(PostcardChallenge value) {
		this.challenge = value;
	}

	public static class LastPostcardError {

		private String violation;
		
		@JsonProperty("postcard_id")
		private byte[] postcardId;
		
		@JsonProperty("seq_num")
		private BigInteger seqNum;
		
		@JsonProperty("signature_hash")
		private byte[] signatureHash;

		/**
		 * Gets the value of the violation property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getViolation() {
			return violation;
		}

		/**
		 * Sets the value of the violation property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setViolation(String value) {
			this.violation = value;
		}

		/**
		 * Gets the value of the postcardId property.
		 * 
		 * @return possible object is byte[]
		 */
		public byte[] getPostcardId() {
			return postcardId;
		}

		/**
		 * Sets the value of the postcardId property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setPostcardId(byte[] value) {
			this.postcardId = value;
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
		 * Gets the value of the signatureHash property.
		 * 
		 * @return possible object is byte[]
		 */
		public byte[] getSignatureHash() {
			return signatureHash;
		}

		/**
		 * Sets the value of the signatureHash property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setSignatureHash(byte[] value) {
			this.signatureHash = value;
		}

	}

	public static class LastValidPostcard {

		@JsonProperty("postcard_id")
		private byte[] postcardId;
		
		@JsonProperty("seq_num")
		private BigInteger seqNum;
		
		@JsonProperty("signature_hash")
		private byte[] signatureHash;

		/**
		 * Gets the value of the postcardId property.
		 * 
		 * @return possible object is byte[]
		 */
		public byte[] getPostcardId() {
			return postcardId;
		}

		/**
		 * Sets the value of the postcardId property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setPostcardId(byte[] value) {
			this.postcardId = value;
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
		 * Gets the value of the signatureHash property.
		 * 
		 * @return possible object is byte[]
		 */
		public byte[] getSignatureHash() {
			return signatureHash;
		}

		/**
		 * Sets the value of the signatureHash property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setSignatureHash(byte[] value) {
			this.signatureHash = value;
		}

	}

}
