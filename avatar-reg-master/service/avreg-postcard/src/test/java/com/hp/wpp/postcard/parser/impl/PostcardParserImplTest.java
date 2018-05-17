/**
 * 
 */
package com.hp.wpp.postcard.parser.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.parser.PostcardParser;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardParserImplTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private PostcardParser postcardParser;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@DataProvider(name = "validPostcardsWithoutKeys")
	public Object[][] dataProviderForValidPostcardsWithoutKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithoutKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithoutKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "key_id");
		return new Object[][] { { jsonRequestPostcardWithoutKeys, deviceId, keyId } };
	}
	
	@DataProvider(name = "validPostcardInValidApplicationId")
	public Object[][] dataProviderForPostcardInvalidapplicationId() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithoutKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/Postcard_InvalidApplicationId.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "key_id");
		return new Object[][] { { jsonRequestPostcardWithoutKeys, deviceId, keyId } };
	}
	
	@DataProvider(name = "validPostcardValidApplicationId")
	public Object[][] dataProviderForPostcardValidapplicationId() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithoutKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/Postcard_ValidApplicationId.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "key_id");
		return new Object[][] { { jsonRequestPostcardWithoutKeys, deviceId, keyId } };
	}
	
	@DataProvider(name = "validate-postcard")
	public Object[][] dataProviderForValidatePostcard() throws IOException, URISyntaxException {
		String invalidPostcardJson1 = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_schema/Invalid_values_1_0_postcard-1.json").toURI())));
		String deviceId = fetchJsonValue(invalidPostcardJson1, "entity_id");
		String keyId = fetchJsonValue(invalidPostcardJson1, "key_id");
		return new Object[][] { { invalidPostcardJson1, deviceId, keyId } };
	}
	
	@DataProvider(name = "validPostcardsWithKeys")
	public Object[][] dataProviderForValidPostcardsWithKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithKeys, "key_id");
		String keyData = getKeyData(jsonRequestPostcardWithKeys);
		return new Object[][] { { jsonRequestPostcardWithKeys, deviceId, keyId, keyData } };
	}
	
	@DataProvider(name = "validPostcardsWithMandatoryParamsMissing")
	public Object[][] dataProviderForValidPostcardsWithMandatoryParamsMissing() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithMandatoryParamsMissing = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/InValidPostcard_MandatoryParamsMissing.json").toURI())));
		String jsonRequestPostcardWithMandatoryParamsValuesMissing = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/InValidPostcard_MandatoryFieldValuesMissing.json").toURI())));
		return new Object[][] { 
//				{ jsonRequestPostcardWithMandatoryParamsMissing, new InvalidPostcardException() },
								{ jsonRequestPostcardWithMandatoryParamsValuesMissing, new PostcardJSONCorruptedException() },
								{ null, new PostcardJSONCorruptedException() },
								{ " ", new PostcardJSONCorruptedException() }};
	}
//	
	
	
	
	@Test(dataProvider = "validPostcardsWithoutKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithoutKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
	}
	
	@Test(dataProvider = "validPostcardsWithKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId, String expectedKeyData) throws InvalidPostcardException {
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeys().get(0).getKeyAgreement().getRsaKem().getKeyData(), expectedKeyData);
	}
	
	@Test(dataProvider = "validate-postcard", groups = "postcard")
	public void validatePostcard(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
	}
	
	@Test(dataProvider = "validPostcardInValidApplicationId", expectedExceptions = { InvalidPostcardException.class, InvalidPostcardException.class } ,groups = "postcard")
	public void validatePostcardInvalidApplicationId(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
	}
	
	@Test(dataProvider = "validPostcardValidApplicationId",groups = "postcard")
	public void validatePostcardValidApplicationId(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
	}
	
	@Test(dataProvider = "validPostcardsWithMandatoryParamsMissing", expectedExceptions = { PostcardJSONCorruptedException.class, PostcardJSONCorruptedException.class } ,groups = "postcard")
	public void test_ParseAndValidatePostcard_WithMandatoryParamsMissing(String jsonPayload, Exception expectedException) throws PostcardJSONCorruptedException {
		System.out.println("test");
		PostcardContent actualValue = postcardParser.parseAndValidatePostcard(jsonPayload);
	}
	
	@Test(groups = "postcard")
	public void test_Serilaize() throws InvalidPostcardException {
		PostcardContent postcardContent = new PostcardContent();
		PostcardContent.PostcardSignedInfo signedInfo = new PostcardContent.PostcardSignedInfo();
		signedInfo.setVersion("1.0");
		postcardContent.setPostcardSignedInfo(signedInfo);
		String actualValue = postcardParser.serialize(postcardContent);
		Assert.assertNotNull(actualValue);
	}
	
	/**
	 * @param jsonPayload
	 * @return
	 */
	private String getKeyData(String jsonPayload) {
		JsonParserFactory factory = JsonParserFactory.getInstance();
		JSONParser parser = factory.newJsonParser();
		Map jsonMap = parser.parseJson(jsonPayload);
		Map signedInfoMap = (Map) jsonMap.get("postcard_signed_info");
		List<PostcardKey> keyMap = (ArrayList<PostcardKey>) signedInfoMap.get("keys");
		Map keyAgreementMap = (Map) keyMap.get(0);
		Map keyAggrmtMap = (Map) keyAgreementMap.get("key_agreement");
		Map rsaKemMap = (Map) keyAggrmtMap.get("rsa_kem");
		String keyData = (String) rsaKemMap.get("key_data");
		return keyData;
	}
	
	private String fetchJsonValue(String jsonString, String key) {
		JsonParserFactory factory = JsonParserFactory.getInstance();
		JSONParser parser = factory.newJsonParser();
		Map jsonMap = parser.parseJson(jsonString);
		Map rootMap = (Map) jsonMap.get("postcard_signed_info");
		String value = (String) rootMap.get(key);
		return value;
	}
}
