package com.hp.wpp.avatar.framework.exceptions;

/**
 * 
 * Root exception for all checked exceptions in Avatar service.
 * 
 * @author srikanth potana
 *
 */
public class EntityRegistrationRetriableException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityRegistrationRetriableException(Exception e) {
		super(e);
	}

	public EntityRegistrationRetriableException(String messg) {
		super(messg);
	}

	public EntityRegistrationRetriableException() {
		super();
	}

	public EntityRegistrationRetriableException(Throwable cause) {
		super(cause);
	}

	public EntityRegistrationRetriableException(String message, Throwable cause) {
		super(message, cause);
	}

}
