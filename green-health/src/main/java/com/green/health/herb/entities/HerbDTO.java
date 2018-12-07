package com.green.health.herb.entities;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.parents.PojoParent;

@JsonInclude(Include.NON_NULL)
public class HerbDTO implements PojoParent{

	@JsonProperty(access = Access.READ_WRITE)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String engName;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String latinName;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String description;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String growsAt;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String whenToPick;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String whereToBuy;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String properties;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String warnings;
	
	@JsonProperty(access = Access.READ_WRITE)
	private List<IllnessDTO> illnesses;
	
	private MultipartFile image;
	
	

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
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

	public List<IllnessDTO> getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(List<IllnessDTO> illnesses) {
		this.illnesses = illnesses;
	}
}