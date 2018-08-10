package com.green.health.herb.entities;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class MiniHerb implements PojoParent {
	
	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[A-Za-z ]{3,}$", message="Name can consist only of letters, digits, dot, dash and underscore")
	private String name;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String image;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^(jpe?g)|(png)|(gif)$")
	private String imageFormat;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
}
