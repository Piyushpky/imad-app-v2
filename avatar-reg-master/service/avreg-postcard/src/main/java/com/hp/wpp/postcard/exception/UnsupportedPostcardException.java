/**
 * 
 */
package com.hp.wpp.postcard.exception;

/**
 * Exception holder for postcards which received with unsupported ciphers and unsupported domains
 * 
 * @author mahammad
 *
 */
public class UnsupportedPostcardException extends PostcardNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UnsupportedPostcardException() {
		super();
	}
	
	public UnsupportedPostcardException(Exception e) {
		super(e);
	}
	
	public UnsupportedPostcardException(String  messg) {
		super(messg);
	}
	
	public UnsupportedPostcardException(String message, Throwable cause) {
		super(message, cause);
	}

}
