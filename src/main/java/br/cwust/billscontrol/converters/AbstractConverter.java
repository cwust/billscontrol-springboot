package br.cwust.billscontrol.converters;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.util.BindingResultHandler;

public abstract class AbstractConverter <S, T>  implements BindingResultHandler {
	public <E extends Enum<E>> E convertEnumValue(String value, Class<E> enumClass, BindingResult bindingResult, String errorMessage) {
		if (value == null) {
			return null;
		} else {
			Optional<E> enumOpt = Arrays.stream(enumClass.getEnumConstants())
				.filter(enumVal -> enumVal.toString().equals(value))
				.findAny();
			
			if (enumOpt.isPresent()) {
				return enumOpt.get();
			} else {
				this.addError(bindingResult, errorMessage);
				return null;
			}
		}
	}
	
	public abstract T convert(S source, BindingResult bindingResult);
}
