package br.cwust.billscontrol.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;
import br.cwust.billscontrol.util.BindingResultHandler;

@Component
public class CreateUserValidator implements BindingResultHandler {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public String getTargetObject() {
		return "user";
	}

	public void validate(User user, BindingResult bindingResult) {
		if (user.getId() != null) {
			addError(bindingResult,  "{user.cannot.create.with.id}");
		}
		
		if (user.getEmail() != null && 
				this.userRepository.findByEmail(user.getEmail()) != null) {
			addError(bindingResult,  "{user.already.exists.with.email}");
		}
		
	}
}
