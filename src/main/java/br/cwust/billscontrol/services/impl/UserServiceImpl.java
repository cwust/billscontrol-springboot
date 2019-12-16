package br.cwust.billscontrol.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.UserCreateDtoToEntityConverter;
import br.cwust.billscontrol.converters.UserEntityToGetDtoConverter;
import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.dto.UserGetDto;
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

	@Autowired
	private UserEntityToGetDtoConverter userEntityToGetDtoConverter;

	@Override
	public void createUser(UserCreateDto user) {
		User userEntity = userDtoToEntityConverter.convert(user);
		String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
		userEntity.setPassword(encodedPassword);
		this.userRepository.save(userEntity);
	}

	@Override
	public UserGetDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Optional<User> userOpt = userRepository.findByEmail(email);
		
		if (!userOpt.isPresent()) {
			throw new RuntimeException("Could not find current user in database!");
		}
		
		return userEntityToGetDtoConverter.convert(userOpt.get());
	}
}
