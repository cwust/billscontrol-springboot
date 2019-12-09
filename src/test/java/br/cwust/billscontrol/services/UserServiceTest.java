package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private User mockUser;
	
	@BeforeEach
	public void init() {
		this.mockUser = new User();
	}
	
	@Test
	public void testCreate() {
		userService.createUser(mockUser);
		
		verify(userRepository).save(mockUser);
	}

	@Test
	public void testUpdate() {
		userService.createUser(mockUser);
		
		verify(userRepository).save(mockUser);
	}

	@Test
	public void testFindByEmail() {
		String email = "email@email.com";
		
		BDDMockito.given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
		
		Optional<User> userFindByEmail = userService.findByEmail(email);
		
		assertSame(mockUser, userFindByEmail.get());
	}
	
	@Test
	public void testFindById() {
		Long id = 123l;
		
		BDDMockito.given(userRepository.findById(id)).willReturn(Optional.of(mockUser));
		
		Optional<User> userFindByEmail = userService.findById(id);
		
		assertSame(mockUser, userFindByEmail.get());
	}
	
}
