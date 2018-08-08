package com.green.health.user.service;

import com.green.health.parents.ServiceParent;
import com.green.health.user.entities.MiniUserDTO;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface UserService extends ServiceParent<UserJPA, UserDTO>{
	
	UserDTO getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException;
	
	void changePassword(MiniUserDTO model, String username) throws MyRestPreconditionsException;
}