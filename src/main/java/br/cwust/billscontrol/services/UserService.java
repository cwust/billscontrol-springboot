package br.cwust.billscontrol.services;

import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.dto.UserGetDto;

public interface UserService {
	void createUser(UserCreateDto user);
	
	UserGetDto getCurrentUser();
}
