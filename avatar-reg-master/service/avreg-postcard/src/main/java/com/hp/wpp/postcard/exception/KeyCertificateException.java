package com.hp.wpp.postcard.exception;

public class KeyCertificateException extends PostcardNonRetriableException {

	private static final long serialVersionUID = 1L;

	public KeyCertificateException(Exception e) {
		super(e);
	}
	
	public KeyCertificateException(String  messg) {
		super(messg);
	}
	
	public KeyCertificateException(String message, Throwable cause) {
		super(message, cause);
	}
}