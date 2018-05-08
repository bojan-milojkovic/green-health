package com.green.health.user.service;

import java.util.List;
import com.green.health.user.dao.UserJPA;

public interface UserService {
	
	List<UserJPA> getAll();
	
	UserJPA getUserById(final Long id);
	
	UserJPA getUserByEmail(final String email);

	void addUserToDb(final UserJPA jpa);
}