package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 9/4/2017.
 */
public class ValidationDataMismatchException extends RuntimeException {

    public ValidationDataMismatchException() {
        super();
    }

    public ValidationDataMismatchException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
}
