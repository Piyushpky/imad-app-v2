package com.hp.wpp.ssn.exception;

/**
 * When ssn issuance counter validation failed against stored ssn issuance counter value.
 *
 * @author srikanth potana
 *
 */
public class InvalidSSNCounterException extends InvalidSSNException {

	private static final long serialVersionUID = 1L;

	public InvalidSSNCounterException(){
		super();
	}

	public InvalidSSNCounterException(Throwable e){
		super(e);
	}

	public InvalidSSNCounterException(String messg) {
		super(messg);
	}
}
