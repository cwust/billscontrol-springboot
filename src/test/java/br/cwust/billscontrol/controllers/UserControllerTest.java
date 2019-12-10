package br.cwust.billscontrol.controllers;


import static br.cwust.billscontrol.test.TestUtils.isErrorResponse;
import static br.cwust.billscontrol.test.TestUtils.isSuccessResponse;
import static br.cwust.billscontrol.test.TestUtils.longString;
import static br.cwust.billscontrol.test.TestUtils.responseContainsMessage;
import static br.cwust.billscontrol.test.TestUtils.responseHasNoData;
import static br.cwust.billscontrol.test.TestUtils.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.cwust.billscontrol.dto.UserDto;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.services.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
	public static final String CREATE_USER_URL = "/api/users";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@Test
	public void testSuccess() throws Exception {
		String name = "John Doe";
		String email = "john.doe@test.com";
		String language = "PT_BR";
		String role = "USER";

		UserDto userDto = new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		userDto.setLanguage(language);
		userDto.setRole(role);
		
		mvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
				.content(toJson(userDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(isSuccessResponse())
			.andExpect(responseHasNoData());
	}
	
	@Test
	public void testValidationErrors() throws Exception {
		String name = longString(User.NAME_LENGTH+10);
		String email = "invalid_email";

		UserDto userDto = new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		
		mvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
				.content(toJson(userDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(isErrorResponse())
			.andExpect(responseHasNoData())
			.andExpect(responseContainsMessage("Invalid user e-mail."))
			.andExpect(responseContainsMessage("User name has maximum of 100 characters."))
			.andExpect(responseContainsMessage("User role is required."))
			.andExpect(responseContainsMessage("User language is required."))
			.andDo(result -> System.out.println(result.getResponse().getContentAsString()));
		
	}
}
