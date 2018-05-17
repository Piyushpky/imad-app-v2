package com.hp.wpp.avatar.framework.exceptions;

public class EnumMapException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public EnumMapException() {
		super();
	}

	public EnumMapException(Throwable cause) {
		super(cause);
	}

	public EnumMapException(String message) {
		super(message);
	}

	public EnumMapException(String message, Throwable cause) {
		super(message, cause);
	}
}

