package com.green.health.illness.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.illness.entities.IllnessJPA;

@Repository
public interface IllnessRepository extends JpaRepository<IllnessJPA, Long>{

	@Query("select i from IllnessJPA i where i.srbName = :param")
	public IllnessJPA findBySrbName(@Param("param") final String srbName);
	
	@Query("select i from IllnessJPA i where i.latinName = :param")
	public IllnessJPA findByLatinName(@Param("param") final String latinName);
}
