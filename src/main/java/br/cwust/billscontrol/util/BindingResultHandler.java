package br.cwust.billscontrol.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public interface BindingResultHandler {

	String getTargetObject();
	
	default void addError(BindingResult bindingResult, String error) {
		bindingResult.addError(new ObjectError(this.getTargetObject(), error));
	}
}
