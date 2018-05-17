package com.hp.wpp.avatar.cidgenerator;

/**
 * Custom Exception for dealing with Invalid CPID while parsing a CPID
 * @author sanket
 * @since 25/11/2015
 */
public class InvalidCloudIdException extends Exception {

    public InvalidCloudIdException(String message){
        super(message);
    }

    public InvalidCloudIdException(String message, Throwable error){
        super(message, error);
    }
}
