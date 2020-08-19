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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.green.health.parents.RatingsParent;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.user.entities.UserJPA;

@Entity
@Table(name = "store")
public class StoreJPA implements RatingsParent{
	
	@Id
	@Column(name="store_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public StoreJPA(){}
	
	public StoreJPA(final String phone){
		this.phone = phone;
	}
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="ratings",
			joinColumns = @JoinColumn(name="store_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<UserSecurityJPA> raters = new HashSet<UserSecurityJPA>();
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@OneToMany(mappedBy="storeJpa", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<ProductJPA> specialOffers = new HashSet<ProductJPA>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserJPA userJpa;
	
	@Column
	private String username;
	
	@Column(name="coordinates")
	private String googleMapsLocation;
	
	@Column(name="website")
	private String storeWebSite;
	
	@Column(name="work_hours")
	private String workHours;
	
	@Column(name="closed_until")
	private LocalDate closedUntil;
	
	@Column
	private LocalDate added;
	
	@Column
	private String phone;
	
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
	
	@Column(name = "rating_ones")
	private long ratingOnes;
	
	@Column(name = "rating_twos")
	private long ratingTwos;
	
	@Column(name = "rating_threes")
	private long ratingThrees;

	@Column(name = "rating_fours")
	private long ratingFours;
	
	@Column(name = "rating_fives")
	private long ratingFives;

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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Set<ProductJPA> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(Set<ProductJPA> specialOffers) {
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
	
	public void setRatingOnes(Long ratingOnes) {
		this.ratingOnes = ratingOnes;
	}

	public void setRatingTwos(Long ratingTwos) {
		this.ratingTwos = ratingTwos;
	}

	public void setRatingThrees(Long ratingThrees) {
		this.ratingThrees = ratingThrees;
	}

	public void setRatingFours(Long ratingFours) {
		this.ratingFours = ratingFours;
	}

	public void setRatingFives(Long ratingFives) {
		this.ratingFives = ratingFives;
	}

	@Override
	public boolean equals( Object o ) {
		if(o != null && o instanceof StoreJPA) {
			if(o==this){
				return true;
			}
			return ((StoreJPA)o).getPhone().equals(phone);
		}
		return false;
	}
	
	@Override
    public int hashCode() {
		long h = 1125899906842597L; // prime
		
		for (int i = 0; i < phone.length(); i++) {
			h = 31*h + phone.charAt(i);
		}
		return (int)h;
	}

	public long getRatingOnes() {
		return ratingOnes;
	}

	public long getRatingTwos() {
		return ratingTwos;
	}

	public long getRatingThrees() {
		return ratingThrees;
	}

	public long getRatingFours() {
		return ratingFours;
	}

	public long getRatingFives() {
		return ratingFives;
	}
	
	@Override
	public void setRatingOnes(long ratingOnes) {
		this.ratingOnes = ratingOnes;
	}

	@Override
	public void setRatingTwos(long ratingTwoos) {
		this.ratingTwos = ratingTwoos;
	}

	@Override
	public void setRatingThrees(long ratingThrees) {
		this.ratingThrees = ratingThrees;
	}

	@Override
	public void setRatingFours(long ratingFours) {
		this.ratingFours = ratingFours;
	}

	@Override
	public void setRatingFives(long ratingFives) {
		this.ratingFives = ratingFives;
	}

	public Set<UserSecurityJPA> getRaters() {
		return raters;
	}

	public void setRaters(Set<UserSecurityJPA> raters) {
		this.raters = raters;
	}
}