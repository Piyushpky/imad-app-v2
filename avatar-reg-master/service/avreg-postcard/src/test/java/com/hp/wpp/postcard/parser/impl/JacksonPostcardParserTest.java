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
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hp.wpp.postcard.exception.InvalidPostcardException;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardKey;
import com.hp.wpp.postcard.parser.PostcardParserProvider;

/**
 * @author mahammad
 *
 */
public class JacksonPostcardParserTest {

	private PostcardParserProvider postcardParserProvider;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		postcardParserProvider = new JacksonPostcardParser();
	}

	
	
	@DataProvider(name = "validPostcardsWithoutKeys")
	public Object[][] dataProviderForValidPostcardsWithoutKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithoutKeys = new String(Files.readAllBytes(Paths.get(JacksonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithoutKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithoutKeys, "key_id");
		return new Object[][] { { jsonRequestPostcardWithoutKeys, deviceId, keyId } };
	}
	
	@DataProvider(name = "validPostcardsWithoutKeys_Serialize")
	public Object[][] dataProviderForValidPostcardsWithoutKeys_Serialize() throws IOException, URISyntaxException, InvalidPostcardException {
		String jsonString = new String(Files.readAllBytes(Paths.get(JacksonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithoutKeys.json").toURI())));
		PostcardContent postcardContent = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonString);
		
		return new Object[][] { { postcardContent, jsonString } };
	}
	
	@DataProvider(name = "validPostcardsWithKeys")
	public Object[][] dataProviderForValidPostcardsWithKeys() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithKeys = new String(Files.readAllBytes(Paths.get(JacksonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/ValidPostcard_WithKeys.json").toURI())));
		String deviceId = fetchJsonValue(jsonRequestPostcardWithKeys, "entity_id");
		String keyId = fetchJsonValue(jsonRequestPostcardWithKeys, "key_id");
		String keyData = getKeyData(jsonRequestPostcardWithKeys);
		return new Object[][] { { jsonRequestPostcardWithKeys, deviceId, keyId, keyData } };
	}
	
	@DataProvider(name = "validPostcardsWithMandatoryParamsMissing")
	public Object[][] dataProviderForValidPostcardsWithMandatoryParamsMissing() throws IOException, URISyntaxException {
		String jsonRequestPostcardWithMandatoryParamsMissing = new String(Files.readAllBytes(Paths.get(JacksonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/InvalidPostcard.json").toURI())));
		return new Object[][] { { jsonRequestPostcardWithMandatoryParamsMissing, new PostcardJSONCorruptedException() },
								{ "", new InvalidPostcardException() } };
	}
	
	
	
	
	@Test(dataProvider = "validPostcardsWithoutKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithoutKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
	}
	
	@Test(dataProvider = "validPostcardsWithKeys", groups = "postcard")
	public void test_ParseAndValidatePostcard_WithValidPostcards_WithKeys(String jsonPayload, String expectedDeviceId, String expectedKeyId, String expectedKeyData) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
		Assert.assertNotNull(actualValue);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getEntityId(), expectedDeviceId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeyId(), expectedKeyId);
		Assert.assertEquals(actualValue.getPostcardSignedInfo().getKeys().get(0).getKeyAgreement().getRsaKem().getKeyData(), expectedKeyData);
	}
	
	@Test(dataProvider = "validPostcardsWithMandatoryParamsMissing", expectedExceptions = { InvalidPostcardException.class, PostcardNonRetriableException.class } ,groups = "postcard")
	public void test_ParseAndValidatePostcard_WithMandatoryParamsMissing(String jsonPayload, Exception expectedException) throws InvalidPostcardException {
		PostcardContent actualValue = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPayload);
	}
	
	@Test(dataProvider = "validPostcardsWithoutKeys_Serialize", groups = "postcard")
	public void test_Serialize(PostcardContent postcardContent, String expectedJson) throws InvalidPostcardException {
		String actualValue = (String) postcardParserProvider.marshal(postcardContent);
		Assert.assertNotNull(actualValue);
	}
	
	@Test(expectedExceptions = PostcardJSONCorruptedException.class ,groups = "postcard")
	public void test_marshallWithInvalidJson() throws InvalidPostcardException {
		postcardParserProvider = new JacksonPostcardParser() {
			@Override
			protected ObjectWriter getObjectWriter() throws IOException {
				throw new IOException();
			}
		};
		String actualValue = (String) postcardParserProvider.marshal(new String());
		Assert.assertNotNull(actualValue);
	}
	
	// resteasy should not change the property order after json marshall and unmarshall
	@Test
	public void test_JsonOrderWithRestEasy() throws IOException, URISyntaxException, InvalidPostcardException {
		String jsonPayload = new String(Files.readAllBytes(Paths.get(JacksonPostcardParserTest.class.getClassLoader().getResource("postcard_parse/test_json.json").toURI())));
		String expectedPayload = postcardParserProvider.getNodeValue(jsonPayload, "entity_signature");
		Assert.assertNotNull(expectedPayload);
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
