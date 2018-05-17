package com.hp.wpp.postcard.json.schema;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PostcardDomain {

	certificate_device("certificate_device"), 
	certificate_entity("certificate_entity"), 
	certificate_model("certificate_model");
	
	private final String value;

	PostcardDomain(String v) {
		value = v;
	}

	@JsonValue
	public String value() {
		return value;
	}
	
	@JsonCreator
	public static PostcardDomain fromValue(String v) {
		for (PostcardDomain c : PostcardDomain.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
