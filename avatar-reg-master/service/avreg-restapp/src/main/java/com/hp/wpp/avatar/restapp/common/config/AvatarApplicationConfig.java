package com.hp.wpp.avatar.restapp.common.config;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.netflix.config.DynamicPropertyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AvatarApplicationConfig {

	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(AvatarApplicationConfig.class);

	private static final String PROD_STAGE = "(PROD|STAGE)";

	public static final String IS_BLACKLIST_ENABLED = "avatar-reg.is.blacklist.enabled";

	public static final String IS_POSTCARD_ENABLED = "avatar-reg.is.postcard.enabled";
	
	public static final String AVATAR_REGISTRATION_URL = "avatar-reg.response.location.url";

	private static final String AVATAR_APPLICATION_ID="avatar-reg.avatar.application.id";
	
	private static final String PROD_STAGE_ENVIRONMENT= "avatar-reg.prod.stage.environment.name";

	private static final String ENVIRONMENT_VARIABLE_NAME = "avatar-reg.environment.name";
	public static final String AVATAR_REG_IS_EVENT_ENABLED = "avatar-reg.is.event.enabled";

	@Autowired
	private DynamicPropertyFactory dynamicPropertyFactory;
	
	public String getAvatarRegistrationURL() {
    	return dynamicPropertyFactory.getStringProperty(AVATAR_REGISTRATION_URL, "").getValue();
    }
	
	public boolean isPostcardEnabled() {
    	return dynamicPropertyFactory.getBooleanProperty(IS_POSTCARD_ENABLED, false).getValue();
    }

	public boolean isBlacklistEnabled() {
		return dynamicPropertyFactory.getBooleanProperty(IS_BLACKLIST_ENABLED, true).getValue();
	}

	public String getApplicationKeyById(String applicationKey){
		return dynamicPropertyFactory.getStringProperty(applicationKey,null).getValue();
	}
	
	public String getAvatarApplicationID() {
		return dynamicPropertyFactory.getStringProperty(AVATAR_APPLICATION_ID, "").getValue();
	}
	
	public String getProdOrStageEnvironmentName() {
    	return dynamicPropertyFactory.getStringProperty(PROD_STAGE_ENVIRONMENT, PROD_STAGE).getValue();
    }
	
	public String getEnvironmentVariableName(){
		return dynamicPropertyFactory.getStringProperty(ENVIRONMENT_VARIABLE_NAME, "ENVIRONMENT_NAME").getValue();
	}

	public boolean isEnvironmentProdOrStage(){
		String environmentVariable= getEnvironmentVariableName();
		String environment = System.getenv(environmentVariable);
		if(environment==null){
			LOG.debug("environment variable {} not found. Environment defaulting to prod", environmentVariable);
			return true;
		}
		
		Pattern p = Pattern.compile(getProdOrStageEnvironmentName());
		Matcher m = p.matcher(environment);
		
		if(m.matches())
			return true;
		
		return false;
	}

	public boolean isEventEnabled() {
		return dynamicPropertyFactory.getBooleanProperty(AVATAR_REG_IS_EVENT_ENABLED, false).getValue();
	}
}
