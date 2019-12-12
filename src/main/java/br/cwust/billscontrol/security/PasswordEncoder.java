package br.cwust.billscontrol.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

	public String encrypt(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
}
