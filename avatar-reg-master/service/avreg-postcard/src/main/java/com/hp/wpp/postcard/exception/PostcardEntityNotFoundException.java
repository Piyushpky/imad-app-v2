/**
 * 
 */
package com.hp.wpp.postcard.exception;

/**
 * @author mahammad
 *
 */
public class PostcardEntityNotFoundException extends PostcardNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PostcardEntityNotFoundException() {
	}

	/**
	 * @param e
	 */
	public PostcardEntityNotFoundException(Throwable e) {
		super(e);
	}
	
	
	public PostcardEntityNotFoundException(String  messg) {
		super(messg);
	}

}
