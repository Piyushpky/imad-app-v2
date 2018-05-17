package com.hp.wpp.postcard.exception;

public class PostcardEncryptFailureException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardEncryptFailureException(Exception e) {
		super(e);
	}
	
	public PostcardEncryptFailureException(String  messg) {
		super(messg);
	}
	
	public PostcardEncryptFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
