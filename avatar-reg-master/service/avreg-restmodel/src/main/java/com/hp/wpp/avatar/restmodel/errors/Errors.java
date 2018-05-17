package com.hp.wpp.avatar.restmodel.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Errors {
	
	@JsonProperty("errors")
	private List<Error> errors = new ArrayList<Error>();

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
}
