package com.hp.wpp.ssn.exception;


/**
 * Root exception place holder for SSN Validation.
 * This exception is mapped with HTTP error status code:400 @ REST-API Resource layer. 
 *
 * @author srikanth potana
 *
 */

public class InvalidSSNException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSSNException(){
		super();
	}

	public InvalidSSNException(Throwable e){
		super(e);
	}
	
	public InvalidSSNException(String messg){
		super(messg);
	}
}
