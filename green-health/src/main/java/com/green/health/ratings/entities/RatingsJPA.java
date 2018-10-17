package com.green.health.ratings.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.green.health.user.entities.UserJPA;

@Entity
@Table(name="ratings")
public class RatingsJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="rating_ones")
	private Integer ratingOnes;
	
	@Column(name="rating_twos")
	private Integer ratingTwos;
	
	@Column(name="rating_threes")
	private Integer ratingThrees;
	
	@Column(name="rating_fours")
	private Integer ratingFours;

	@Column(name="rating_fives")
	private Integer ratingFives;
	
	@ManyToOne
	@JoinColumn(name="link_id")
	private LinkJPA link;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserJPA raters;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRatingOnes() {
		return ratingOnes;
	}

	public void setRatingOnes(Integer ratingOnes) {
		this.ratingOnes = ratingOnes;
	}

	public Integer getRatingTwos() {
		return ratingTwos;
	}

	public void setRatingTwos(Integer ratingTwos) {
		this.ratingTwos = ratingTwos;
	}

	public Integer getRatingThrees() {
		return ratingThrees;
	}

	public void setRatingThrees(Integer ratingThrees) {
		this.ratingThrees = ratingThrees;
	}

	public Integer getRatingFours() {
		return ratingFours;
	}

	public void setRatingFours(Integer ratingFours) {
		this.ratingFours = ratingFours;
	}

	public Integer getRatingFives() {
		return ratingFives;
	}

	public void setRatingFives(Integer ratingFives) {
		this.ratingFives = ratingFives;
	}

	public LinkJPA getLink() {
		return link;
	}

	public void setLink(LinkJPA link) {
		this.link = link;
	}

	public UserJPA getRaters() {
		return raters;
	}

	public void setRaters(UserJPA raters) {
		this.raters = raters;
	}
}