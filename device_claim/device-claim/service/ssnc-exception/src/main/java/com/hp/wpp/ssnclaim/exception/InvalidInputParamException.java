package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 8/28/2017.
 */
public class InvalidInputParamException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    public InvalidInputParamException(){super();}

    public InvalidInputParamException(Throwable e) {
        super(e);
    }

    public InvalidInputParamException(String messg) {
        super(messg);
    }
}
