package com.green.health.herb.entities;

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
@Table(name="herb")
public class HerbJPA implements PojoParent{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="eng_name")
	private String engName;
	
	@Column(name="latin_name")
	private String latinName;
	
	@Column
	private String description;
	
	@Column(name="grows_at")
	private String growsAt;
	
	@Column(name="when_to_pick")
	private String whenToPick;
	
	@Column(name="where_to_buy")
	private String whereToBuy;
	
	@Column
	private String warnings;
	
	@Column
	private String properties;
	
	@OneToMany(mappedBy="herb", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<LinkJPA> links = new HashSet<LinkJPA>();
	
	@OneToMany(mappedBy="herb", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Map<String, HerbLocaleJPA> herbLocales = new HashMap<String ,HerbLocaleJPA>();
	
	public HerbLocaleJPA getForSpecificLocale(String locale) {
		return herbLocales.get(locale);
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

	public void setEngName(String srbName) {
		this.engName = srbName;
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

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
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

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public Set<LinkJPA> getLinks() {
		return links;
	}

	public void setLinks(Set<LinkJPA> links) {
		this.links = links;
	}

	public Map<String, HerbLocaleJPA> getHerbLocales() {
		return herbLocales;
	}

	public void setHerbLocales(Map<String, HerbLocaleJPA> herbLocales) {
		this.herbLocales = herbLocales;
	}
}