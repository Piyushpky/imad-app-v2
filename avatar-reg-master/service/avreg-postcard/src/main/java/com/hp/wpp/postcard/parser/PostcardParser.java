/**
 * 
 */
package com.hp.wpp.postcard.parser;

import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.json.schema.PostcardContent;

/**
 * @author mahammad
 *
 */
public interface PostcardParser {
	
	
	/**
	 * parse the json postcard payload and validate data.
	 * 
	 * 
	 * @param jsonPostcard
	 * @return
	 */
	public PostcardContent parseAndValidatePostcard(String jsonPostcard) throws PostcardJSONCorruptedException;
	
	/**
	 * 
	 * 
	 * @param object
	 * @return
	 */
	public <T> String serialize(T object) throws PostcardJSONCorruptedException ;
	
	public <T> String getNodeValue(String json, String key) throws PostcardJSONCorruptedException;

}
