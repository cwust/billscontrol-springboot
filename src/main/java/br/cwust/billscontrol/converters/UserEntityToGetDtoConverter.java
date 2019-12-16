package br.cwust.billscontrol.converters;

import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.UserGetDto;
import br.cwust.billscontrol.model.User;

@Component
public class UserEntityToGetDtoConverter {
	public UserGetDto convert(User user) {
		UserGetDto dto = new UserGetDto();
		
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setRole(user.getRole().toString());
		dto.setLanguage(user.getLanguage().toString());
		
		return dto;
	}
}
