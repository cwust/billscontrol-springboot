package br.cwust.billscontrol.controllers;

import static br.cwust.billscontrol.test.TestUtils.isSuccessResponse;
import static br.cwust.billscontrol.test.TestUtils.toJson;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import br.cwust.billscontrol.dto.CredentialsDto;
import br.cwust.billscontrol.security.services.AuthenticationService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

	public static final String AUTH_URL = "/api/auth";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AuthenticationService authenticationService;

	@Test
	public void testAuthenticationOk() throws Exception {
		String email = "email@email.com";
		String rawPassword = "rawPassword";

		String token = "AAABBBCCC";

		CredentialsDto credentialsDto = new CredentialsDto();
		credentialsDto.setEmail(email);
		credentialsDto.setPassword(rawPassword);

		given(authenticationService.getAuthenticationToken(credentialsDto)).willReturn(token);

		mvc.perform(MockMvcRequestBuilders.post(AUTH_URL).content(toJson(credentialsDto))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(isSuccessResponse()).andExpect(jsonPath("$.data").value(token));
	}
}
