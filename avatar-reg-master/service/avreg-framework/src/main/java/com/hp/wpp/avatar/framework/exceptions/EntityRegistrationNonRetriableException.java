package com.hp.wpp.avatar.framework.exceptions;

/**
 * 
 * Root exception for all run-time exceptions in Avatar service.
 * 
 * @author srikanth potana
 *
 */
public class EntityRegistrationNonRetriableException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public EntityRegistrationNonRetriableException() {
		super();
	}

	public EntityRegistrationNonRetriableException(Throwable cause) {
		super(cause);
	}

	public EntityRegistrationNonRetriableException(String message) {
		super(message);
	}

	public EntityRegistrationNonRetriableException(String message, Throwable cause) {
		super(message, cause);
	}
}
