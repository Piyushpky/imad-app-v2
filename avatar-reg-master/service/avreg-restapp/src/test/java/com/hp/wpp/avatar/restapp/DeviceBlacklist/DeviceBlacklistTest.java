package com.hp.wpp.avatar.restapp.DeviceBlacklist;

import com.hp.wpp.avatar.framework.exceptions.DeviceBlacklistException;
import com.hp.wpp.avatar.registration.device.entities.BlacklistRules;
import com.hp.wpp.avatar.registration.device.repository.BlacklistRulesDao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by aroraja on 11/15/2017.
 */
public class DeviceBlacklistTest {

    @InjectMocks
    private DeviceBlacklist deviceBlacklist;

    @Mock
    private BlacklistRulesDao blacklistRulesDao;

    private String cloudId = "abc";

    @BeforeMethod
    public void setup() {

        MockitoAnnotations.initMocks(this);
    }

    @Test(expectedExceptions = DeviceBlacklistException.class)
    public void testSuccessDeviceBlacklistWithFirmwareAndModel()
    {
        BlacklistRules blacklistRules = new BlacklistRules();
        blacklistRules.setIsActive(true);
        String UserAgent = "A B C D";

        Mockito.when(blacklistRulesDao.getRuleByModelandFirmawareVersion((String) Mockito.anyString(), (String) Mockito.anyString())).thenReturn(blacklistRules);
        deviceBlacklist.isBlacklist(UserAgent,cloudId);
    }

    @Test(expectedExceptions = DeviceBlacklistException.class)
    public void testSuccessDeviceBlacklistWithModel()
    {
        BlacklistRules blacklistRules = new BlacklistRules();
        blacklistRules.setIsActive(true);
        String UserAgent = "A B C D";
        Mockito.when(blacklistRulesDao.getRuleByModelandFirmawareVersion((String) Mockito.anyString(), (String) Mockito.anyString())).thenReturn(null);
        Mockito.when(blacklistRulesDao.getRuleByModel((String) Mockito.anyString())).thenReturn(blacklistRules);
        deviceBlacklist.isBlacklist(UserAgent,cloudId);
    }

    @Test(expectedExceptions = DeviceBlacklistException.class)
    public void testSuccessDeviceBlacklistWithFirmwareVersion()
    {
        BlacklistRules blacklistRules = new BlacklistRules();
        blacklistRules.setIsActive(true);
        String UserAgent = "A B C D";
        Mockito.when(blacklistRulesDao.getRuleByModelandFirmawareVersion((String) Mockito.anyString(), (String) Mockito.anyString())).thenReturn(null);
        Mockito.when(blacklistRulesDao.getRuleByModel((String) Mockito.anyString())).thenReturn(null);
        Mockito.when(blacklistRulesDao.getRuleByFirmwareVersion((String) Mockito.anyString())).thenReturn(blacklistRules);
        deviceBlacklist.isBlacklist(UserAgent,cloudId);
    }
}
