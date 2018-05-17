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
public enum PostcardEncryption {
	
	aes_128("aes_128"),
	none("none");
	
	private final String value;

	PostcardEncryption(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardEncryption fromValue(String v) {
		for (PostcardEncryption c : PostcardEncryption.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}


}
