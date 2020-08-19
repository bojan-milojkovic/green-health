package com.green.health.store.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.annotations.EmailPattern;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class StoreDTO implements PojoParent {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@Pattern(regexp="^[^!@#$%^&*(),?`\";:{}|<>0-9_-]+$", message="Store name must contain only letters and white spaces")
	private String name;
	
	@Pattern(regexp="^[^@$%^&*()?`\";#{}|<>_]+$", message="Store description must contain only letters, digits, white spaces, ., !, ,, :, and -")
	private String description;
	
	private List<ProductDTO> specialOffers = new ArrayList<ProductDTO>();
	
	private String googleMapsLocation;
	
	private String storeWebSite;
	
	
	private MultipartFile image;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String username;
	
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDate added;
	
	@Pattern(regexp="[0-9]{1,2}-[0-9]{1,2}#([0-9]{1,2}-[0-9]{1,2}#)?([0-9]{1,2}-[0-9]{1,2}#)?", message="Incorrect work hours in the week format")
	private String workHours;
	
	private LocalDate closedUntil;
	
	@Pattern(regexp="^[+]?[0-9 -]+$", message="Phone number must contain only digits, white spaces, - and +")
	private String phone;
	
	@Pattern(regexp="^[^!@#$%^&*(),.?\\`\";:{}|<>0-9_-]+$", message="Country name must contain only letters and white spaces")
	private String country;
	
	@Pattern(regexp="^[0-9]+$", message="Postal code can consist only of digits")
	private String postalCode;
	
	@Pattern(regexp="^[^!@#$%^&*(),.?\\`\";:{}|<>0-9_-]+$", message="City name must contain only letters and white spaces")
	private String city;
	
	@Pattern(regexp="^[^!@$%^&*()?\\`\";:{}|<>_]+$", message="Address 1 must contain only letters, digits, white spaces, ., ,, #, and -")
	private String address1;
	
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Address 2 must contain only letters, digits, white spaces, ., ,, #, and -")
	private String address2;
	
	@EmailPattern
	private String email;
	
	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	public String getWorkHours() {
		return workHours;
	}

	public void setWorkHours(String workHours) {
		this.workHours = workHours;
	}

	public LocalDate getClosedUntil() {
		return closedUntil;
	}

	public void setClosedUntil(LocalDate closedUntil) {
		this.closedUntil = closedUntil;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getAdded() {
		return added;
	}

	public void setAdded(LocalDate added) {
		this.added = added;
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

	public List<ProductDTO> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(List<ProductDTO> specialOffers) {
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
}
