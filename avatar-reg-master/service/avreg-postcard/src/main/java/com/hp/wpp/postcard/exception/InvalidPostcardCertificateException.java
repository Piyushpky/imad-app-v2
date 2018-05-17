package com.hp.wpp.postcard.exception;

public class InvalidPostcardCertificateException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public InvalidPostcardCertificateException(Exception e) {
		super(e);
	}
	
	public InvalidPostcardCertificateException(String  messg) {
		super(messg);
	}
	
	public InvalidPostcardCertificateException(String message, Throwable cause) {
		super(message, cause);
	}
}
