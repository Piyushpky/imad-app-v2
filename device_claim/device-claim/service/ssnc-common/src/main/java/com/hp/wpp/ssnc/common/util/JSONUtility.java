package com.hp.wpp.ssnc.common.util;

import com.hp.wpp.ssnclaim.exception.SSNNonRetriableException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JSONUtility {
     private JSONUtility(){};
	public static <T> Object unmarshal(Class<T> pojoClass, String jsonText) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonText, pojoClass);
		} catch (Exception e) {
			throw new SSNNonRetriableException("Exception while un-marshalling the json payload");
		}
	}
	
	public static <T> String marshal(T object) {
		ObjectWriter writter = new ObjectMapper().setSerializationInclusion(Inclusion.NON_EMPTY).writer().withDefaultPrettyPrinter();
		try {
			return writter.writeValueAsString(object);
		} catch (Exception e) {
			throw new SSNNonRetriableException("Exception while marshalling to json payload");
		}
	}
}
