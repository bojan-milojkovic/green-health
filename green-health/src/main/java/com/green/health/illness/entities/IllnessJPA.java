package com.green.health.illness.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.green.health.parents.PojoParent;
import com.green.health.ratings.entities.LinkJPA;

@Entity
@Table(name="illness")
public class IllnessJPA implements PojoParent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="latin_name")
	private String latinName;
	
	@Column(name="srb_name")
	private String srbName;
	
	@Column
	private String description;
	
	@Column
	private String symptoms;
	
	@OneToMany(mappedBy="illnesses", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<LinkJPA> links = new HashSet<LinkJPA>();
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

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

	public Set<LinkJPA> getLinks() {
		return links;
	}

	public void setLinks(Set<LinkJPA> links) {
		this.links = links;
	}
}