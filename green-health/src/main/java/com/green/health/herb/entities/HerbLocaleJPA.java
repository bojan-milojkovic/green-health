package com.green.health.herb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.green.health.parents.PojoParent;

@Entity
@Table(name="herb_locale")
public class HerbLocaleJPA implements PojoParent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="herb_id")
	private HerbJPA herb;
	
	@Column
	private String locale;
	
	@Column
	private String description;
	
	@Column(name="grows_at")
	private String growsAt;
	
	@Column(name="when_to_pick")
	private String whenToPick;
	
	@Column
	private String warnings;
	
	@Column
	private String properties;
	
	@Column(name="where_to_buy")
	private String whereToBuy;
	
	public Long getId() {
		return id;
	}

	public HerbJPA getHerb() {
		return herb;
	}

	public void setHerb(HerbJPA herb) {
		this.herb = herb;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getWhereToBuy() {
		return whereToBuy;
	}

	public void setWhereToBuy(String whereToBuy) {
		this.whereToBuy = whereToBuy;
	}

	public void setId(Long id) {
		this.id = id;
	}
}