/**
 * 
 */
package com.hp.wpp.postcard.json.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mahammad
 *
 */
public enum PostcardCreator {
	
	entity("entity"),
	service("service");
	
	private final String value;

	PostcardCreator(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardCreator fromValue(String v) {
		for (PostcardCreator c : PostcardCreator.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}




}
