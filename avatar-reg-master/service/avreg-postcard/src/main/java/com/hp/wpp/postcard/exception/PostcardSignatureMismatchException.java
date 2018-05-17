package com.hp.wpp.postcard.exception;

public class PostcardSignatureMismatchException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardSignatureMismatchException(Exception e) {
		super(e);
	}
	
	public PostcardSignatureMismatchException(String  messg) {
		super(messg);
	}
	
	public PostcardSignatureMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
