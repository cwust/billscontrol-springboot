package br.cwust.billscontrol.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.springframework.validation.BindingResult;

public class TestUtils {
	public static void assertNoBindingResultErrorsAdded(BindingResult bindingResult) {
		verify(bindingResult, never()).addError(any());
	}
	
	public static void assertBindingResultErrorAdded(BindingResult bindingResult, String object, String message) {
		verify(bindingResult).addError(argThat(objError -> 
			objError.getObjectName().contentEquals(object) &&
			objError.getDefaultMessage().contentEquals(message)));
	}

}
