package com.hp.wpp.postcard.exception;

public class KeyExistsException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public KeyExistsException(Exception e) {
		super(e);
	}
	
	public KeyExistsException(String  messg) {
		super(messg);
	}
	
	public KeyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}