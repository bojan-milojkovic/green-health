package com.green.health.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.green.health.security.entities.UserHasRolesJPA;

@Repository
public interface UserHasRolesRepository extends JpaRepository<UserHasRolesJPA, Long>{

}
