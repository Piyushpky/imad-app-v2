package com.hp.wpp.postcard.exception;

public class PostcardCompressionFailureException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardCompressionFailureException(Exception e) {
		super(e);
	}
	
	public PostcardCompressionFailureException(String  messg) {
		super(messg);
	}
	
	public PostcardCompressionFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
