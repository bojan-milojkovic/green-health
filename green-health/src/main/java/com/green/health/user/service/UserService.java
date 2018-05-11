package com.green.health.user.service;

import java.util.List;
import com.green.health.user.entities.UserJPA;
import com.green.health.util.exceptions.MyValueAlreadyTakenException;

public interface UserService {
	
	List<UserJPA> getAll();
	
	UserJPA getUserById(final Long id);
	
	UserJPA getUserByEmail(final String email);
	
	UserJPA getUserByUsername(final String username);

	void addUserToDb(UserJPA resource) throws MyValueAlreadyTakenException;
	
	UserJPA editUser(UserJPA resource, Long id) throws MyValueAlreadyTakenException;
	
	void deleteUser(final Long id);
}