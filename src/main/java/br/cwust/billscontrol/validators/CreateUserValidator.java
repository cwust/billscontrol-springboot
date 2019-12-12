package br.cwust.billscontrol.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.repositories.UserRepository;

@Component
public class CreateUserValidator extends AbstractValidator<UserCreateDto> {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public String getObjectName() {
		return "user";
	}
	
	@Override
	public void validate(UserCreateDto user, BindingResult bindingResult) {
		validateEnum(user.getRole(), UserRole.class, bindingResult, "{user.role.invalid}");

		validateEnum(user.getLanguage(), SupportedLanguage.class, bindingResult, "{user.language.invalid}");
		
		validateMailDoesNotExistYet(user, bindingResult);
	}
	
	protected void validateMailDoesNotExistYet(UserCreateDto user, BindingResult bindingResult) {
		if (user.getEmail() != null) {
			boolean mailDoesNotExistYet = !this.userRepository.findByEmail(user.getEmail()).isPresent();
			validateCondition(mailDoesNotExistYet, bindingResult, "{user.already.exists.with.email}");
		}		
	}
}
