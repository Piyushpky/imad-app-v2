package com.hp.wpp.ssnclaim.exception;




/**
 * Root exception place holder for SSN Validation.
 * This exception is mapped with HTTP error status code:400 @ REST-API Resource layer. 
 *
 * @author srikanth potana
 *
 */

public class CryptographyException extends InvalidPrinterCodeException{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CryptographyException(){
		super();
	}

	public CryptographyException(Throwable e){
		super(e);
	}
	
	public CryptographyException(String messg){
		super(messg);
	}
}
