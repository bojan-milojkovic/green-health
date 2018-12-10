package com.green.health.herb.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.herb.entities.HerbLocaleJPA;

@Repository
public interface HerbLocaleRepository  extends JpaRepository<HerbLocaleJPA, Long> {
	
	@Query("select h from HerbLocaleJPA h where h.locale = :p1 and h.localName = :p2")
	public HerbLocaleJPA findWhereLocaleAndLocalName(@Param("p1") final String locale, @Param("p2") final String localName);
}