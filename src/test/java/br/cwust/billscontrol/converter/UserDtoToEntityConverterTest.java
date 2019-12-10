package br.cwust.billscontrol.converter;

import static br.cwust.billscontrol.test.TestUtils.assertBindingResultErrorAdded;
import static br.cwust.billscontrol.test.TestUtils.assertNoBindingResultErrorsAdded;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.converters.UserDtoToEntityConverter;
import br.cwust.billscontrol.dto.UserDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserDtoToEntityConverterTest {
	@Autowired
	private UserDtoToEntityConverter userDtoToEntityConverter;
	
	@Mock
	private BindingResult bindingResult;
	
	@Test
	public void testConvertOk() {
		UserDto dto = new UserDto();
		
		Long id = 123l;
		String email = "test@test.com";
		String name = "John Doe";
		
		dto.setId(id);
		dto.setEmail(email);
		dto.setName(name);
		dto.setRole("USER");
		dto.setLanguage("PT_BR");
		
		User entity = userDtoToEntityConverter.convert(dto, bindingResult);
		
		assertEquals(id, entity.getId());
		assertEquals(email, entity.getEmail());
		assertEquals(name, entity.getName());
		assertEquals(UserRole.USER, entity.getRole());
		assertEquals(SupportedLanguage.PT_BR, entity.getLanguage());
		assertNoBindingResultErrorsAdded(bindingResult);
	}

	@Test
	public void testConvertEnumsError() {
		UserDto dto = new UserDto();
		
		dto.setRole("SUPER_ADMIN");
		dto.setLanguage("JA");
		
		userDtoToEntityConverter.convert(dto, bindingResult);
		
		assertBindingResultErrorAdded(bindingResult, "user", "{user.role.invalid}");
		assertBindingResultErrorAdded(bindingResult, "user", "{user.language.invalid}");
	}
}
