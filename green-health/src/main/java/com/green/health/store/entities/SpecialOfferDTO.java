package com.green.health.store.entities;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class SpecialOfferDTO implements PojoParent {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;

	@Pattern(regexp="^[^!@#$%^&*(),.?`\";:{}|<>0-9_-]+$", message="Country name must contain only letters and white spaces")
	private String name;
	
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Description must contain only letters, digits, white spaces, ., #, and -")
	private String description;
	
	@Min(1)
	private Float price;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Long storeId;
	
	@Pattern(regexp="^[A-Z]{3}$", message="Currency must consist only of 3 capital leters")
	private String currency;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape=Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime expirationDate;
	
	@Override
	public boolean equals(Object o){
		if(o!=null && o instanceof SpecialOfferDTO){
			if(o==this) {
        		return true;
        	}
			return ((SpecialOfferDTO)o).getName().equals(name) && ((SpecialOfferDTO)o).getStoreId()==storeId;
		}
		return false;
	}
	@Override
	public int hashCode(){
		return storeId.intValue() + name.hashCode();
	}
	
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
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
}
