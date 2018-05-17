package com.hp.wpp.avatar.restapp.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hp.wpp.avatar.framework.exceptions.ModellingException;

public class JSONUtility {

	public static <T> Object unmarshal(Class<T> pojoClass, String jsonText) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        try {
            return objectMapper.readValue(jsonText, pojoClass);
        } catch (IOException e) {
            throw new ModellingException("Failed to deserialize to json object",e);
        }
    }
		
	public static <T> String marshal(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

		ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(object);
        } catch(IOException e) {
            throw new ModellingException("Failed to serialize json object",e);
        }
    }
}
