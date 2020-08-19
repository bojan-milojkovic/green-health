package com.green.health.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.store.entities.StoreJPA;

@Repository
public interface StoreRepository extends JpaRepository<StoreJPA, Long>{
	
	@Query("select s from StoreJPA s where s.id = :param")
	public StoreJPA findStoreById(@Param("param") final Long id);
	
	@Query("select s from StoreJPA s where s.email = :param")
	public StoreJPA findByEmail(@Param("param") final String email);
	
	@Query("select s from StoreJPA s where s.name = :param")
	public StoreJPA findByName(@Param("param") final String name);
	
	@Query("select s from StoreJPA s where s.phone = :param")
	public StoreJPA findByPhoneNumber(@Param("param") final String phone);
}