package com.hp.wpp.avatar.framework.exceptions;


/**
 * 
 * The class EntityValidationException is a checked exception. It should be thrown, when rest-api receives invalid hp custom auth header.
 * 
 * @author srikanth potana
 *
 */
public class EntityValidationException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public EntityValidationException(Exception e) {
		super(e);
	}
	
	public EntityValidationException(String  messg) {
		super(messg);
	}
	
	public EntityValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
