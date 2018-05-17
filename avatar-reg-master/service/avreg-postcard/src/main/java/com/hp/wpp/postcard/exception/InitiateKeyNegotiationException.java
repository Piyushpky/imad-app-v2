/**
 * 
 */
package com.hp.wpp.postcard.exception;

/**
 * Exception holder for send service instruction postcard to request client to initiate key negotiation.
 * 
 * @author mahammad
 *
 */
public class InitiateKeyNegotiationException extends InvalidPostcardException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String keyNegotiationRequestPostcard;

	/**
	 * 
	 */

	public InitiateKeyNegotiationException() {
		super();
	}
	
	public InitiateKeyNegotiationException(Exception e) {
		super(e);
	}
	
	public InitiateKeyNegotiationException(String  messg) {
		super(messg);
	}
	
	public InitiateKeyNegotiationException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getKeyNegotiationRequestPostcard() {
		return keyNegotiationRequestPostcard;
	}

	public void setKeyNegotiationRequestPostcard(String keyNegotiationRequestPostcard) {
		this.keyNegotiationRequestPostcard = keyNegotiationRequestPostcard;
	}
}
