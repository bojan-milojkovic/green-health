package com.green.health.illness.entities;

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
@Table(name="illness_locale")
public class IllnessLocaleJPA implements PojoParent, IllnessInterface {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="illness_id")
	private IllnessJPA illness;
	
	@Column
	private String locale;
	
	@Column(name="local_name")
	private String localName;
	
	@Column
	private String description;
	
	@Column
	private String symptoms;
	
	@Column
	private String cause;
	
	@Column
	private String treatment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IllnessJPA getIllness() {
		return illness;
	}

	public void setIllness(IllnessJPA illness) {
		this.illness = illness;
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

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	
	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	@Override
	public boolean equals( Object o ) {
		if( o != null && o instanceof IllnessLocaleJPA) {
			if(o==this) {
        		return true;
        	}
			return this.locale.equals(((IllnessLocaleJPA)o).getLocale()) && 
					this.illness.getId() == ((IllnessLocaleJPA)o).getIllness().getId();
		}
		
		return false;
	}
	
	@Override
    public int hashCode() {
		/*// FNV hashing algorithm :
		long hash = 0xCBF29CE484222325L;
		for (String s : new String[] {this.locale, this.illness.getLatinName()}) {
			hash ^= s.hashCode();
			hash *= 0x100000001B3L;
		}
		return (int)hash;*/
		long h = 1125899906842597L; // prime
		
		for (int i = 0; i < locale.length(); i++) {
			h = 31*h + locale.charAt(i);
		}
		return (int)(31*h + illness.getId().hashCode());
    }
}
