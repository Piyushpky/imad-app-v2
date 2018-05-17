/**
 * 
 */
package com.hp.wpp.postcard.exception;

public class InvalidPostcardException extends PostcardNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidPostcardException() {
		super();
	}
	
	public InvalidPostcardException(Exception e) {
		super(e);
	}
	
	public InvalidPostcardException(String  messg) {
		super(messg);
	}
	
	public InvalidPostcardException(String message, Throwable cause) {
		super(message, cause);
	}

}
