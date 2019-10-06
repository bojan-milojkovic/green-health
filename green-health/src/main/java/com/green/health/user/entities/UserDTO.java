package com.green.health.user.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.annotations.EmailPattern;
import com.green.health.parents.PojoParent;
import com.green.health.store.entities.StoreDTO;

@JsonInclude(Include.NON_NULL)
public class UserDTO implements PojoParent{
	
	@Min(0)
	private Long id;
	
	@Pattern(regexp="^[^!@#$%^&*(),?`\";:{}|<>]{5,}$", message="Username can consist only of letters, digits, dot, dash and underscore")
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="Password cannot contain a white space or ; and must be at least 6 characters long")
	private String password;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="New password cannot contain a white space or ; and must be at least 6 characters long")
	private String newPassword;
	
	@Pattern(regexp="^[^!@#$%^&*().,?`\";:{}|<>0-9_ -]+$", message="First name cannot contain digits or special characters")
	private String firstName;
	
	@Pattern(regexp="^[^!@#$%^&*().,?`\";:{}|<>0-9_ -]+$", message="Last name cannot contain digits or special characters")
	private String lastName;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape=Shape.STRING, pattern = "yyyy-MM-dd") // initialize LocalDate object from json string
	private LocalDateTime registration;
	
	@Pattern(regexp="^[+]?[0-9 ]+$", message="Phone number 1 must contain only digits white spaces and +")
	private String phone1;
	
	@Pattern(regexp="^[+]?[0-9 ]+$", message="Phone number 2 must contain only digits white spaces and +")
	private String phone2;
	
	@Pattern(regexp="^[^!@#$%^&*(),.?`\";:{}|<>0-9_-]+$", message="Country name must contain only letters and white spaces")
	private String country;
	
	@Pattern(regexp="^[0-9]+$", message="Postal code can consist only of digits")
	private String postalCode;
	
	@Pattern(regexp="^[^!@#$%^&*(),.?`\";:{}|<>0-9_-]+$", message="City name must contain only letters and white spaces")
	private String city;
	
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Address 1 must contain only letters, digits, white spaces, ., #, and -")
	private String address1;
	
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Address 2 must contain only letters, digits, white spaces, ., #, and -")
	private String address2;
	
	@JsonProperty(access = Access.READ_ONLY)
	private List<StoreDTO> stores = new ArrayList<StoreDTO>();
	
	public List<StoreDTO> getStores() {
		return stores;
	}

	public void setStores(List<StoreDTO> stores) {
		this.stores = stores;
	}

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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
