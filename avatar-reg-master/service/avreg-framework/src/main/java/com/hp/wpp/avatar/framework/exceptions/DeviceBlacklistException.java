package com.hp.wpp.avatar.framework.exceptions;

/**
 * Created by aroraja on 11/15/2017.
 */
public class DeviceBlacklistException extends RuntimeException {

    public DeviceBlacklistException(String errorDescription) {
        super( errorDescription);
    }
}
