package com.green.health.user.entities;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class MiniUser implements PojoParent{

	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^[A-Za-z0-9._-]{3,}$", message="Name can consist only of letters, digits, dot, dash and underscore")
	private String name;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="Password cannot contain a white space or ; and must be at least 6 characters long")
	private String password;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="Password cannot contain a white space or ; and must be at least 6 characters long")
	private String newPassword;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String image;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^(jpe?g)|(png)|(gif)$")
	private String imageFormat;
	
	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}
}