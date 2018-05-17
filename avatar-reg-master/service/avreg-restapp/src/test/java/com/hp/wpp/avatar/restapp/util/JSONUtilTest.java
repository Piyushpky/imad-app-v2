package com.hp.wpp.avatar.restapp.util;

import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.restmodel.enums.AdditionalIdType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification;
import com.hp.wpp.avatar.restmodel.json.schema.EntityInformation;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class JSONUtilTest {
	
	@Test
	public void testUnMarshalVerifyEntityInfo() throws Exception{
		String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/service-registration-valid.json").toURI())));
		EntityIdentification service = (EntityIdentification)JSONUtility.unmarshal(EntityIdentification.class, regPayload);
		
		assertEquals(service.getEntityInfo().size(), 3);
	}
	
	@Test
	public void testUnMarshalVerifyEntityAdditionalIds() throws Exception{
		String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/device-registration-valid.json").toURI())));
		EntityIdentification device = (EntityIdentification)JSONUtility.unmarshal(EntityIdentification.class, regPayload);
		
		assertEquals(device.getEntityAdditionalIds().size(), 3);
		for(EntityIdentification.EntityAdditionalId ids: device.getEntityAdditionalIds()){
			if(ids.getIdType().equals(AdditionalIdType.ssn.name()))
				assertEquals(ids.getIdValue(), "1dhue9-jdj8sjbj-28jsl");
		}
	}
	
	@Test(expectedExceptions=EntityRegistrationNonRetriableException.class)
	public void testUnMarshalWithInvalidPayloads()throws Exception{
		String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/service-registration-invalid.json").toURI())));
		JSONUtility.unmarshal(EntityIdentification.class, regPayload);
	}

    @Test
    public void testUnMarshalVerifyLanguageEnum() throws Exception{
        String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/device-reg-valid-LanguageChina.json").toURI())));
        EntityIdentification device = (EntityIdentification)JSONUtility.unmarshal(EntityIdentification.class, regPayload);

        assertEquals(device.getLanguage(), Language.SimplifiedChinese);
        assertEquals(device.getCountryAndRegionName(), Country.hongKongSAR);
    }

    @Test
    public void testVerifyMarshalEntityInformationWithLanguageEnumChecks() throws Exception{
        EntityInformation entityInformation = new EntityInformation();
        entityInformation.setLanguage(Language.SimplifiedChinese);
        entityInformation.setCountryAndRegionName(Country.unitedStates);

        String responsePayload = JSONUtility.marshal(entityInformation);
        EntityInformation info = (EntityInformation)JSONUtility.unmarshal(EntityInformation.class, responsePayload);
        Assert.assertEquals(info.getLanguage(), Language.SimplifiedChinese);
        Assert.assertEquals(info.getCountryAndRegionName(), Country.unitedStates);
    }
    
    
    @Test
    public void testVerifyMarshalEntityInformationWithInvalidLanguage() throws Exception{
        String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/device-reg-invalid-Language.json").toURI())));
        EntityIdentification device = (EntityIdentification)JSONUtility.unmarshal(EntityIdentification.class, regPayload);
        Assert.assertNull(device.getLanguage());
    }
    
    @Test
    public void testVerifyMarshalEntityInformationWithInvalidCountry() throws Exception{
        String regPayload = new String(Files.readAllBytes(Paths.get(JSONUtilTest.class.getClassLoader().getResource("entity-identification/device-reg-invalid-Country.json").toURI())));
        EntityIdentification device = (EntityIdentification)JSONUtility.unmarshal(EntityIdentification.class, regPayload);
        Assert.assertNull(device.getCountryAndRegionName());
    }
}
