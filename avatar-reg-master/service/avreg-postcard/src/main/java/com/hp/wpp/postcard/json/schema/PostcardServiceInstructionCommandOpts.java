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
public enum PostcardServiceInstructionCommandOpts {
	
	sign_with_entity_private_key("sign_with_entity_private_key"),
	entity_certificate_key_id_only("entity_certificate_key_id_only"),
	encrypt_with_new_secret("encrypt_with_new_secret");
	
	private final String value;

	PostcardServiceInstructionCommandOpts(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardServiceInstructionCommandOpts fromValue(String v) {
		for (PostcardServiceInstructionCommandOpts c : PostcardServiceInstructionCommandOpts.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
