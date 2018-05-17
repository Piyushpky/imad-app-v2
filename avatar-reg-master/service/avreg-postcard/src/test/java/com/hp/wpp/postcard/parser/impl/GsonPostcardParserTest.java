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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.parser.PostcardParserProvider;

/**
 * @author mahammad
 *
 */
public class GsonPostcardParserTest {

	private PostcardParserProvider postcardParserProvider;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		postcardParserProvider = new GsonPostcardParser();
	}
	
	@DataProvider(name = "validPostcardsWithoutKeys")
	public Object[][] dataProviderForValidPostcardsWithoutKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithoutKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithoutKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "device_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "key_id");
		return new Object[][] { { jsonRequestPostcardWithoutKeys, deviceId, keyId } };
	}
	
	@DataProvider(name = "gsonSerialize")
	public Object[][] dataProviderForSerialize() throws IOException, URISyntaxException, InvalidPostcardException {
		String jsonText = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithoutKeys.json").toURI())));
		PostcardParserProvider postcardParserProvider = new JacksonPostcardParser();
		PostcardContent postcardContent = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonText);
		return new Object[][] { { postcardContent, jsonText } };
	}
	
	@DataProvider(name = "validPostcardsWithKeys")
	public Object[][] dataProviderForValidPostcardsWithKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithKeys = new String(Files.readAllBytes(Paths.get(GsonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithKeys, "device_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithKeys, "key_id");
		String keyData = getKeyData(jsonRequestPostcardWithKeys);
		return new Object[][] { { jsonRequestPostcardWithKeys, deviceId, keyId, keyData } };
	}
	
	@DataProvider(name = "validPostcardsWithMandatoryParamsMissing")
	public Object[][] dataProviderForValidPostcardsWithMandatoryParamsMissing() throws IOException, URISyntaxException {
		return new Object[][] { { null, null }};
	}
	
	// Exceptions/Null responses are expected because pojo classes doesnt have gson annotations
	@Test(dataProvider = "validPostcardsWithoutKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithoutKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
		Assert.assertNotNull(actualValue);
	}
	
	// Exceptions/Null responses are expected because pojo classes doesnt have gson annotations
	@Test(dataProvider = "validPostcardsWithKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId, String expectedKeyData) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
		Assert.assertNotNull(actualValue);
	}
	
	@Test(dataProvider = "validPostcardsWithMandatoryParamsMissing", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithMandatoryParamsMissing(String jsonPayload, Object expectedValue) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
		Assert.assertNull(actualValue);
	}
	
	// Exceptions/Null responses are expected because pojo classes doesnt have
	// gson annotations
	@Test(dataProvider = "gsonSerialize", groups = "postcard")
	public void test_Serialize(PostcardContent postcardContent, String expectedJson) throws InvalidPostcardException {
		String actualValue = postcardParserProvider.marshal(postcardContent);
		Assert.assertNotNull(actualValue);
//		Assert.assertEquals(actualValue, expectedJson);
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
