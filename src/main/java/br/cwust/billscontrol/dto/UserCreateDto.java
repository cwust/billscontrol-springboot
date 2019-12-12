package br.cwust.billscontrol.dto;

import static br.cwust.billscontrol.util.BillsControlStringUtils.hideValue;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.cwust.billscontrol.model.User;

public class UserCreateDto {
	private String email;
	private String password;
	private String name;
	private String role;
	private String language;

	@NotEmpty(message = "{user.email.notempty}" )
	@Email(message = "{user.email.valid}")
	@Length(message = "{user.email.length}", max = User.EMAIL_LENGTH)	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotEmpty(message = "{user.password.notempty}" )
	@Length(message = "{user.password.length}", min = 6, max = 20)	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotEmpty(message = "{user.name.notempty}" )
	@Length(message = "{user.name.length}", max = User.NAME_LENGTH)	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message = "{user.role.notempty}" )
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@NotEmpty(message = "{user.language.notempty}" )
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "UserCreateDto [email=" + email + ", password=" + hideValue(password) + ", name=" + name + ", role=" + role
				+ ", language=" + language + "]";
	}
}
