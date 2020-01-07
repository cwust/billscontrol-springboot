package br.cwust.billscontrol.controllers;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.dto.Message;
import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.exception.MultiUserMessageException;

public abstract class AbstractControlller {
	protected <T> ResponseEntity<Response<T>> badRequestFromBindingResult(BindingResult bindingResult) {
		List<Message> errorMessages = bindingResult.getAllErrors().stream()
				.map(objError -> Message.error(objError.getDefaultMessage()))
				.collect(Collectors.toList());
		
		Response<T> response = Response.error(errorMessages);
		
		return ResponseEntity.badRequest().body(response);
	}

	protected <T> ResponseEntity<Response<T>> badRequestFromMultiUserMessageException(MultiUserMessageException mume) {
		List<Message> errorMessages = mume.getErrors().stream()
				.map(error -> Message.error(error))
				.collect(Collectors.toList());
		
		Response<T> response = Response.error(errorMessages);
		
		return ResponseEntity.badRequest().body(response);
	}

	protected <T> ResponseEntity<Response<T>> proceedIfNoErrors(BindingResult bindingResult,
			Supplier<ResponseEntity<Response<T>>> supplier) {
		if (bindingResult.hasErrors()) {
			return badRequestFromBindingResult(bindingResult);
		} else {
			try {
				return supplier.get();				
			} catch (MultiUserMessageException mume) {
				return badRequestFromMultiUserMessageException(mume);
			}
		}
	}
	
	protected <T> ResponseEntity<Response<T>> success() {
		return ResponseEntity.ok(Response.success());
	}

	protected <T> ResponseEntity<Response<T>> success(T data) {
		return ResponseEntity.ok(Response.success(data));
	}

}
