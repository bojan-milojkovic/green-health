package com.green.health.user.service;

import java.util.List;
import com.green.health.user.entities.UserJPA;

public interface UserService {
	
	List<UserJPA> getAll();
	
	UserJPA getUserById(final Long id);
	
	UserJPA getUserByEmail(final String email);
	
	UserJPA getUserByUsername(final String username);

	void addUserToDb(UserJPA resource);
	
	UserJPA editUser(UserJPA resource, Long id);
	
	void deleteUser(final Long id);
}