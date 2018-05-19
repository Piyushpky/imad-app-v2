package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 9/4/2017.
 */
public class InvalidClaimCodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidClaimCodeException(){
        super();
    }
    public InvalidClaimCodeException(String msg) {
        super(msg);
    }
}
