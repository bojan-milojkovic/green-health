package com.green.health.ratings.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.ratings.entities.RatingsJPA;

@Repository
public interface RatingsRepository extends JpaRepository<RatingsJPA, Long>{
	
	@Query("select r from RatingsJPA where r.users.id = :user and r.links.id = :links")
	public RatingsJPA findRatingsByUserAndLink(@Param("user") Long userId, @Param("link") Integer LinkId);

}