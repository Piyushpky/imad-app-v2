package com.hp.wpp.avatar.framework.exceptions;

public class InvalidSerialNumberException extends InvalidRegistrationDataException {

	private static final long serialVersionUID = 1L;

	public InvalidSerialNumberException(Exception e) {
		super(e);
	}
	
	public InvalidSerialNumberException(String  messg) {
		super(messg);
	}
	
	public InvalidSerialNumberException(String message, Throwable cause) {
		super(message, cause);
	}
}
