package com.green.health.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.green.health.security.entities.RoleJPA;

@Repository
public interface RoleRepository extends JpaRepository<RoleJPA, Long>{

}
