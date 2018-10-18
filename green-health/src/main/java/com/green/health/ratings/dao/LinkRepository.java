package com.green.health.ratings.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.ratings.entities.LinkJPA;

@Repository
public interface LinkRepository  extends JpaRepository<LinkJPA, Integer>{

	@Query("select l from LinkJPA l where l.herbs.id = :herb and l.illnesses.id = :illness")
	public LinkJPA findOneByHerbAndIllness(@Param("herb") Long herbId, @Param("illness") Long illnessId);
}