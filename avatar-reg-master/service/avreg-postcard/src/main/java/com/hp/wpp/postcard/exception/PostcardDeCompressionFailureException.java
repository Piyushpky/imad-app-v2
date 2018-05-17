package com.hp.wpp.postcard.exception;

public class PostcardDeCompressionFailureException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardDeCompressionFailureException(Exception e) {
		super(e);
	}
	
	public PostcardDeCompressionFailureException(String  messg) {
		super(messg);
	}
	
	public PostcardDeCompressionFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
