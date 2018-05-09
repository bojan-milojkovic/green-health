package com.green.health.user.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.annotations.EmailPattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "user")
@JsonInclude(Include.NON_NULL)
public class UserJPA {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@Column(name="username")
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[A-Za-z0-9._-]{5,}$", message="Username can consist only of letters, digits, dot, dash and underscore")
	private String username;
	
	@Column(name="password")
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[^ ;]{5,}$", message="Password cannot contain a white space or ;")
	private String password;
	
	@Column(name="first_name")
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[A-Z][a-z]+$", message="First name must start with a capital letter, followed by small letters.")
	private String firstName;
	
	@Column(name="last_name")
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Z][a-z]+", message="First name must start with a capital letter, followed by small letters.")
	private String lastName;
	
	@Column(name="reg_date")
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape=Shape.STRING, pattern = "yyyy-MM-dd") // initialize LocalDateObject from json string
	private LocalDate registration;
	
	@Column(name="email")
	@JsonProperty(access = Access.READ_WRITE)
	@EmailPattern
	private String email;
	
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
	public LocalDate getRegistration() {
		return registration;
	}
	public void setRegistration(LocalDate registration) {
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
