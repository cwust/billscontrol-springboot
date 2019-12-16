package br.cwust.billscontrol.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;

@Component
public class CurrentUser {
	@Autowired
	private UserRepository userRepository;
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public String getEmail() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			return getAuthentication().getName();
		} else {
			return null;
		}
	}
	
	public User getUserEntity() {
		String email = getEmail();
		if (email == null) {
			return null;
		} else {
			Optional<User> userOpt = userRepository.findByEmail(email);
			if (userOpt.isPresent()) {
				return userOpt.get();
			} else { 
				return null;
			}
		}	
	}
}
