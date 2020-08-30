package com.green.health.ratings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.green.health.ratings.entities.LinkJPA;

@Repository
public interface RatingsRepository extends JpaRepository<LinkJPA, Long> {

	@Query("select l from LinkJPA l where l.herb.id=:hid and l.illness.id=:iid")
	public LinkJPA findByHerbAndIllness(@Param("hid") Long herbId, @Param("iid") Long illnessId);
}
