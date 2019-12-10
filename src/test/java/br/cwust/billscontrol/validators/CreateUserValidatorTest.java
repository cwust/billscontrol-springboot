package br.cwust.billscontrol.validators;

import static br.cwust.billscontrol.test.TestUtils.assertBindingResultErrorAdded;
import static br.cwust.billscontrol.test.TestUtils.assertNoBindingResultErrorsAdded;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CreateUserValidatorTest {
	@Autowired
	private CreateUserValidator createUserValidator;

	@MockBean
	private UserRepository userRepository;

	@Mock
	private BindingResult bindingResult;

	@Test
	public void testUserWithEmailAlreadyExists() {
		String email = "john.doe@email.com";

		User user = new User();
		user.setEmail(email);

		BDDMockito.given(userRepository.findByEmail(email)).willReturn(Optional.of(new User()));

		createUserValidator.validate(user, bindingResult);

		assertBindingResultErrorAdded(bindingResult, "user", "{user.already.exists.with.email}");
	}

	@Test
	public void testNoValidationProblems() {
		String email = "john.doe@email.com";

		User user = new User();
		user.setEmail(email);

		BDDMockito.given(userRepository.findByEmail(email)).willReturn(Optional.empty());

		createUserValidator.validate(user, bindingResult);

		verify(userRepository).findByEmail(email);
		assertNoBindingResultErrorsAdded(bindingResult);
	}

}
