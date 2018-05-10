package com.green.health.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.green.health.user.entities.UserJPA;

@Repository
public interface UserRepository extends JpaRepository<UserJPA, Long>{

	// query annotation can be skipped ; JpaRepository can guess the query from method name
	@Query("select u from UserJPA u where u.email = :param")
	public UserJPA findByEmail(@Param("param") final String email);
	
	@Query("select u from UserJPA u where u.username = :param")
	public UserJPA findByUsername(@Param("param") final String username);
}
