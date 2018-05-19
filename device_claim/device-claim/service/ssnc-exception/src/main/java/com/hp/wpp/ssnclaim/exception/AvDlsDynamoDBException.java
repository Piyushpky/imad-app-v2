package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 8/28/2017.
 */
public class AvDlsDynamoDBException extends RuntimeException {
    public AvDlsDynamoDBException() {
        super();
    }
    public AvDlsDynamoDBException(String message) {
        super(message);
    }

}
