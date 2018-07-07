package com.green.health.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.security.entities.UserHasRolesJPA;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.RoleRepository;
import com.green.health.security.repositories.UserHasRolesRepository;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserSecurityRepository userSecurityRepository;
	
	@Autowired
	private UserHasRolesRepository userHasRolesRepository;
	
	// get all users :
	public List<UserDTO> getAll(){
		return userRepository.findAll().stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}
	
	// get a specific user :
	public UserDTO getUserById(final Long id){
		return convertJpaToModel(userRepository.getOne(id));
	}
	
	public boolean isPostDataPresent(final UserDTO model) {
		return model!=null && model.getUsername()!=null && model.getPassword()!=null && model.getEmail()!=null 
				&& model.getFirstName()!=null && model.getLastName()!=null;
	}
	
	public boolean isPatchDataPresent(final UserDTO model) {
		return model.getPassword()!=null || model.getEmail()!=null || model.getFirstName()!=null || model.getLastName()!=null;
	}
	
	public UserDTO getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException{
		if(RestPreconditions.checkString(username)){
			return convertJpaToModel(userRepository.findByUsername(username));
		}
		if(RestPreconditions.checkString(email)){
			if(!email.matches("^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$")){
				throw new MyRestPreconditionsException("Finding user by parameters failed",
						"You must provide a valid email address.");
			}
			return convertJpaToModel(userRepository.findByEmail(email));
		}
		throw new MyRestPreconditionsException("Finding user by parameters failed",
				"When searching a user, you must provide at least one parameter - username or email.");
	}
	
	// add new user to db :
	public void addUserToDb(final UserDTO model) throws MyRestPreconditionsException {
		// check everything except id and registration is present in resource :
		if(isPostDataPresent(model)) {
			// check that username and email are unique :
			RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByUsername(model.getUsername()), 
					"Create user : the username "+ model.getUsername()+" belongs to another user.");
			RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
					"Create user : Email " + model.getEmail() + " belongs to another user.");
			
			model.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
			
			UserJPA jpa = convertModelToJPA(model);
			
			userRepository.save(jpa);
			
			UserSecurityJPA usJpa = new UserSecurityJPA();
			usJpa.setActive(true);
			usJpa.setNotLocked(true);
			usJpa.setPassword(jpa.getPassword());
			usJpa.setUsername(jpa.getUsername());
			usJpa.setUserJpa(jpa);
			jpa.setUserSecurityJpa(usJpa);
			userSecurityRepository.save(usJpa);
			
			UserHasRolesJPA uhrJpa = new UserHasRolesJPA();
			uhrJpa.setUserSecurityJpa(usJpa);
			uhrJpa.setRoleJpa(roleRepository.getOne(1L));
			usJpa.getUserHasRolesJpa().add(uhrJpa);
			userHasRolesRepository.save(uhrJpa);
			
		}else{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("You cannot register",
							"The following data is missing from your registration form");
			if(model.getEmail()==null){
				ex.getErrors().add("email");
			}
			if(model.getFirstName()==null){
				ex.getErrors().add("first name");
			}
			if(model.getLastName()==null){
				ex.getErrors().add("last name");
			}
			if(model.getPassword()==null){
				ex.getErrors().add("password");
			}
			if(model.getUsername()==null){
				ex.getErrors().add("username");
			}
			
			throw ex;
		}
	}

	@Override
	public UserDTO editUser(UserDTO model, Long id) throws MyRestPreconditionsException {
		
		// check that ids match :
		UserJPA jpa = userRepository.findByUsername(model.getUsername());
		RestPreconditions.assertTrue(jpa.getId()==id, "You cannot edit someone else's user account.");
		
		if(isPatchDataPresent(model)) {

			// password
			if(model.getPassword() != null) {
				// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
				if(!BCrypt.checkpw(model.getPassword(), jpa.getPassword())) {
					jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
				}
			}
			// email
			if(model.getEmail()!=null && !jpa.getEmail().equals(model.getEmail())) {
				// check this new email isn't in the db already :
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
						"Edit user : Email "+model.getEmail()+" belongs to another user.");
				
				jpa.setEmail(model.getEmail());
			}
			// first name
			if(model.getFirstName()!=null && !jpa.getFirstName().equals(model.getFirstName())) {
				jpa.setFirstName(model.getFirstName());
			}
			// last name
			if(model.getLastName()!=null && !jpa.getLastName().equals(model.getLastName())) {
				jpa.setLastName(model.getLastName());
			}
			
			userRepository.save(jpa);
			
			// update user security :
			UserSecurityJPA usJpa = jpa.getUserSecurityJpa();
			usJpa.setLastUpdate(LocalDateTime.now());
			usJpa.setPassword(jpa.getPassword()); // maybe it was changed
			
			userSecurityRepository.save(usJpa);
			
			return convertJpaToModel(jpa);
			
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("You cannot edit your user",
					"Your user edit request is invalid.");
			ex.getErrors().add("You must provide some editable data");
			ex.getErrors().add("Username is not editable");
			throw ex;
		}
	}

	@Override
	public void deleteUser(final Long id, final String username) throws MyRestPreconditionsException {
		UserJPA jpa = userRepository.getOne(id);
		if(jpa != null) {
			if(jpa.getUsername().equals(username)){
				userRepository.deleteById(id);
			} else {
				throw new MyRestPreconditionsException("Access violation !!!","You are trying to delete someone elses's user");
			}
		} else {
			throw new MyRestPreconditionsException("Invalid id","Entity with that id does not exist in the system.");
		}
	}

	@Override
	public UserJPA convertModelToJPA(UserDTO model) {
		UserJPA jpa = new UserJPA();
		if(model.getId()==null){
			jpa.setEmail(model.getEmail());
			jpa.setFirstName(model.getFirstName());
			jpa.setLastName(model.getLastName());
			jpa.setPassword(model.getPassword());
			jpa.setRegistration(LocalDateTime.now());
			jpa.setUsername(model.getUsername());
		} else {
			if(RestPreconditions.checkString(model.getEmail())){
				jpa.setEmail(model.getEmail());
			}
			if(RestPreconditions.checkString(model.getFirstName())){
				jpa.setFirstName(model.getFirstName());
			}
			if(RestPreconditions.checkString(model.getLastName())){
				jpa.setLastName(model.getLastName());
			}
			if(RestPreconditions.checkString(model.getUsername())){
				jpa.setUsername(model.getUsername());
			}
		}
		
		return jpa;
	}

	@Override
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setId(jpa.getId());
		model.setUsername(jpa.getUsername());
		model.setFirstName(jpa.getFirstName());
		model.setLastName(jpa.getLastName());
		model.setEmail(jpa.getEmail());
		
		return model;
	}
}
