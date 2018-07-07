package com.green.health.user.entities;

import java.time.LocalDateTime;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.annotations.EmailPattern;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class UserDTO implements PojoParent{
	
	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[A-Za-z0-9._-]{5,}$", message="Username can consist only of letters, digits, dot, dash and underscore")
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="Password cannot contain a white space or ; and must be at least 6 characters long")
	private String password;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[A-Z][a-z]+$", message="First name must start with a capital letter, followed by small letters.")
	private String firstName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Z][a-z]+", message="Last name must start with a capital letter, followed by small letters.")
	private String lastName;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape=Shape.STRING, pattern = "yyyy-MM-dd") // initialize LocalDate object from json string
	private LocalDateTime registration;
	
	@JsonProperty(access = Access.READ_WRITE)
	@EmailPattern
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
