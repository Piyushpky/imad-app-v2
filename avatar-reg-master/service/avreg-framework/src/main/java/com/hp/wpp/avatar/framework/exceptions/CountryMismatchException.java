package com.hp.wpp.avatar.framework.exceptions;


public class CountryMismatchException extends InvalidRegistrationDataException {

	private static final long serialVersionUID = 1L;

	public CountryMismatchException(Exception e) {
		super(e);
	}
	
	public CountryMismatchException(String  messg) {
		super(messg);
	}
	
	public CountryMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
