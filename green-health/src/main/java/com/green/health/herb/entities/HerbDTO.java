package com.green.health.herb.entities;

import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class HerbDTO implements PojoParent{

	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z ]{3,}", message="Herb name can consist only of at least 3 letters and whitespaces")
	private String srbName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z ]{3,}", message="Herb Latin name can consist only of at least 3 letters and whitespaces")
	private String latinName;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb description can consist only of letters, digits, dot, comma, and whitespaces")
	private String description;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb growsAt can consist only of letters, digits, dot, comma, and whitespaces")
	private String growsAt;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb picking tips can consist only of letters, digits, dot, comma, and whitespaces")
	private String whenToPick;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb buying tips can consist only of letters, digits, dot, comma, and whitespaces")
	private String whereToBuy;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb properties can consist only of letters, digits, dot, comma, and whitespaces")
	private String properties;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Za-z0-9 .,:'()-]+", message="Herb use warnings can consist only of letters, digits, dot, comma, and whitespaces")
	private String warnings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrbName() {
		return srbName;
	}

	public void setSrbName(String srbName) {
		this.srbName = srbName;
	}

	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGrowsAt() {
		return growsAt;
	}

	public void setGrowsAt(String growsAt) {
		this.growsAt = growsAt;
	}

	public String getWhenToPick() {
		return whenToPick;
	}

	public void setWhenToPick(String whenToPick) {
		this.whenToPick = whenToPick;
	}

	public String getWhereToBuy() {
		return whereToBuy;
	}

	public void setWhereToBuy(String whereToBuy) {
		this.whereToBuy = whereToBuy;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}
}