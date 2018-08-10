package com.green.health.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.user.entities.MiniUser;
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
	public UserDTO getOneById(final Long id){
		return convertJpaToModel(userRepository.getOne(id));
	}
	
	public boolean isPostDataPresent(final UserDTO model) {
		return RestPreconditions.checkString(model.getUsername()) && 
				RestPreconditions.checkString(model.getPassword()) && 
				RestPreconditions.checkString(model.getEmail()) && 
				RestPreconditions.checkString(model.getFirstName()) && 
				RestPreconditions.checkString(model.getLastName());
	}
	
	public boolean isPatchDataPresent(final UserDTO model) {
		return /*RestPreconditions.checkString(model.getPassword()) ||*/ 
				RestPreconditions.checkString(model.getEmail()) || 
				RestPreconditions.checkString(model.getFirstName()) || 
				RestPreconditions.checkString(model.getLastName());
	}
	
	public UserDTO getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException{
		if(RestPreconditions.checkString(username)){
			UserSecurityJPA jpa = userSecurityRepository.findByUsername(username);
			if(jpa!=null){
				return convertJpaToModel(jpa.getUserJpa());
			}
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
	public void addNew(final UserDTO model) throws MyRestPreconditionsException {
		// check everything except id and registration is present in resource :
		if(isPostDataPresent(model)) {
			// check that username and email are unique :
			RestPreconditions.checkSuchEntityAlreadyExists(userSecurityRepository.findByUsername(model.getUsername()), 
					"Create user : the username "+ model.getUsername()+" belongs to another user.");
			RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
					"Create user : Email " + model.getEmail() + " belongs to another user.");
			
			userRepository.save(convertModelToJPA(model));
			
		}else{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("You cannot register",
							"The following data is missing from your registration form");
			if(RestPreconditions.checkString(model.getEmail())){
				ex.getErrors().add("email");
			}
			if(RestPreconditions.checkString(model.getFirstName())){
				ex.getErrors().add("first name");
			}
			if(RestPreconditions.checkString(model.getLastName())){
				ex.getErrors().add("last name");
			}
			if(RestPreconditions.checkString(model.getPassword())){
				ex.getErrors().add("password");
			}
			if(RestPreconditions.checkString(model.getUsername())){
				ex.getErrors().add("username");
			}
			
			throw ex;
		}
	}

	@Override
	public UserDTO edit(UserDTO model, final Long id) throws MyRestPreconditionsException {
		
		// check that ids match :
		UserSecurityJPA usJpa = userSecurityRepository.findByUsername(model.getUsername());
		RestPreconditions.assertTrue(usJpa.getId()==id, "You cannot edit someone else's user account.");
		
		UserJPA jpa = usJpa.getUserJpa();
		
		if(isPatchDataPresent(model)) {
			
			// email
			if(RestPreconditions.checkString(model.getEmail()) && !jpa.getEmail().equals(model.getEmail())) {
				// check this new email isn't in the db already :
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
						"Edit user : Email "+model.getEmail()+" belongs to another user.");
				
				jpa.setEmail(model.getEmail());
			}
			// first name
			if(RestPreconditions.checkString(model.getFirstName()) && !jpa.getFirstName().equals(model.getFirstName())) {
				jpa.setFirstName(model.getFirstName());
			}
			// last name
			if(RestPreconditions.checkString(model.getLastName()) && !jpa.getLastName().equals(model.getLastName())) {
				jpa.setLastName(model.getLastName());
			}
			
			userRepository.save(jpa);
			
			// update user security :
			usJpa.setLastUpdate(LocalDateTime.now());
			
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
	public void delete(final Long id) throws MyRestPreconditionsException {
		userRepository.deleteById(id);
	}
	
	@Override
	public void changePassword(MiniUser model, String username) throws MyRestPreconditionsException {
		
		{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("Change password error","request json is missing some elements.");
	
			if((model.getId()==null || (model.getId()!=null && model.getId()<0))) {
				ex.getErrors().add("Invalid or missing user id");
			}
			if(!RestPreconditions.checkString(model.getPassword())) {
				ex.getErrors().add("Original password is mandatory");
			}
			if(!RestPreconditions.checkString(model.getNewPassword())) {
				ex.getErrors().add("New password is mandatory");
			}
			
			if(!ex.getErrors().isEmpty()){
				throw ex;
			}
		}

		UserSecurityJPA jpa = userSecurityRepository.findByUsername(username);
		
		if(jpa.getId() == model.getId()) {
			// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
			if(BCrypt.checkpw(model.getPassword(), jpa.getPassword())) { 
				// new password should be different
				if(!BCrypt.checkpw(model.getNewPassword(), jpa.getPassword())) {
					jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
					userSecurityRepository.save(jpa);
				} else {
					throw new MyRestPreconditionsException("Change password error","Original password is the same as the new password");
				}
			} else {
				throw new MyRestPreconditionsException("Change password error","Original password does not match with the input value");
			}
		} else {
			throw new MyRestPreconditionsException("Access violation !!!","You are trying to change someone elses's password");
		}
	}

	@Override
	public UserJPA convertModelToJPA(UserDTO model) {
		UserJPA jpa = new UserJPA();
		
		jpa.setEmail(model.getEmail());
		jpa.setFirstName(model.getFirstName());
		jpa.setLastName(model.getLastName());
		jpa.setRegistration(LocalDateTime.now());
		
		UserSecurityJPA usJpa = new UserSecurityJPA();
		usJpa.setActive(true);
		usJpa.setNotLocked(true);
		usJpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
		usJpa.setUsername(model.getUsername());
		usJpa.setUserJpa(jpa);
		jpa.setUserSecurityJpa(usJpa);
		userSecurityRepository.save(usJpa);
		
		UserHasRolesJPA uhrJpa = new UserHasRolesJPA();
		uhrJpa.setUserSecurityJpa(usJpa);
		uhrJpa.setRoleJpa(roleRepository.getOne(1L));
		usJpa.getUserHasRolesJpa().add(uhrJpa);
		userHasRolesRepository.save(uhrJpa);
		
		return jpa;
	}

	@Override
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setId(jpa.getId());
		model.setUsername(jpa.getUserSecurityJpa().getUsername());
		model.setFirstName(jpa.getFirstName());
		model.setLastName(jpa.getLastName());
		model.setEmail(jpa.getEmail());
		
		return model;
	}
}
