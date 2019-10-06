package com.green.health.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.green.health.store.entities.SpecialOfferJPA;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOfferJPA, Long>{

}
