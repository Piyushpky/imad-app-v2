package com.hp.wpp.avatar.framework.exceptions;

public class EmptyConfigUrlException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public EmptyConfigUrlException() {
		super();
	}

	public EmptyConfigUrlException(Throwable cause) {
		super(cause);
	}

	public EmptyConfigUrlException(String message) {
		super(message);
	}

	public EmptyConfigUrlException(String message, Throwable cause) {
		super(message, cause);
	}
}
