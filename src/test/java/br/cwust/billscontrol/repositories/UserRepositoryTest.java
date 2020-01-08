package br.cwust.billscontrol.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	
	@AfterEach
	private void cleanup() {
		userRepository.deleteAll();
	}

	@Test
	public void testSave() {
		User user = new User();
		user.setName("John Doe");
		user.setPassword("password");
		user.setEmail("john.doe@whatever.com");
		user.setRole(UserRole.USER);

		assertNull(user.getId());

		userRepository.save(user);
		
		assertNotNull(user.getId());
	}

	@Test
	public void testFindByEmail() {
		String email = "john.doe@whatever.com";
		
		User user = new User();
		user.setName("John Doe");
		user.setEmail(email);
		user.setPassword("password");
		user.setRole(UserRole.USER);

		userRepository.save(user);
		
		Long id = user.getId();
		
		Optional<User> userFindByEmail = userRepository.findByEmail(email);
		
		assertTrue(userFindByEmail.isPresent());
		
		assertEquals(id, userFindByEmail.get().getId());
	}

}
