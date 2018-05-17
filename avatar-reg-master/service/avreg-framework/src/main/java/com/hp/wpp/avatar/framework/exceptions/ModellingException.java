package com.hp.wpp.avatar.framework.exceptions;

public class ModellingException extends EntityRegistrationNonRetriableException {

	private static final long serialVersionUID = 1L;

	public ModellingException() {
		super();
	}

	public ModellingException(Throwable cause) {
		super(cause);
	}

	public ModellingException(String message) {
		super(message);
	}

	public ModellingException(String message, Throwable cause) {
		super(message, cause);
	}
}
