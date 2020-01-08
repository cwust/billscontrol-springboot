package br.cwust.billscontrol.services;

import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.dto.UserGetDto;
import br.cwust.billscontrol.model.User;

public interface UserService {
	User createUser(UserCreateDto user);
	
	UserGetDto getCurrentUser();
}
