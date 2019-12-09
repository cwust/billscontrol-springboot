package br.cwust.billscontrol.services;

import java.util.Optional;

import br.cwust.billscontrol.model.User;

public interface UserService {
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(String email);

	void createUser(User user);

	void updateUser(User user);

	Optional<User> findCurrentUser();
}
