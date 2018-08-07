package com.green.health.herb.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.herb.entities.HerbJPA;

@Repository
public interface HerbRepository extends JpaRepository<HerbJPA, Long>{
	
	@Query("select h from HerbJPA h where h.srbName = :param")
	public HerbJPA getHerbBySrbName(@Param("param") final String name);

	@Query("select h from HerbJPA h where h.latinName = :param")
	public HerbJPA getHerbByLatinName(@Param("param") final String latinName);
}
