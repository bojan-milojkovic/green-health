package com.green.health.illness.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private String engName;
	
	@Column
	private String description;
	
	@Column
	private String symptoms;
	
	@OneToMany(mappedBy="illness", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<LinkJPA> links = new HashSet<LinkJPA>();
	
	@OneToMany(mappedBy="illness", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Map<String,IllnessLocaleJPA> illnessLocales = new HashMap<String, IllnessLocaleJPA>();
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}
	
	public Map<String, IllnessLocaleJPA> getIllnessLocales() {
		return illnessLocales;
	}

	public void setIllnessLocales(Map<String, IllnessLocaleJPA> illnessLocales) {
		this.illnessLocales = illnessLocales;
	}

	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String srbName) {
		this.engName = srbName;
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
	
	public IllnessLocaleJPA getForSpecificLocale(final String locale) {
		return getIllnessLocales().get(locale);
	}
}