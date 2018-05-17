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
public enum PostcardCompression {
	
	gzip("gzip"), 
	deflate("deflate"), 
	http_gzip("http_gzip"),
	none("none");
	
	private final String value;

	PostcardCompression(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardCompression fromValue(String v) {
		for (PostcardCompression c : PostcardCompression.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}


}
