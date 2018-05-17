package com.hp.wpp.avatar.registration.device.repository.impl;

import com.hp.wpp.avatar.registration.device.entities.BlacklistRules;
import com.hp.wpp.avatar.registration.device.enums.BlacklistRuleType;
import com.hp.wpp.avatar.registration.device.repository.BlacklistRulesDao;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 * Created by aroraja on 11/14/2017.
 */
@Transactional(value = "podDeviceTransactionManager")
@Repository
public class BlacklistRulesDaoImpl implements BlacklistRulesDao {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(BlacklistRulesDaoImpl.class);

    private static final String RULE_VALUE = "ruleValue";
    private static final String RULE_TYPE = "ruleType";
    private static final String GET_BLACKLIST_RULES= "getBlacklistRules";

    @PersistenceContext(unitName="pod_device")
    private EntityManager em;

    @Override
    public<T> T updateEntity(T entity){
        entity = em.merge(entity);
        return entity;
    }


    @Override
    public<T> void persistEntity(T entity){
        em.persist(entity);
    }

    @Override
    public BlacklistRules getRuleByFirmwareVersion(String firmwareVersion) {
        String ruleValue="*|"+firmwareVersion;
        Query query = em.createNamedQuery(GET_BLACKLIST_RULES).setParameter(RULE_TYPE, BlacklistRuleType.RULETYPE_FIRMWARE_VERSION).setParameter(RULE_VALUE, ruleValue);
        try {
            BlacklistRules rule = (BlacklistRules)query.getSingleResult();
            return rule;
        } catch (NoResultException ex) {
            return null;
        }
        catch (PersistenceException e)
        {
            throw new AvregServiceErrorException("Fetching Blacklist Rule By FirmwareVersion Failed"+e.getMessage());
        }

    }

    @Override
    public BlacklistRules getRuleByModel(String model) {

        String ruleValue=model+"|*";
        Query query = em.createNamedQuery(GET_BLACKLIST_RULES).setParameter(RULE_TYPE, BlacklistRuleType.RULETYPE_MODEL).setParameter(RULE_VALUE, ruleValue);
        try {
            BlacklistRules rule = (BlacklistRules) query.getSingleResult();
            return rule;
        } catch (NoResultException ex) {
            return null;
        }
        catch (PersistenceException e)
        {
            throw new AvregServiceErrorException("Fetching Blacklist by RuleByModel Failed"+e.getMessage());
        }

    }

    @Override
    public BlacklistRules getRuleByModelandFirmawareVersion(String model, String firmwareVersion) {

        String ruleValue=model+"|"+firmwareVersion;
        Query query = em.createNamedQuery(GET_BLACKLIST_RULES).setParameter(RULE_TYPE, BlacklistRuleType.RULETYPE_MODEL_AND_FIRMWARE_VERSION).setParameter(RULE_VALUE, ruleValue);
        try {
            BlacklistRules rule = (BlacklistRules) query.getSingleResult();
            return rule;
        } catch (NoResultException ex) {
            return null;
        }
        catch (PersistenceException e)
        {
            throw new AvregServiceErrorException("Fetching Blacklist by RuleByModel Failed"+e.getMessage());
        }

    }
}
