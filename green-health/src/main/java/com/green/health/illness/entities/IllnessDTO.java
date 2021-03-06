package com.green.health.illness.entities;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class IllnessDTO implements PojoParent, IllnessInterface {

	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z ]{3,}", message="Illness Latin name can consist only of at least 3 latin letters and whitespaces")
	private String latinName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[^!@#$%^&*().,?`\";:{}|<>0-9_-]{3,}+", message="Illness local name can consist only of at least 3 letters and whitespaces")
	private String localName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[^!@#$%^&*()?`\";{}|<>]+", message="Illness description can consist only of letters, digits, dot, comma, and whitespaces")
	private String description;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[^!@#$%^&*()?`\";{}|<>]+", message="Illness symptoms can consist only of letters, digits, dot, comma, and whitespaces")
	private String symptoms;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[^!@#$%^&*()?`\";{}|<>]+", message="Illness cause can consist only of letters, digits, dot, comma, and whitespaces")
	private String cause;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[^!@#$%^&*()?`\";{}|<>]+", message="Illness treatment can consist only of letters, digits, dot, comma, and whitespaces")
	private String treatment;
	
	@JsonProperty(access = Access.READ_WRITE)
	private List<HerbDTO> herbs;
	

	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
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
	
	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public List<HerbDTO> getHerbs() {
		return herbs;
	}

	public void setHerbs(List<HerbDTO> herbs) {
		this.herbs = herbs;
	}
}