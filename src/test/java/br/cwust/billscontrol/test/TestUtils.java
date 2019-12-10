package br.cwust.billscontrol.test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.core.IsNull;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
	public static void assertNoBindingResultErrorsAdded(BindingResult bindingResult) {
		verify(bindingResult, never()).addError(any());
	}
	
	public static void assertBindingResultErrorAdded(BindingResult bindingResult, String object, String message) {
		verify(bindingResult).addError(argThat(objError -> 
			objError.getObjectName().contentEquals(object) &&
			objError.getDefaultMessage().contentEquals(message)));
	}
	
	public static String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Unable to generate JSON");
			return null;
		}
	}
	
	public static String longString(int numChars) {
		return longString(numChars, "TEST ");
	}

	public static String longString(int numChars, String pattern) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() < numChars) {
			sb.append(pattern);
		}
		return sb.substring(0, numChars - 1);
	}
	
	public static ResultMatcher responseContainsMessage(String message) {
		return jsonPath(String.format("$.messages[?(@.message=='%s')]", message)).exists();
	}
	
	public static ResultMatcher isSuccessResponse() {
		return jsonPath("$.success").value(true);
	}
	
	public static ResultMatcher isErrorResponse() {
		return jsonPath("$.success").value(false);
	}
	
	public static ResultMatcher responseHasNoData() {
		return jsonPath("$.data").value(IsNull.nullValue());
	}
}
