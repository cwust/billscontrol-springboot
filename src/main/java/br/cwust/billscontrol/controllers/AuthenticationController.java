package br.cwust.billscontrol.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.security.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController extends AbstractControlller {
	@Autowired
	private AuthenticationService authenticationService; 

	@PostMapping
	public ResponseEntity<Response<String>> authenticate(
			@Valid @RequestBody CredentialsDto credentials, BindingResult bindingResult)
			throws AuthenticationException {
		
		return proceedIfNoErrors(bindingResult, () -> success(authenticationService.getAuthenticationToken(credentials)));
	}
}
