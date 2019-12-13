package br.cwust.billscontrol.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.cwust.billscontrol.model.User;

public class BillsControlUserDetails implements UserDetails{

	private static final long serialVersionUID = -7437333520191583235L;

	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	
	public static UserDetails from(User user) {
		BillsControlUserDetails userDetails = new BillsControlUserDetails();
		userDetails.username = user.getEmail();
		userDetails.password = user.getPassword();
		userDetails.authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));
		
		return userDetails;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
