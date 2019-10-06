package com.green.health.user.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.green.health.parents.PojoParent;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.store.entities.StoreJPA;

@Entity
@Table(name = "user")
public class UserJPA implements PojoParent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="reg_date")
	private LocalDateTime registration;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone_1")
	private String phone1;
	
	@Column(name="phone_2")
	private String phone2;
	
	@Column
	private String country;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column
	private String city;
	
	@Column(name="address_1")
	private String address1;
	
	@Column(name="address_2")
	private String address2;
	
	@OneToOne(mappedBy="userJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private UserSecurityJPA userSecurityJpa;
	
	@OneToMany(mappedBy="userJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<StoreJPA> storeJpa = new HashSet<StoreJPA>();
	
	public Set<StoreJPA> getStoreJpa() {
		return storeJpa;
	}

	public void setStoreJpa(Set<StoreJPA> storeJpa) {
		this.storeJpa = storeJpa;
	}

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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