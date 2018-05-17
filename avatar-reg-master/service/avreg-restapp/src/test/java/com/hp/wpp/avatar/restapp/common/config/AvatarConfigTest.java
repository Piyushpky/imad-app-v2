package com.hp.wpp.avatar.restapp.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "/applicationContext-avatar-config-test.xml")
public class AvatarConfigTest extends AbstractTestNGSpringContextTests{

	@Autowired 
	public AvatarApplicationConfig config;
	
	@Test
	public void testAvatarConfig() throws Exception{
		Assert.assertEquals(config.isPostcardEnabled(), false);
	}
	
//	@Test
	public void testIsProdStageEnvironment(){
		Assert.assertTrue(config.isEnvironmentProdOrStage());
	}
}
