package br.cwust.billscontrol.security.services;

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.exception.MultiUserMessageException;

public interface AuthenticationService {
	String getAuthenticationToken(CredentialsDto credentials) throws MultiUserMessageException;
}
