package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

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

import br.cwust.billscontrol.dto.UserCreateDto;
import br.cwust.billscontrol.dto.UserGetDto;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;
import br.cwust.billscontrol.security.CurrentUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

	public static final String USER_EMAIL = "email@email.com";
	public static final String USER_NAME = "John  Doe";
	public static final UserRole USER_ROLE = UserRole.USER;
	public static final SupportedLanguage USER_LANGUAGE = SupportedLanguage.EN;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@MockBean
	private CurrentUser currentUser;

	@Autowired
	private UserService userService;
	
	@Test
	public void testCreateUser() {
		String rawPassword = "RAWPASSWORD";
		String encodedPassword = "ENCODEDPASSWORD";

		UserCreateDto dto = new UserCreateDto();
		dto.setEmail(USER_EMAIL);
		dto.setName(USER_NAME);
		dto.setPassword(rawPassword);
		dto.setRole(USER_ROLE.toString());		
		dto.setLanguage(USER_LANGUAGE.toString());
		
		given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);
		
		userService.createUser(dto);
		ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(argCaptor.capture());
		
		User savedUser = argCaptor.getValue();
		assertEquals(USER_EMAIL, savedUser.getEmail());
		assertEquals(encodedPassword, savedUser.getPassword());
		assertEquals(USER_NAME, savedUser.getName());
		assertEquals(USER_ROLE, savedUser.getRole());
		assertEquals(USER_LANGUAGE, savedUser.getLanguage());		
	}
	
	@Test
	public void testGetCurrentUser() {
		User entity = new User();
		entity.setEmail(USER_EMAIL);
		entity.setName(USER_NAME);
		entity.setRole(USER_ROLE);
		entity.setLanguage(USER_LANGUAGE);
		
		given(currentUser.getEmail()).willReturn(USER_EMAIL);
		given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(entity));
		
		UserGetDto result = userService.getCurrentUser();
		
		assertNotNull(result);
		assertEquals(USER_EMAIL, result.getEmail());
		assertEquals(USER_NAME, result.getName());
		assertEquals(USER_ROLE.toString(), result.getRole());
		assertEquals(USER_LANGUAGE.toString(), result.getLanguage());		
	}
}
