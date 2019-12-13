package br.cwust.billscontrol.services;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.converters.UserCreateDtoToEntityConverter;
import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;
import br.cwust.billscontrol.security.BillsControlPasswordEncoder;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserCreateDtoToEntityConverter userCreateDtoToEntityConverter;

	@MockBean
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;
	
	@Test
	public void testCreateUser() {
		String clearPassword = "CLEARPASSWORD";
		String encodedPassword = "ENCODEDPASSWORD";

		UserCreateDto user = new UserCreateDto();
		User userEntity = new User();
		userEntity.setPassword(clearPassword);

		given(userCreateDtoToEntityConverter.convert(user)).willReturn(userEntity);
		given(passwordEncoder.encode(clearPassword)).willReturn(encodedPassword);
		
		userService.createUser(user);
		ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(argCaptor.capture());
		
		User savedUser = argCaptor.getValue();
		assertSame(userEntity, savedUser);
		assertEquals(encodedPassword, savedUser.getPassword());
	}
}
