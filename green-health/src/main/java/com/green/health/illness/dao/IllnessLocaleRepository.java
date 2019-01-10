package com.green.health.illness.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.illness.entities.IllnessLocaleJPA;

@Repository
public interface IllnessLocaleRepository extends JpaRepository<IllnessLocaleJPA, Long> {
	
	@Query("select h from IllnessLocaleJPA h where h.locale = :p1 and h.localName = :p2")
	public IllnessLocaleJPA findWhereLocaleAndLocalName(@Param("p1") final String locale, @Param("p2") final String localName);
	
	@Query("select h from IllnessLocaleJPA h where h.locale = :p1 and h.illness.id = :p2")
	public IllnessLocaleJPA findWhereLocaleAndIllnessId(@Param("p1") final String locale, @Param("p2") final Long illnessId);

}
