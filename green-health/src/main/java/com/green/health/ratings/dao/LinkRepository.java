package com.green.health.ratings.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.green.health.ratings.entities.LinkJPA;

@Repository
public interface LinkRepository  extends JpaRepository<LinkJPA, Integer>{
	
}