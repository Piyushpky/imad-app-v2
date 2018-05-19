package com.hp.wpp.ssnclaim.restmodel.json.schema.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Errors {
	private  static final ObjectMapper JSON = new ObjectMapper();
	
	@JsonProperty("errors")
	private List<Error> errors = new ArrayList();

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public static Errors fromJsonAsBytes(byte[] bytes) throws IOException {
		return JSON.readValue(bytes,Errors.class);
	}
	public String toJsonAsString() throws JsonProcessingException {
		return JSON.writeValueAsString(this);
	}
}
