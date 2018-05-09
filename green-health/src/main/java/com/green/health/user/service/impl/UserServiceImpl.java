package com.green.health.user.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	// get all users :
	public List<UserJPA> getAll(){
		return userRepository.findAll();
	}
	
	// get a specific user :
	public UserJPA getUserById(final Long id){
		return userRepository.getOne(id);
	}
	
	// get user by email :
	public UserJPA getUserByEmail(final String email){
		return userRepository.findByEmail(email);
	}
	
	// add new user to db :
	public void addUserToDb(final UserJPA jpa){
		jpa.setRegistration(LocalDate.now());
		jpa.setPassword(BCrypt.hashpw(jpa.getPassword(), BCrypt.gensalt()));
		userRepository.save(jpa);
	}
}
