package br.cwust.billscontrol.services;

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.exception.MultiUserMessageException;

public interface AuthenticationService {
	String getAuthenticationToken(CredentialsDto credentials) throws MultiUserMessageException;
}
