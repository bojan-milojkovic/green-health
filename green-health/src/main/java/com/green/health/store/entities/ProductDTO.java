package com.green.health.store.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class ProductDTO implements PojoParent {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[^!@#$%^&*(),.?`\";:{}|<>0-9_-]+$", message="Product local name must contain only letters and white spaces")
	private String localName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="^[^!@$%^&*()?`\";:{}|<>_]+$", message="Description must contain only letters, digits, white spaces, ., #, and -")
	private String localDescription;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Double price;
	
	@Pattern(regexp="^[A-Z]{3}$", message="Currency must consist only of 3 capital leters")
	private String currency;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long storeId;
	
	@JsonIgnore
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer minPrice;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer maxPrice;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<Long> contains = new TreeSet<Long>();
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<Long> treats = new TreeSet<Long>();
	
	@JsonProperty(access = Access.READ_ONLY)
	private List<HerbDTO> herbs = new ArrayList<HerbDTO>();
	
	@JsonProperty(access = Access.READ_ONLY)
	private List<IllnessDTO> illnesses = new ArrayList<IllnessDTO>();
	
	@JsonProperty(access = Access.READ_WRITE)
	private LocalDate expires;
	
	@JsonIgnore
	private String city;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getLocalDescription() {
		return localDescription;
	}

	public List<HerbDTO> getHerbs() {
		return herbs;
	}

	public void setHerbs(List<HerbDTO> herbs) {
		this.herbs = herbs;
	}

	public List<IllnessDTO> getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(List<IllnessDTO> illnesses) {
		this.illnesses = illnesses;
	}

	public void setLocalDescription(String localDescription) {
		this.localDescription = localDescription;
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	public Set<Long> getContains() {
		return contains;
	}

	public void setContains(Set<Long> contains) {
		this.contains = contains;
	}
	
	public Set<Long> getTreats() {
		return treats;
	}

	public void setTreats(Set<Long> treats) {
		this.treats = treats;
	}

	public LocalDate getExpires() {
		return expires;
	}

	public void setExpires(LocalDate expires) {
		this.expires = expires;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
