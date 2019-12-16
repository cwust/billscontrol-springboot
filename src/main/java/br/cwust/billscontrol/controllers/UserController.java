package br.cwust.billscontrol.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cwust.billscontrol.dto.Response;
import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.dto.UserGetDto;
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
	private CreateUserValidator createUserValidator;
	
	@PostMapping
	public ResponseEntity<Response<Void>> createUser(
			@Valid @RequestBody UserCreateDto user,
			BindingResult bindingResult) {
		log.debug("creating user {}", user);
		
		createUserValidator.validate(user, bindingResult);

		return proceedIfNoErrors(bindingResult, () -> {
			userService.createUser(user);
			log.debug("user {} was successfully created", user);
			return success();
		});
	}
	
	@GetMapping(path = "/current")
	public ResponseEntity<Response<UserGetDto>> getCurrentUser() {
		log.debug("getting current user {}");
		
		UserGetDto currentUser = userService.getCurrentUser();
		
		return success(currentUser);
	}	
}
