package com.hp.wpp.postcard.exception;

public class PostcardDecryptFailureException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardDecryptFailureException(Exception e) {
		super(e);
	}
	
	public PostcardDecryptFailureException(String  messg) {
		super(messg);
	}
	
	public PostcardDecryptFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
