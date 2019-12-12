package br.cwust.billscontrol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;

@Entity
@Table(name = "user")
public class User {
	public static final int EMAIL_LENGTH = 100;
	public static final int PASSWORD_LENGTH = 100;
	public static final int NAME_LENGTH = 100;
	
	private Long id;
	private String email;
	private String password;
	private String name;
	private UserRole role;
	private SupportedLanguage language;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="email", nullable = false, length = EMAIL_LENGTH)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="password", nullable = false, length = PASSWORD_LENGTH)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="name", nullable = false, length = NAME_LENGTH)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable = false)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="language", nullable = true)
	public SupportedLanguage getLanguage() {
		return language;
	}

	public void setLanguage(SupportedLanguage language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name + ", role=" + role + ", language=" + language
				+ "]";
	}
}
