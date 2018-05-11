package com.green.health.user.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

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
	
	// get by username :
	public UserJPA getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	// get user by email :
	public UserJPA getUserByEmail(final String email){
		return userRepository.findByEmail(email);
	}
	
	// add new user to db :
	public void addUserToDb(final UserJPA resource) throws MyRestPreconditionsException {
		// check everything except id and registration is present in resource :
		if(resource.isPostDataPresent()) {
			// check that username and email are unique :
			RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByUsername(resource.getUsername()), 
					"Create user : the username "+ resource.getUsername()+" belongs to another user.");
			RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(resource.getEmail()), 
					"Create user : Email " + resource.getEmail() + " belongs to another user.");
			
			resource.setRegistration(LocalDate.now());
			resource.setPassword(BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()));
			userRepository.save(resource);
			
		}
	}

	@Override
	public UserJPA editUser(UserJPA resource, Long id) throws MyRestPreconditionsException {
		UserJPA jpa = userRepository.getOne(id);
		if(resource.isPatchDataPresent()) {

			// password
			if(resource.getPassword() != null) {
				resource.setPassword(BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()));
				if(!jpa.getPassword().equals(resource.getPassword())) {
					jpa.setPassword(resource.getPassword());
				}
			}
			// email
			if(resource.getEmail()!=null && !jpa.getEmail().equals(resource.getEmail())) {
				// check this username isn't in the db already :
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(resource.getEmail()), 
						"Edit user : Email "+resource.getEmail()+" belongs to another user.");
				
				jpa.setEmail(resource.getEmail());
			}
			// first name
			if(resource.getFirstName()!=null && !jpa.getFirstName().equals(resource.getFirstName())) {
				jpa.setFirstName(resource.getFirstName());
			}
			// last name
			if(resource.getLastName()!=null && !jpa.getLastName().equals(resource.getLastName())) {
				jpa.setLastName(resource.getLastName());
			}
			
			userRepository.save(jpa);
		}
		
		return jpa;
	}

	@Override
	public void deleteUser(Long id) {
		if(userRepository.getOne(id) != null) {
			userRepository.deleteById(id);
		}
	}
}
