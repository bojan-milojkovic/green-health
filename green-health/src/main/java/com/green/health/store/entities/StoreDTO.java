package com.green.health.store.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.annotations.EmailPattern;
import com.green.health.parents.PojoParent;
import com.green.health.user.entities.UserDTO;

@JsonInclude(Include.NON_NULL)
public class StoreDTO implements PojoParent {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@Pattern(regexp="^[^!@#$%^&*(),.?`\";:{}|<>0-9_-]+$", message="Country name must contain only letters and white spaces")
	private String name;
	
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Description must contain only letters, digits, white spaces, ., #, and -")
	private String description;
	
	private Set<SpecialOfferDTO> specialOffers = new HashSet<SpecialOfferDTO>();
	
	private String googleMapsLocation;
	
	private String storeWebSite;
	
	@JsonProperty(access = Access.READ_ONLY)
	private UserDTO owner;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String username;
	
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDate added;
	
	@Pattern(regexp="^[+]?[0-9 -]+$", message="Phone number 1 must contain only digits white spaces and +")
	private String phone1;
	
	@Pattern(regexp="^[+]?[0-9 -]+$", message="Phone number 2 must contain only digits white spaces and +")
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
	
	@EmailPattern
	private String email;
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public LocalDate getAdded() {
		return added;
	}

	public void setAdded(LocalDate added) {
		this.added = added;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<SpecialOfferDTO> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(Set<SpecialOfferDTO> specialOffers) {
		this.specialOffers = specialOffers;
	}

	public String getGoogleMapsLocation() {
		return googleMapsLocation;
	}

	public void setGoogleMapsLocation(String googleMapsLocation) {
		this.googleMapsLocation = googleMapsLocation;
	}

	public String getStoreWebSite() {
		return storeWebSite;
	}

	public void setStoreWebSite(String storeWebSite) {
		this.storeWebSite = storeWebSite;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO user) {
		this.owner = user;
	}
}
