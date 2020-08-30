package com.green.health.ratings.entities;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class RatingDTO implements PojoParent{
	
	public RatingDTO(){}
	
	private RatingDTO(Long sid, Long pid, double ratings){
		this.ratings = ratings;
		this.storeId = sid;
		this.productId = pid;
	}
	
	public static RatingDTO buildStoreRating(Long sid, double ratings){
		return new RatingDTO(sid, null, ratings);
	}
	
	public static RatingDTO buildProductRating(Long pid, double ratings){
		return new RatingDTO(null, pid, ratings);
	}
	
	@JsonIgnore
	private String username;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long herbId;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[A-Za-z ]+$", message="Herb latin name must consist only of latin letters and spaces.")
	private String herbLatinName;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[^!@#$%^&*().,?`\";:{}|<>0-9_ -]+$", message="Herb local name cannot contain digits or special characters")
	private String herbLocalName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long illnessId;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[A-Za-z ]+$", message="Herb latin name must consist only of latin letters and spaces.")
	private String illnessLatinName;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[^!@#$%^&*().,?`\";:{}|<>0-9_ -]+$", message="Illness local name cannot contain digits or special characters")
	private String illnessLocalName;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1) @Max(5)
	private int newRatings;
	
	@JsonProperty(access = Access.READ_ONLY)
	private double ratings;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long storeId;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long  productId;
	
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getNewRatings() {
		return newRatings;
	}

	public void setNewRatings(int newRatings) {
		this.newRatings = newRatings;
	}

	public double getRatings() {
		return ratings;
	}

	public void setRatings(double ratings) {
		this.ratings = ratings;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getHerbId() {
		return herbId;
	}

	public void setHerbId(Long herbId) {
		this.herbId = herbId;
	}

	public String getHerbLatinName() {
		return herbLatinName;
	}

	public void setHerbLatinName(String herbLatinName) {
		this.herbLatinName = herbLatinName;
	}

	public String getHerbLocalName() {
		return herbLocalName;
	}

	public void setHerbLocalName(String herbLocalName) {
		this.herbLocalName = herbLocalName;
	}

	public Long getIllnessId() {
		return illnessId;
	}

	public void setIllnessId(Long illnessId) {
		this.illnessId = illnessId;
	}

	public String getIllnessLatinName() {
		return illnessLatinName;
	}

	public void setIllnessLatinName(String illnessLatinName) {
		this.illnessLatinName = illnessLatinName;
	}

	public String getIllnessLocalName() {
		return illnessLocalName;
	}

	public void setIllnessLocalName(String illnessLocalName) {
		this.illnessLocalName = illnessLocalName;
	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long id) {
		
	}
}