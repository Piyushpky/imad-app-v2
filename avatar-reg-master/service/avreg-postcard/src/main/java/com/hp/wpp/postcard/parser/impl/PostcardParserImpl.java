/**
 * 
 */
package com.hp.wpp.postcard.parser.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.parser.PostcardParser;
import com.hp.wpp.postcard.parser.PostcardParserProvider;

/**
 * @author mahammad
 *
 */
public class PostcardParserImpl implements PostcardParser {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardParserImpl.class);
	
	@Autowired
	private PostcardParserProvider postcardParserProvider;
	
	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.parser.PostcardParser#parseAndValidatePostcard(java.lang.String)
	 */
	@Override
	public PostcardContent parseAndValidatePostcard(String jsonPostcard)throws PostcardJSONCorruptedException{
		if(StringUtils.isBlank(jsonPostcard)) {
			throw new PostcardJSONCorruptedException("Empty/null json received");
		}

		PostcardContent postcardContent = (PostcardContent) postcardParserProvider.unmarshal(PostcardContent.class, jsonPostcard);
		// validating mandatory params
		validateMandatoryPostcardParams(postcardContent);
        return postcardContent;
	}

	private void validateMandatoryPostcardParams(PostcardContent postcardContent)throws PostcardJSONCorruptedException{
		Validator valid = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<PostcardContent>> validator = valid.validate(postcardContent);
		if(!validator.isEmpty()) {
			LOG.debug("Missing mandatory params: {}", validator.toString());
			throw new PostcardJSONCorruptedException("Missing mandatory params");
		}
	}

	@Override
	public <T> String serialize(T object) throws PostcardJSONCorruptedException{
		return postcardParserProvider.marshal(object);
	}

	@Override
	public <T> String getNodeValue(String json, String key) throws PostcardJSONCorruptedException{
		return postcardParserProvider.getNodeValue(json, key);
	}
}
