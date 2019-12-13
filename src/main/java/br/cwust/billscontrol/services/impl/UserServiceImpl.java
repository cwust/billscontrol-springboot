package br.cwust.billscontrol.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.UserCreateDtoToEntityConverter;
import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;
import br.cwust.billscontrol.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCreateDtoToEntityConverter userDtoToEntityConverter;
	
	@Override
	public void createUser(UserCreateDto user) {
		User userEntity = userDtoToEntityConverter.convert(user);
		String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
		userEntity.setPassword(encodedPassword);
		this.userRepository.save(userEntity);
	}
}
