package com.green.health.user.service;

import java.util.List;

import com.green.health.parents.ServiceParent;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface UserService extends ServiceParent<UserJPA, UserDTO>{
	
	List<UserDTO> getAll();
	
	UserDTO getUserById(final Long id);
	
	UserDTO getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException;

	void addUserToDb(UserDTO model) throws MyRestPreconditionsException;
	
	UserDTO editUser(UserDTO resource, Long id) throws MyRestPreconditionsException;
	
	void deleteUser(final Long id, final String username) throws MyRestPreconditionsException;
}