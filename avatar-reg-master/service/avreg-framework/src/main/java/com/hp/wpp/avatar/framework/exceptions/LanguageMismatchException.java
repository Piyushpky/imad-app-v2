package com.hp.wpp.avatar.framework.exceptions;


public class LanguageMismatchException extends InvalidRegistrationDataException {

	private static final long serialVersionUID = 1L;

	public LanguageMismatchException(Exception e) {
		super(e);
	}
	
	public LanguageMismatchException(String  messg) {
		super(messg);
	}
	
	public LanguageMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
