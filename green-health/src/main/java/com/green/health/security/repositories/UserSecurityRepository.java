package com.green.health.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.security.entities.UserSecurityJPA;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurityJPA, String> {

	@Query("select u from UserSecurityJPA u where u.username = :param")
	UserSecurityJPA findByUsername(@Param("param") final String username);
	
}