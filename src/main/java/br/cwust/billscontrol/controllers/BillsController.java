package br.cwust.billscontrol.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.services.BillService;

public class BillsController extends AbstractControlller {
	@Autowired
	private BillService billService;
	
	@PostMapping
	public ResponseEntity<Response<Void>> createBill(
			@Valid @RequestBody BillCreateDto bill,
			BindingResult bindingResult) {
		
		return proceedIfNoErrors(bindingResult, () -> {
			billService.createBill(bill);
			return success();
		});	
	}
}
