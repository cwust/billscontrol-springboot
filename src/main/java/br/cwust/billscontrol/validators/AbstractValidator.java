package br.cwust.billscontrol.validators;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public abstract class AbstractValidator<T> {
	public abstract void validate(T object, BindingResult bindingResult);

	public abstract String getObjectName();
	
	protected void validateCondition(boolean condition, BindingResult bindingResult, String message) {
		if (!condition) {
			this.addError(bindingResult, message);
		}
	}

	protected <E extends Enum<E>> void validateEnum(String enumName, Class<E> enumClass, BindingResult bindingResult,
			String message) {
		this.validateCondition(EnumUtils.isValidEnum(enumClass, enumName), bindingResult, message);
	}
	
	protected void addError(BindingResult bindingResult, String error) {
		bindingResult.addError(new ObjectError(this.getObjectName(), error));
	}
}
