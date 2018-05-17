package com.hp.wpp.avatar.registration.device.enums;

/**
 * Created by aroraja on 11/14/2017.
 */
public enum BlacklistRuleType {
    RULETYPE_FIRMWARE_VERSION("RULETYPE_FIRMWARE_VERSION"),
    RULETYPE_MODEL("RULETYPE_MODEL"),
    RULETYPE_MODEL_AND_FIRMWARE_VERSION("RULETYPE_MODEL_AND_FIRMWARE_VERSION");

    private final String blacklistRuleType;

    BlacklistRuleType(String value) {
        this.blacklistRuleType = value;
    }

    public String getValue(){
        return this.blacklistRuleType;
    }

}
