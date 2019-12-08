package br.cwust.billscontrol.repositories;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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
	
	@BeforeEach
	private void init() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testSave() {
		User user = new User();
		user.setName("John Doe");
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
		user.setRole(UserRole.USER);

		userRepository.save(user);
		
		Long id = user.getId();
		
		User userFindByEmail = userRepository.findByEmail(email);
		
		assertEquals(id, userFindByEmail.getId());
		
	}

}
