package com.green.health.store.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.green.health.parents.PojoParent;
import com.green.health.user.entities.UserJPA;

@Entity
@Table(name = "store")
public class StoreJPA implements PojoParent{
	
	@Id
	@Column(name="store_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public StoreJPA(){}
	
	public StoreJPA(final Long id){
		this.id = id;
	}
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@OneToMany(mappedBy="storeJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<SpecialOfferJPA> specialOffers = new HashSet<SpecialOfferJPA>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserJPA userJpa;
	
	@Column(name="coordinates")
	private String googleMapsLocation;
	
	@Column(name="website")
	private String storeWebSite;
	
	@Column
	private LocalDate added;
	
	@Column
	private String phone1;
	
	@Column
	private String phone2;
	
	@Column
	private String country;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column
	private String city;
	
	@Column
	private String address1;
	
	@Column
	private String address2;
	
	@Column
	private String email;
	
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

	@Override
	public Long getId() {
		return id;
	}

	@Override
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

	public Set<SpecialOfferJPA> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(Set<SpecialOfferJPA> specialOffers) {
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

	public UserJPA getUserJpa() {
		return userJpa;
	}

	public void setUserJpa(UserJPA userJpa) {
		this.userJpa = userJpa;
	}
	
	@Override
	public boolean equals( Object o ) {
		if(o != null && o instanceof StoreJPA) {
			if(o==this){
				return true;
			}
			return ((StoreJPA)o).getId() == id;
		}
		return false;
	}
	
	@Override
    public int hashCode() {
		return id.intValue();
	}
}