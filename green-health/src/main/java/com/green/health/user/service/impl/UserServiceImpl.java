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
	
	public UserJPA getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException{
		if(RestPreconditions.checkString(username)){
			return userRepository.findByUsername(username);
		}
		if(RestPreconditions.checkString(email)){
			if(!email.matches("^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$")){
				throw new MyRestPreconditionsException("Finding user by parameters failed",
						"You must provide a valid email address.");
			}
			return userRepository.findByEmail(email);
		}
		throw new MyRestPreconditionsException("Finding user by parameters failed",
				"When searching a user, you must provide at least one parameter - username or password.");
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
			
		}else{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("You cannot register",
							"The following data is missing from your registration form");
			if(resource.getEmail()==null){
				ex.getErrors().add("email");
			}
			if(resource.getFirstName()==null){
				ex.getErrors().add("first name");
			}
			if(resource.getLastName()==null){
				ex.getErrors().add("last name");
			}
			if(resource.getPassword()==null){
				ex.getErrors().add("password");
			}
			if(resource.getUsername()==null){
				ex.getErrors().add("username");
			}
			
			throw ex;
		}
	}

	@Override
	public UserJPA editUser(UserJPA resource, Long id) throws MyRestPreconditionsException {
		// check that ids match :
		RestPreconditions.assertTrue(resource.getId()==id, "You cannot edit someone else's user account.");
		
		UserJPA jpa = userRepository.getOne(id);
		if(resource.isPatchDataPresent()) {

			// password
			if(resource.getPassword() != null) {
				// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
				if(!BCrypt.checkpw(resource.getPassword(), jpa.getPassword())) {
					jpa.setPassword(BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()));
				}
			}
			// email
			if(resource.getEmail()!=null && !jpa.getEmail().equals(resource.getEmail())) {
				// check this email isn't in the db already :
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
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("You cannot edit your user",
					"Your user edit request is invalid.");
			ex.getErrors().add("You must provide some editable data");
			ex.getErrors().add("Username is not editable");
			throw ex;
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
