package com.green.health.ratings.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ratings")
public class RatingsJPA {
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="link_id")
	private Long linkId;
	
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
	
	
}