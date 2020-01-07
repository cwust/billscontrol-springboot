package br.cwust.billscontrol.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.exception.MultiUserMessageException;
import br.cwust.billscontrol.security.JwtTokenParser;
import br.cwust.billscontrol.services.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenParser jwtTokenParser;

	
	
	@Override
	public String getAuthenticationToken(CredentialsDto credentials) throws MultiUserMessageException {
		Authentication authentication;
		
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					credentials.getEmail(), credentials.getPassword()));
		} catch (BadCredentialsException bce) {
			throw new MultiUserMessageException("Invalid credentials!");
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());
		
		return jwtTokenParser.createToken(userDetails);
	}
}
