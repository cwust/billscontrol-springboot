package br.cwust.billscontrol.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.cwust.billscontrol.model.User;

public class UserDto {
	private Long id;
	private String email;
	private String name;
	private String role;
	private String language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "{user.email.notempty}" )
	@Email(message = "{user.email.valid}")
	@Length(message = "{user.email.length}", max = User.EMAIL_LENGTH)	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
