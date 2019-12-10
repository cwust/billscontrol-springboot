package br.cwust.billscontrol.converters;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.dto.UserDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;

@Component
public class UserDtoToEntityConverter extends AbstractConverter<UserDto, User> {

	@Override
	public String getTargetObject() {
		return "user";
	}

	@Override
	public User convert(UserDto source, BindingResult bindingResult) {
		User user = new User();
		user.setId(source.getId());
		user.setEmail(source.getEmail());
		user.setName(source.getName());
		user.setRole(this.convertEnumValue(source.getRole(), UserRole.class, bindingResult, "{user.role.invalid}"));
		user.setLanguage(this.convertEnumValue(source.getLanguage(), SupportedLanguage.class, bindingResult, "{user.language.invalid}"));
		return user;
	}


}
