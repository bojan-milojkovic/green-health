package com.green.health.user.entities;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.green.health.parents.PojoParent;
import com.green.health.security.entities.UserSecurityJPA;

@Entity
@Table(name = "user")
public class UserJPA implements PojoParent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="reg_date")
	private LocalDateTime registration;
	
	@Column(name="email")
	private String email;
	
	@OneToOne(mappedBy="userJpa", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private UserSecurityJPA userSecurityJpa;
	
	public UserSecurityJPA getUserSecurityJpa() {
		return userSecurityJpa;
	}

	public void setUserSecurityJpa(UserSecurityJPA userSecurityJpa) {
		this.userSecurityJpa = userSecurityJpa;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDateTime getRegistration() {
		return registration;
	}
	public void setRegistration(LocalDateTime registration) {
		this.registration = registration;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}