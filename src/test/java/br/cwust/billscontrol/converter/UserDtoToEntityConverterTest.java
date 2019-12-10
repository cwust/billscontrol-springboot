package br.cwust.billscontrol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
		assertNoBindingResultErrorsAdded();
	}

	@Test
	public void testConvertEnumsError() {
		UserDto dto = new UserDto();
		
		dto.setRole("SUPER_ADMIN");
		dto.setLanguage("JA");
		
		userDtoToEntityConverter.convert(dto, bindingResult);
		
		assertBindingResultErrorAdded("user", "{user.role.invalid}");
		assertBindingResultErrorAdded("user", "{user.language.invalid}");
	}

	public void assertNoBindingResultErrorsAdded() {
		verify(bindingResult, never()).addError(any());
	}
	
	public void assertBindingResultErrorAdded(String object, String message) {
		verify(bindingResult).addError(argThat(objError -> 
			objError.getObjectName().contentEquals(object) &&
			objError.getDefaultMessage().contentEquals(message)));
	}
}
