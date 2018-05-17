/**
 * 
 */
package com.hp.wpp.postcard.schema.validator;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import junit.framework.Assert;

/**
 * @author mahammad
 *
 */
public class SchemaValidationUtilsTest {
	
	
	@BeforeMethod
	public void setUp() {
	}
	
	@DataProvider(name = "schema-validation")
	public Object[][]  dataProviderSchemaValidation() throws Exception {
		
		ClassLoader classLoader = getClass().getClassLoader();
		File schema_1_1_File = new File(classLoader.getResource("postcard_schema/1_1_scheam.json").getFile());
		File schema_1_0_File = new File(classLoader.getResource("postcard_schema/1_0_scheam.json").getFile());
		
		File inputFile_1_0 = new File(classLoader.getResource("postcard_schema/valid_1_0_postcard.json").getFile());
		File inputFile_1_1 = new File(classLoader.getResource("postcard_schema/valid_1_1_postcard.json").getFile());
		File inputFile_runtime_1_0 = new File(classLoader.getResource("postcard_schema/valid_1_0_postcard_runtime.json").getFile());
		
		File InvalidinputFile_1_0_1 = new File(classLoader.getResource("postcard_schema/Invalid_1_0_postcard-1.json").getFile());
		File InvalidinputFile_1_0_2 = new File(classLoader.getResource("postcard_schema/Invalid_1_0_postcard-2.json").getFile());
		
		return new Object[][] { 
					{ schema_1_1_File, inputFile_1_0, true }, 
					{ schema_1_0_File, inputFile_1_0, true }, 
					{ schema_1_1_File, inputFile_runtime_1_0, true },
					// in json there is no way to handle dis
					{ schema_1_0_File, inputFile_1_1, true },
					{ schema_1_0_File, inputFile_1_0, true },
					{ schema_1_0_File, InvalidinputFile_1_0_1, false }, 
					{ schema_1_0_File, InvalidinputFile_1_0_2, false } 
					};
	}
	
	@Test(dataProvider = "schema-validation", groups = "postcard")
	public void validateJsonAgainstSchema(File schemaFile, File jsonFile, boolean expectedValue) throws IOException, ProcessingException {
		boolean actualValue = SchemaValidationUtils.isJsonValid(schemaFile, jsonFile);
		Assert.assertEquals(expectedValue, actualValue);
	}
}
