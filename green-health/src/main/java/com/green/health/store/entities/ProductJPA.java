package com.green.health.store.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.green.health.parents.RatingsParent;
import com.green.health.security.entities.UserSecurityJPA;

@Entity
@Table(name = "product")
public class ProductJPA implements RatingsParent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public ProductJPA(){}
	
	@Column(name = "local_name")
	private String localName;
	
	@Column(name = "local_description")
	private String localDescription;
	
	@Column
	private Double price;
	
	@Column
	private String currency;
	
	@Column
	private LocalDate expires;
	
	@ManyToOne
	@JoinColumn(name="store_id")
	private StoreJPA storeJpa;
	
	@Column
	private String contains;
	
	@Column
	private String treats;
	
	@Column
	private String city;
	
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
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="ratings",
			joinColumns = @JoinColumn(name="product_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<UserSecurityJPA> raters = new HashSet<UserSecurityJPA>();
	
	public LocalDate getExpires() {
		return expires;
	}

	public void setExpires(LocalDate expies) {
		this.expires = expies;
	}

	public StoreJPA getStoreJpa() {
		return storeJpa;
	}

	public void setStoreJpa(StoreJPA storeJpa) {
		this.storeJpa = storeJpa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocalDescription() {
		return localDescription;
	}

	public void setLocalDescription(String localDescription) {
		this.localDescription = localDescription;
	}

	public String getContains() {
		return contains;
	}

	public void setContains(String contains) {
		this.contains = contains;
	}

	public String getTreats() {
		return treats;
	}

	public void setTreats(String treats) {
		this.treats = treats;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
	public int hashCode(){
		long h = 1125899906842597L; // prime
		
		if(contains.contains(",")){
			for(String phh : contains.split(",")){
				h = 31*h + Long.parseLong(phh);
			}
		} else {
			h = 31*h + Long.parseLong(contains);
		}
		
		if(treats.contains(",")){
			for(String pfi : treats.split(",")){
				h = 31*h + Long.parseLong(pfi);
			}
		} else {
			h = 31*h + Long.parseLong(treats);
		}
	
		return (int)(31*h + storeJpa.getId());
	}
	
	@Override
	public boolean equals(Object o){
		if(o!=null && o instanceof ProductJPA){
			if(o==this)
				return true;
			return storeJpa.getId().equals(((ProductJPA)o).getStoreJpa().getId()) && 
					contains.equals(((ProductJPA)o).getContains()) && // compare herbs they contain
					treats.equals(((ProductJPA)o).getTreats()); // compare illnesses they treat
		}
		return false;
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