package com.green.health.store.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.green.health.parents.PojoParent;

@Entity
@Table(name = "special_offer")
public class SpecialOfferJPA implements PojoParent{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public SpecialOfferJPA(){}
	
	public SpecialOfferJPA(String name, Long storeId){
		this.name = name;
		this.storeJpa = new StoreJPA(storeId);
	}
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Column
	private Float price;
	
	@Column
	private String currency;
	
	@Column(name="expiration_date")
	private LocalDateTime expirationDate;
	
	@ManyToOne
	@JoinColumn(name="store_id")
	private StoreJPA storeJpa;
	
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
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

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
	public int hashCode(){
		return storeJpa.getId().intValue() + name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o!=null && o instanceof SpecialOfferJPA){
			if(o==this)
				return true;
			return ((SpecialOfferJPA)o).getName().equals(name) && ((SpecialOfferJPA)o).getStoreJpa().equals(storeJpa);
		}
		return false;
	}
}