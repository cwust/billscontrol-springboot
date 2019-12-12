package br.cwust.billscontrol.services;

import br.cwust.billscontrol.dto.UserCreateDto;

public interface UserService {
	void createUser(UserCreateDto user);
}
