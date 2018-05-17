package com.hp.wpp.avatar.restmodel.json.schema;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.EntityClassifier;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification.EntityVersion;

public class EIValidationTest {

	@DataProvider(name="identificationPayload")
	public Object[][] createIdentificationPayload(){
		return new Object[][]{
				{ getEntityIdentification(false), true},
				{ getEntityIdentification(true), false}
		};
	}
	
	@Test(dataProvider="identificationPayload")
	public void testPrinterIdentification(EntityIdentification identification, boolean expectedFlag){
		Set<ConstraintViolation<EntityIdentification>> validator = validate(identification);
		Assert.assertEquals(validator.isEmpty(),expectedFlag);
	}
	
	public Set<ConstraintViolation<EntityIdentification>> validate(EntityIdentification identification) {
		Validator valid=Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<EntityIdentification>> validator = valid.validate(identification);
		return validator;
	}
	
	private EntityIdentification getEntityIdentification(boolean isInvalid) {
		EntityIdentification identification = new EntityIdentification();
		identification.setVersion("1.0");
		identification.setEntityId("CN835455GH");
		identification.setEntityModel("A9T80A");
		identification.setEntityName("HP Photosmart 5520g");
		identification.setEntityType(EntityType.devices);
		identification.setEntityClassifier(EntityClassifier.printer);
		identification.setEntityDomain("palermo_2016");
		if(!isInvalid){identification.setCountryAndRegionName(Country.africa);}
		EntityVersion version = new EntityVersion();
		version.setRevision("PM1540AR");
		version.setDate("1015-08-13");
		identification.setEntityVersion(version);
		identification.setResetCounter(1);
		return identification;
	}
	

}
