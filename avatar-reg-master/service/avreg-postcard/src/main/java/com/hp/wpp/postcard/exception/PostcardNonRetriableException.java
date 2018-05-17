/**
 * 
 */
package com.hp.wpp.postcard.exception;

/**
 * @author mahammad
 *
 */
public class PostcardNonRetriableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PostcardNonRetriableException(){
		super();
	}

	public PostcardNonRetriableException(Throwable e){
		super(e);
	}
	
	public PostcardNonRetriableException(String message){
		super(message);
	}
	
	public PostcardNonRetriableException(String message, Throwable cause) {
		super(message, cause);
	}

}
