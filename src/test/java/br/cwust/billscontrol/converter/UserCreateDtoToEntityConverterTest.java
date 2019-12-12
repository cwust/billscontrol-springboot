package br.cwust.billscontrol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.converters.UserCreateDtoToEntityConverter;
import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserCreateDtoToEntityConverterTest {
	@Autowired
	private UserCreateDtoToEntityConverter userCreateDtoToEntityConverter;
	
	@Test
	public void testConvertOk() {
		UserCreateDto dto = new UserCreateDto();
		
		String email = "test@test.com";
		String name = "John Doe";
		
		dto.setEmail(email);
		dto.setName(name);
		dto.setRole("USER");
		dto.setLanguage("PT_BR");
		
		User entity = userCreateDtoToEntityConverter.convert(dto);
		
		assertEquals(email, entity.getEmail());
		assertEquals(name, entity.getName());
		assertEquals(UserRole.USER, entity.getRole());
		assertEquals(SupportedLanguage.PT_BR, entity.getLanguage());
	}
}
