package br.cwust.billscontrol.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cwust.billscontrol.converters.UserDtoToEntityConverter;
import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.dto.UserDto;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.services.UserService;
import br.cwust.billscontrol.validators.CreateUserValidator;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController extends AbstractControlller {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDtoToEntityConverter userDtoToEntityConverter;
	
	@Autowired
	private CreateUserValidator createUserValidator;
	
	@PostMapping
	public ResponseEntity<Response<Void>> createUser(
			@Valid @RequestBody UserDto user,
			BindingResult bindingResult) {
		log.debug("creating user {}", user);
		
		User userEntity = userDtoToEntityConverter.convert(user, bindingResult);
		createUserValidator.validate(userEntity, bindingResult);
		
		return proceedIfNoErrors(bindingResult, () -> {
			userService.createUser(userEntity);
			log.debug("user {} was successfully created", user);
			return success();
		});
	}
	
	
}
