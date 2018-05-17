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
public enum PostcardSignatureScheme {
	
	hmac_sha256("hmac_sha256"), 
	raw_rsassa_pss("raw_rsassa_pss"), 
	sha256_with_rsa_and_mfg1("sha256_with_rsa_and_mfg1");
	
	private final String value;

	PostcardSignatureScheme(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardSignatureScheme fromValue(String v) {
		for (PostcardSignatureScheme c : PostcardSignatureScheme.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		return null;
	}
}
