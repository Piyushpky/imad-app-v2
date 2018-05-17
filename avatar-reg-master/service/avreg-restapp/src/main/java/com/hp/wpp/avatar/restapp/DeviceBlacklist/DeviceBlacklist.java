package com.hp.wpp.avatar.restapp.DeviceBlacklist;

import com.hp.wpp.avatar.framework.exceptions.DeviceBlacklistException;
import com.hp.wpp.avatar.registration.device.repository.BlacklistRulesDao;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by aroraja on 11/15/2017.
 */
@Component
public class DeviceBlacklist {

    @Autowired
    private BlacklistRulesDao blackListRulesDao;

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(DeviceBlacklist.class);

    private void setBlackListRulesDao(BlacklistRulesDao blackListRulesDao){
        this.blackListRulesDao=blackListRulesDao;
    }

    public void isBlacklist(String userAgent, String cloudId) {
       // User-Agent - Sample "devices printer/1.0 W1A30A TETONXXXXA001.1746A.00"
        String[] userAgentValues = StringUtils.split(userAgent, " ");
        if (userAgentValues.length > 3) {
            String firmwareVersion = userAgentValues[3];
            String model = userAgentValues[2];
            if ((blackListRulesDao.getRuleByModelandFirmawareVersion(model, firmwareVersion) != null)
                    || (blackListRulesDao.getRuleByModel(model) != null) ||
                    (blackListRulesDao.getRuleByFirmwareVersion(firmwareVersion) != null)) {
                if (cloudId != null)
                    LOG.info("Device is Blacklisted firmwareVersion={}, modelNumber={} cloudId={]", firmwareVersion, model, cloudId);
                else
                    LOG.info("Device is Blacklisted firmwareVersion={}, modelNumber={} ", firmwareVersion, model);

                throw new DeviceBlacklistException("Device is been blacklisted");

            }
        }
    }

    public boolean isBlackListEnabled( boolean isAppconfigBlackListFlagEnabled) {
        return isAppconfigBlackListFlagEnabled;
    }
}
