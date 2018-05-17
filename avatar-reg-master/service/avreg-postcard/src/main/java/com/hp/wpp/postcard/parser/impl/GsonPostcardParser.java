/**
 * 
 */
package com.hp.wpp.postcard.parser.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.wpp.postcard.exception.PostcardJSONCorruptedException;
import com.hp.wpp.postcard.parser.PostcardParserProvider;

/**
 * @author mahammad
 *
 */
public class GsonPostcardParser implements PostcardParserProvider {
	
	@Override
	public <T> Object unmarshal(Class<T> pojoClass, String jsonText) {
		Object retValue = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		Gson gson = gsonBuilder.create();
		retValue = gson.fromJson(jsonText, pojoClass);
		 
		return retValue;
	}
	
	@Override
	public <T> String marshal(T object) {
		String retValue = null;

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		retValue = gson.toJson(object);

		return retValue;
	}

	@Override
	public <T> String getNodeValue(String json, String key) throws PostcardJSONCorruptedException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
