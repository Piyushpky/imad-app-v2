/**
 * 
 */
package com.hp.wpp.postcard.parser;

import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;

/**
 * @author mahammad
 *
 */
public interface PostcardParserProvider {
	
	/**
	 * Convert json text to pojo class
	 * 
	 * @param pojoClass
	 * @param jsonText
	 * @return
	 */
	public <T> Object unmarshal(Class<T> pojoClass, String jsonText) throws PostcardJSONCorruptedException ;
	
	/**
	 * Convert pojo class to json text
	 * 
	 * @param object
	 * @return
	 */
	public <T> String marshal(T object) throws PostcardJSONCorruptedException ;
	
	public <T> String getNodeValue(String json, String key) throws PostcardJSONCorruptedException ;

}
