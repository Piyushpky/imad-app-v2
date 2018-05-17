package com.hp.wpp.ssn.exception;


/**
 * when registration payload's signature fragments does not match with server generated fragments values.
 *
 * @author srikanth potana
 *
 */

public class InvalidSSNSignatureException extends InvalidSSNException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSSNSignatureException(){
		super();
	}

	public InvalidSSNSignatureException(Throwable e){
		super(e);
	}

	public InvalidSSNSignatureException(String messg) {
		super(messg);
	}
}
