/**
 * 
 */
package com.hp.wpp.postcard.exception;

/**
 * @author mahammad
 *
 */
public class PostcardRetriableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PostcardRetriableException(){
		super();
	}

	public PostcardRetriableException(Throwable e){
		super(e);
	}

}
