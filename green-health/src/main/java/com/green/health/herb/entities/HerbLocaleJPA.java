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
	private String localName;
	
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
	
	
	public HerbLocaleJPA() {
	}
	
	public HerbLocaleJPA(HerbJPA herb, String locale) {
		super();
		this.herb = herb;
		this.locale = locale;
	}

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
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
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
	
	@Override
	public boolean equals( Object o ) {
		if( o != null && o instanceof HerbLocaleJPA) {
			if(o==this) {
        		return true;
        	}
			return this.locale.equals(((HerbLocaleJPA)o).getLocale()) && 
					this.herb.getId() == ((HerbLocaleJPA)o).getHerb().getId();
		}
		
		return false;
	}
	
	@Override
    public int hashCode() {
		// FNV hashing algorithm :
		long hash = 0xCBF29CE484222325L;
		for (String s : new String[] {this.locale, this.herb.getLatinName()}) {
			hash ^= s.hashCode();
			hash *= 0x100000001B3L;
		}
		return (int)hash;
    }
}