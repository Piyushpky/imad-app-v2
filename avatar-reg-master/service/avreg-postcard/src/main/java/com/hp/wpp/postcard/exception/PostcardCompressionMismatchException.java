package com.hp.wpp.postcard.exception;

public class PostcardCompressionMismatchException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardCompressionMismatchException(Exception e) {
		super(e);
	}
	
	public PostcardCompressionMismatchException(String  messg) {
		super(messg);
	}
	
	public PostcardCompressionMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
