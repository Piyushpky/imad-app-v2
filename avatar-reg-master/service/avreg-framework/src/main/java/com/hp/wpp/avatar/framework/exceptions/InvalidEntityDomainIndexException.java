package com.hp.wpp.avatar.framework.exceptions;

public class InvalidEntityDomainIndexException extends InvalidRegistrationDataException {

	private static final long serialVersionUID = 1L;

	public InvalidEntityDomainIndexException(Exception e) {
		super(e);
	}
	
	public InvalidEntityDomainIndexException(String  messg) {
		super(messg);
	}
	
	public InvalidEntityDomainIndexException(String message, Throwable cause) {
		super(message, cause);
	}
}
