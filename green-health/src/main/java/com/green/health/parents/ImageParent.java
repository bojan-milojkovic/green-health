package com.green.health.parents;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class ImageParent {

	@JsonProperty(access = Access.READ_ONLY)
	protected MultipartFile image;
	
	@JsonProperty(access = Access.READ_ONLY)
	protected String imageFormat;

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
}