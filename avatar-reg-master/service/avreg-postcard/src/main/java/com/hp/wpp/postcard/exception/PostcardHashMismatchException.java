package com.hp.wpp.postcard.exception;

public class PostcardHashMismatchException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public PostcardHashMismatchException(Exception e) {
		super(e);
	}
	
	public PostcardHashMismatchException(String  messg) {
		super(messg);
	}
	
	public PostcardHashMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
