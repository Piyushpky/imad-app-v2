/**
 * 
 */
package com.hp.wpp.postcard.exception;

public class PostcardJSONCorruptedException extends PostcardNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PostcardJSONCorruptedException() {
	}

	public PostcardJSONCorruptedException(Throwable cause) {
		super(cause);
	}

	
	public PostcardJSONCorruptedException(String message) {
		super(message);
	}

	public PostcardJSONCorruptedException(String message, Throwable cause) {
		super(message, cause);
	}

}
