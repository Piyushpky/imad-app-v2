/**
 * 
 */
package com.hp.wpp.postcard.exception;

public class InvalidPublicKeyException extends PostcardNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */

	public InvalidPublicKeyException() {
		super();
	}
	
	public InvalidPublicKeyException(Exception e) {
		super(e);
	}
	
	public InvalidPublicKeyException(String  messg) {
		super(messg);
	}
	
	public InvalidPublicKeyException(String message, Throwable cause) {
		super(message, cause);
	}
}
