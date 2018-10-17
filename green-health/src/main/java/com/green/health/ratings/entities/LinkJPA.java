package com.green.health.ratings.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.entities.IllnessJPA;

@Entity
@Table(name="herb_for_illness")
public class LinkJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="herb_id")
	private HerbJPA herbs;
	
	@ManyToOne
	@JoinColumn(name="illness_id")
	private IllnessJPA illnesses;

	@OneToMany(mappedBy="link", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<RatingsJPA> ratings = new HashSet<RatingsJPA>();
	
	public LinkJPA(){
		
	}
	
	public LinkJPA(HerbJPA herbs, IllnessJPA illnesses) {
		this.herbs = herbs;
		this.illnesses = illnesses;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HerbJPA getHerbs() {
		return herbs;
	}

	public void setHerbs(HerbJPA herbs) {
		this.herbs = herbs;
	}

	public IllnessJPA getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(IllnessJPA illnesses) {
		this.illnesses = illnesses;
	}

	public Set<RatingsJPA> getRatings() {
		return ratings;
	}

	public void setRatings(Set<RatingsJPA> ratings) {
		this.ratings = ratings;
	}
	
	@Override
	public int hashCode() {
		// standard hashCode for 2 interchangeable integers
		int res = 17;
	    res = res * 31 + (int)(Math.min(herbs.getId(), illnesses.getId()));
	    res = res * 31 + (int)(Math.max(herbs.getId(), illnesses.getId()));
	    return res;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (!(obj instanceof LinkJPA))
	        return false;
	    if (obj == this)
	        return true;
	    // two objects are the same if they have the same herb and illness :
	    return this.getHerbs().getId() == ((LinkJPA) obj).getHerbs().getId() &&
	    		this.getIllnesses().getId() == ((LinkJPA) obj).getIllnesses().getId();
	}
}