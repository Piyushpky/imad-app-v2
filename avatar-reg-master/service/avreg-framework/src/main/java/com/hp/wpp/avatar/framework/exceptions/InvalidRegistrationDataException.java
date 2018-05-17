package com.hp.wpp.avatar.framework.exceptions;


/**
 * 
 * The class InvalidRegistrationDataException is an unchecked exception. It should be thrown, when rest-api receives invalid registration payload.
 * 
 * 1. when registration request payload was not following spec contract like mandatory vs non-mandatory fields.
 * 2. when format of Date field is not expected.   
 * 
 * @author srikanth potana
 *
 */
public class InvalidRegistrationDataException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public InvalidRegistrationDataException() {
		super();
	}

	public InvalidRegistrationDataException(Throwable cause) {
		super(cause);
	}

	public InvalidRegistrationDataException(String message) {
		super(message);
	}

	public InvalidRegistrationDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
