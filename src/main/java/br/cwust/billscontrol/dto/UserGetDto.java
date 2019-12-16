package br.cwust.billscontrol.dto;

import br.cwust.billscontrol.model.User;

public class UserGetDto {
	private String email;
	private String name;
	private String role;
	private String language;

	public static UserGetDto from(User user) {
		UserGetDto dto = new UserGetDto();
		
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setRole(user.getRole().toString());
		dto.setLanguage(user.getLanguage().toString());
		
		return dto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
