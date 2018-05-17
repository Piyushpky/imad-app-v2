/**
 * 
 */
package com.hp.wpp.postcard.parser.impl;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.parser.PostcardParserProvider;

/**
 * @author mahammad
 *
 */
public class JacksonPostcardParser implements PostcardParserProvider {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(JacksonPostcardParser.class);
	
	@Override
	public <T> Object unmarshal(Class<T> pojoClass, String jsonText) throws PostcardJSONCorruptedException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonText, pojoClass);
		} catch (IOException e) {
			LOG.debug("Exception while parsing json string: {}", e);
			throw new PostcardJSONCorruptedException("Exception while parsing json string",e);
		}
	}
	
	@Override
	public <T> String marshal(T object) throws PostcardJSONCorruptedException {
		try {
			ObjectWriter writter = getObjectWriter();
			return writter.writeValueAsString(object);
		}  catch (IOException e) {
			LOG.debug("Exception while serializing to json string: {}", e);
			throw new PostcardJSONCorruptedException("Exception while serializing to json string",e);
		}
	}
	
	protected ObjectWriter getObjectWriter() throws IOException {
		return new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY).writer().withDefaultPrettyPrinter();
	}

	@Override
	public <T> String getNodeValue(String json, String key) throws PostcardJSONCorruptedException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(json);
			return rootNode.findValue(key).toString();
			
		} catch (Exception e) {
			LOG.debug("Exception while serializing to json string: {}", e);
			throw new PostcardJSONCorruptedException("Exception while serializing to json string",e);
		}
	}
}
