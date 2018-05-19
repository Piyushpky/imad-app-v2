package com.hp.wpp.ssnclaim.exception;


import com.hp.wpp.ssnclaim.exception.InvalidInputParamException;

/**
 * Root exception place holder for PrinterCode Validation.
 * This exception is mapped with HTTP error status code:400 @ REST-API Resource layer. 
 *
 * @author som
 *
 */

public class InvalidPrinterCodeException extends InvalidInputParamException{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPrinterCodeException(){
		super();
	}

	public InvalidPrinterCodeException(Throwable e){
		super(e);
	}
	
	public InvalidPrinterCodeException(String messg){
		super(messg);
	}
}
