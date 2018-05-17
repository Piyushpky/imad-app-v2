package com.hp.wpp.avatar.restapp.resources;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;

public class EntityResourceHelperTest {
	private static final String SPEC_VERSION = "1.0";

	@Test
	public void testEntityConfig() throws Exception{
		EntityConfigurationBO configBO = new EntityConfigurationBO();
		configBO.setCloudId("cloud_id");
		configBO.setSpecVersion(SPEC_VERSION);
		LinkBO linkBO1 = new LinkBO();
		linkBO1.setHref("https://avatar.hpeprint.com/avatar/connectivityconfig/printers/seoupthe3pk");
		linkBO1.setRel("connectivity_config");
		configBO.getConfigurations().add(linkBO1);

		String payload = EntityResourceHelper.createEntityConfigJsonPayload(configBO);
		assertTrue(payload.contains(SPEC_VERSION));
		assertTrue(payload.contains("connectivity_config"));
	}


}