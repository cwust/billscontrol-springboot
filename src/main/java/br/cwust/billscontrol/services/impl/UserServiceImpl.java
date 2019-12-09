package br.cwust.billscontrol.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;
import br.cwust.billscontrol.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public Optional<User> findById(Long id) {
		//TODO: only ADMIN can do this
		return this.userRepository.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		//TODO: only ADMIN can do this
		return this.userRepository.findByEmail(email);
	}
	
	@Override
	public void createUser(User user) {
		//TODO: validate no other user with same email
		this.userRepository.save(user);
	}

	@Override
	public void updateUser(User user) {
		//TODO: validate no other user with new email
		this.userRepository.save(user);
	}

	@Override
	public Optional<User> findCurrentUser() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
