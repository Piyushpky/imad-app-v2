package com.hp.wpp.avatar.framework.exceptions;


/**
 * 
 * The class EntityNotRegisteredException is a checked exception. It should be thrown, when given cloud-id is not exist in cloud DB.
 * 
 * @author srikanth potana
 *
 */
public class EntityNotRegisteredException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public EntityNotRegisteredException(Exception e) {
		super(e);
	}
	
	public EntityNotRegisteredException(String  messg) {
		super(messg);
	}
}
