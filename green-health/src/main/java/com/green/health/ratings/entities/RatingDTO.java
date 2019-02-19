package com.green.health.ratings.entities;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class RatingDTO implements PojoParent{
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String username;
	
	@JsonProperty(access = Access.READ_WRITE)
	@NotNull
	@Min(1)
	private Long herbId;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String herbLatinName;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String herbLocalName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@NotNull
	@Min(1)
	private Long illnessId;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String illnessLatinName;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String illnessLocalName;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1) @Max(5)
	private int newRatings;
	
	@JsonProperty(access = Access.READ_ONLY)
	private double ratings;
	
	
	
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