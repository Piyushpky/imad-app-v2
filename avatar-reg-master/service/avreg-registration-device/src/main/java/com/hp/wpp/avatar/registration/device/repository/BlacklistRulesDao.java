package com.hp.wpp.avatar.registration.device.repository;

import com.hp.wpp.avatar.registration.device.entities.BlacklistRules;

/**
 * Created by aroraja on 11/14/2017.
 */

public interface BlacklistRulesDao {

    public <T> void persistEntity(T entity);

    public <T> T updateEntity(T entity);

    /**
     * Check if printer with particular firmware version exists in the rule
     * @param firmwareVersion
     * @return
     */
    public  BlacklistRules getRuleByFirmwareVersion(String firmwareVersion);

    /**
     * Check if printer with particular model exists in the rule
     * @param model
     * @return
     */
    public BlacklistRules getRuleByModel(String model);

    public BlacklistRules getRuleByModelandFirmawareVersion(String model, String firmwareVersion);

}
