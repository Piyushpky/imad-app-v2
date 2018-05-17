package com.hp.wpp.avatar.framework.exceptions;

/**
 * 
 * @author bnshr
 *
 */
public class InvalidRequestException extends EntityRegistrationNonRetriableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRequestException() {
		super();
	}

	public InvalidRequestException(Throwable cause) {
		super(cause);
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}