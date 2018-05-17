package com.hp.wpp.postcard.exception;

public class EmptyCipherInputsException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public EmptyCipherInputsException(Exception e) {
		super(e);
	}
	
	public EmptyCipherInputsException(String  messg) {
		super(messg);
	}
	
	public EmptyCipherInputsException(String message, Throwable cause) {
		super(message, cause);
	}
}
