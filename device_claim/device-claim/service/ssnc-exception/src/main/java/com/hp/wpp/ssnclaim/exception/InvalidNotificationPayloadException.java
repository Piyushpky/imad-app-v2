package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 9/3/2017.
 */
public class InvalidNotificationPayloadException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidNotificationPayloadException(){super();}
    public InvalidNotificationPayloadException(String msg){super(msg);}
}
