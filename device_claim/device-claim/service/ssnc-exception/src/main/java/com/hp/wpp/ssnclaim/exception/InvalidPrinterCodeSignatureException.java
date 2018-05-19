package com.hp.wpp.ssnclaim.exception;


/**
 * when printercode's signature fragments does not match with server generated fragments values.
 *
 * @author som
 *
 */

public class InvalidPrinterCodeSignatureException extends InvalidPrinterCodeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPrinterCodeSignatureException(){
		super();
	}

	public InvalidPrinterCodeSignatureException(Throwable e){
		super(e);
	}

	public InvalidPrinterCodeSignatureException(String messg) {
		super(messg);
	}
}
