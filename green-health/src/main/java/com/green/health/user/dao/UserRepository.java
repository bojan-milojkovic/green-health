package com.green.health.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.green.health.user.entities.UserJPA;

@Repository
public interface UserRepository extends JpaRepository<UserJPA, Long>{

	// query annotation can be skipped ; JpaRepository can guess the query from method name
	@Query("select u from UserJPA u where u.email = :param")
	public UserJPA findByEmail(final String email);
}
