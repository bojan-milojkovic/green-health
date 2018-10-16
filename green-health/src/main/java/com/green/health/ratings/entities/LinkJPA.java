package com.green.health.ratings.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="herb_for_illness")
public class LinkJPA {
	
	@Id
	private Long id;
	
	@Column(name="herb_id")
	private Long herbId;
	
	@Column(name="illness_id")
	private Long illnessId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHerbId() {
		return herbId;
	}

	public void setHerbId(Long herbId) {
		this.herbId = herbId;
	}

	public Long getIllnessId() {
		return illnessId;
	}

	public void setIllnessId(Long illnessId) {
		this.illnessId = illnessId;
	}
}