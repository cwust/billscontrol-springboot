package br.cwust.billscontrol.converters;

import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;

@Component
public class UserCreateDtoToEntityConverter {

	public User convert(UserCreateDto source) {
		User user = new User();
		user.setEmail(source.getEmail());
		user.setPassword(source.getPassword());
		user.setName(source.getName());
		user.setRole(Enum.valueOf(UserRole.class, source.getRole()));
		user.setLanguage(Enum.valueOf(SupportedLanguage.class, source.getLanguage()));
		return user;
	}


}
