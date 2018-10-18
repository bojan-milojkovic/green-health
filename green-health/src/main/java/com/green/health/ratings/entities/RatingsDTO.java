package com.green.health.ratings.entities;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class RatingsDTO implements PojoParent {

	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@NotNull
	private HerbDTO herb;
	
	@JsonProperty(access = Access.READ_WRITE)
	@NotNull
	private IllnessDTO illness;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Max(5) @Min(1)
	@NotNull
	private Integer newRatings;
	
	@JsonProperty(access = Access.READ_ONLY)
	@NotNull
	private float ratings;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HerbDTO getHerb() {
		return herb;
	}

	public void setHerb(HerbDTO herb) {
		this.herb = herb;
	}

	public IllnessDTO getIllness() {
		return illness;
	}

	public void setIllness(IllnessDTO illness) {
		this.illness = illness;
	}

	public float getRatings() {
		return ratings;
	}

	public void setRatings(float ratings) {
		this.ratings = ratings;
	}

	public Integer getNewRatings() {
		return newRatings;
	}

	public void setNewRatings(Integer newRatings) {
		this.newRatings = newRatings;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}