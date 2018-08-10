package com.green.health.herb.entities;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.ImageParent;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class MiniHerb extends ImageParent implements PojoParent {
	
	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[A-Za-z ]{3,}$", message="Name can consist only of letters, digits, dot, dash and underscore")
	private String name;

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
}
