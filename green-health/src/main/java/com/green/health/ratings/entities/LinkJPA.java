package com.green.health.ratings.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.parents.PojoParent;
import com.green.health.security.entities.UserSecurityJPA;

@Entity
@Table(name="herb_for_illness")
public class LinkJPA implements PojoParent{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="herb_id")
	private HerbJPA herb;
	
	@ManyToOne
	@JoinColumn(name="illness_id")
	private IllnessJPA illness;
	
	@Column(name="rating_ones")
	private int ratingOnes;
	
	@Column(name="rating_twos")
	private int ratingTwos;
	
	@Column(name="rating_threes")
	private int ratingThrees;
	
	@Column(name="rating_fours")
	private int ratingFours;

	@Column(name="rating_fives")
	private int ratingFives;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="ratings",
			joinColumns = @JoinColumn(name="link_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<UserSecurityJPA> raters = new HashSet<UserSecurityJPA>();

	public LinkJPA(){}
	
	public LinkJPA(HerbJPA herbs, IllnessJPA illnesses) {
		this.herb = herbs;
		this.illness = illnesses;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HerbJPA getHerb() {
		return herb;
	}

	public void setHerb(HerbJPA herbs) {
		this.herb = herbs;
	}

	public IllnessJPA getIllness() {
		return illness;
	}

	public void setIllness(IllnessJPA illnesses) {
		this.illness = illnesses;
	}
	
	@Override
	public int hashCode() {
		// standard hashCode for 2 interchangeable integers
		int res = 17;
	    res = res * 31 + (int)(Math.min(herb.getId(), illness.getId()));
	    res = res * 31 + (int)(Math.max(herb.getId(), illness.getId()));
	    return res;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj != null && obj instanceof LinkJPA) {
		    if (obj == this)
		        return true;
		    // two objects are the same if they have the same herb and illness :
		    return this.getHerb().getId() == ((LinkJPA) obj).getHerb().getId() &&
		    		this.getIllness().getId() == ((LinkJPA) obj).getIllness().getId();
	    }
	    return false;
	}
	
	public double calculateRating() {
		int total = ratingOnes + ratingTwos + ratingThrees + ratingFours + ratingFives;
		return (ratingOnes + 2*ratingTwos + 3*ratingThrees + 4*ratingFours + 5*ratingFives)/
				(total!=0 ? total : 1);
	}

	public int getRatingOnes() {
		return ratingOnes;
	}

	public void setRatingOnes(int ratingOnes) {
		this.ratingOnes = ratingOnes;
	}

	public int getRatingTwos() {
		return ratingTwos;
	}

	public void setRatingTwos(int ratingTwos) {
		this.ratingTwos = ratingTwos;
	}

	public int getRatingThrees() {
		return ratingThrees;
	}

	public void setRatingThrees(int ratingThrees) {
		this.ratingThrees = ratingThrees;
	}

	public int getRatingFours() {
		return ratingFours;
	}

	public void setRatingFours(int ratingFours) {
		this.ratingFours = ratingFours;
	}

	public int getRatingFives() {
		return ratingFives;
	}

	public void setRatingFives(int ratingFives) {
		this.ratingFives = ratingFives;
	}

	public Set<UserSecurityJPA> getRaters() {
		return raters;
	}

	public void setRaters(Set<UserSecurityJPA> raters) {
		this.raters = raters;
	}
}