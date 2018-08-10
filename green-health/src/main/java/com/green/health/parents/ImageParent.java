package com.green.health.parents;

import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class ImageParent {

	@JsonProperty(access = Access.READ_ONLY)
	protected String image;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Pattern(regexp="^(jpe?g)|(png)|(gif)$")
	protected String imageFormat;

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