package com.green.health.illness.entities;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class IllnessDTO implements PojoParent {

	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z ]{3,}", message="Illness Latin name can consist only of at least 3 letters and whitespaces")
	private String latinName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z ]+", message="Illness Serbian name can consist only of at least 3 letters and whitespaces")
	private String srbName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Illness description can consist only of letters, digits, dot, comma, and whitespaces")
	private String description;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Illness symptoms can consist only of letters, digits, dot, comma, and whitespaces")
	private String symptoms;
	
	
	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getSrbName() {
		return srbName;
	}

	public void setSrbName(String srbName) {
		this.srbName = srbName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public Long getId() {
		return id;
	}
}
