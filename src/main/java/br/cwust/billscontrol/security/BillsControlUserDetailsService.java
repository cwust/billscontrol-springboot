package br.cwust.billscontrol.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.UserRepository;

@Service
public class BillsControlUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepository.findByEmail(username);
		if (userOpt.isPresent()) {
			return BillsControlUserDetails.from(userOpt.get());
		} else {
			return null;
		}
	}

}
