package com.hp.wpp.ssnclaim.exception;


/**
 * Root exception place holder for SSN Validation.
 * This exception is mapped with HTTP error status code:400 @ REST-API Resource layer. 
 *
 * @author srikanth potana
 *
 */

public class SNKeyNotFoundException extends InvalidInputParamException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SNKeyNotFoundException(){
		super();
	}

	public SNKeyNotFoundException(Throwable e){
		super(e);
	}
	
	public SNKeyNotFoundException(String messg){
		super(messg);
	}
}
