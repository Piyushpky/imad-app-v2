package com.hp.wpp.ssnclaim.exception;

/**
 * Created by kumaniti on 8/23/2017.
 */
public class InvalidPrinterCodeLengthException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidPrinterCodeLengthException(){
        super();
    }

    public InvalidPrinterCodeLengthException(Throwable e){
        super(e);
    }

    public InvalidPrinterCodeLengthException(String s) {
        super(s);
    }
}
