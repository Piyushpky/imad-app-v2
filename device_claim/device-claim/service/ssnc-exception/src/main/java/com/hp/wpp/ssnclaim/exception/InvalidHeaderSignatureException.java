package com.hp.wpp.ssnclaim.exception;




/**
 * when registration payload's signature fragments does not match with server generated fragments values.
 *
 * @author srikanth potana
 *
 */

public class InvalidHeaderSignatureException extends InvalidPrinterCodeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidHeaderSignatureException(){
		super();
	}

	public InvalidHeaderSignatureException(Throwable e){
		super(e);
	}

	public InvalidHeaderSignatureException(String messg) {
		super(messg);
	}
}
