package br.cwust.billscontrol.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.security.JwtTokenParser;

public class AuthenticationController extends AbstractControlller {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenParser jwtTokenParser;

	@PostMapping
	public ResponseEntity<Response<String>> authenticate(
			@Valid @RequestBody CredentialsDto credentials, BindingResult bindingResult)
			throws AuthenticationException {
		
		return proceedIfNoErrors(bindingResult, () -> doAuthentication(credentials));
	}
	
	private ResponseEntity<Response<String>> doAuthentication(CredentialsDto credentials) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				credentials.getEmail(), credentials.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());

		return success(jwtTokenParser.createToken(userDetails));
	}
}
