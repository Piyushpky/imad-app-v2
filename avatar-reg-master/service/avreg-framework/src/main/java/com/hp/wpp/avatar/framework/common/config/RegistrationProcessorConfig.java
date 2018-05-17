package com.hp.wpp.avatar.framework.common.config;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.netflix.config.DynamicPropertyFactory;

/**
 * introduced this class as a temporary basis. This has to remove, once appconfig refreshable property issue addressed from restapp layer. 
 * @author potana
 *
 */
@Component
public class RegistrationProcessorConfig {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RegistrationProcessorConfig.class);

	@Autowired
	private DynamicPropertyFactory dynamicPropertyFactory;
	
	private static final String  CREDENTIAL_REFRESH_ENABLED="avatar-reg.response.credential.refresh.enabled";
	
	public boolean isRegResponseHasCredentialRefreshURLEnabled() {
		Boolean isEnabled = dynamicPropertyFactory.getBooleanProperty(CREDENTIAL_REFRESH_ENABLED, true).getValue();
		LOG.debug("is Reg Response Has Credential Refresh URL Enabled: {}" , isEnabled);
    	return isEnabled;
    }

}