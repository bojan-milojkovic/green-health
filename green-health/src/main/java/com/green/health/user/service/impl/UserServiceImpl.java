package com.green.health.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.images.storage.StorageService;
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

	private UserRepository userRepository;
	
	private RoleRepository roleRepository;

	private UserSecurityRepository userSecurityRepository;
	
	private UserHasRolesRepository userHasRolesRepository;
	
	private StorageService storageServiceImpl;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			UserSecurityRepository userSecurityRepository, UserHasRolesRepository userHasRolesRepository,
			StorageService storageServiceImpl) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userSecurityRepository = userSecurityRepository;
		this.userHasRolesRepository = userHasRolesRepository;
		this.storageServiceImpl = storageServiceImpl;
	}

	// get all users :
	public List<UserDTO> getAll(){
		return UserService.super.getAll();
	}
	
	// get a specific user :
	public UserDTO getOneById(final Long id) throws MyRestPreconditionsException{
		return UserService.super.getOneById(id);
	}
	
	public void isPostDataPresent(final UserDTO model) throws MyRestPreconditionsException {
		MyRestPreconditionsException ex = 
				new MyRestPreconditionsException("You cannot register",
						"The following data is missing from your registration form");
		if(!RestPreconditions.checkString(model.getEmail())){
			ex.getErrors().add("email");
		}
		if(!RestPreconditions.checkString(model.getFirstName())){
			ex.getErrors().add("first name");
		}
		if(!RestPreconditions.checkString(model.getLastName())){
			ex.getErrors().add("last name");
		}
		if(!RestPreconditions.checkString(model.getPassword())){
			ex.getErrors().add("password");
		}
		if(!RestPreconditions.checkString(model.getUsername())){
			ex.getErrors().add("username");
		}
		
		if(!ex.getErrors().isEmpty()) {
			throw ex;
		}
	}
	
	public boolean isPatchDataPresent(final UserDTO model) {
		return RestPreconditions.checkString(model.getEmail()) || 
				RestPreconditions.checkString(model.getFirstName()) || 
				RestPreconditions.checkString(model.getLastName());
	}
	
	public void saveProfilePicture(MultipartFile file, final String username) throws MyRestPreconditionsException{
		storageServiceImpl.saveImage(file, 
					RestPreconditions.checkNotNull(userSecurityRepository.findByUsername(username),
					"Save profile picture error","The user '"+username+"' doesn't exist")
				.getId(), 
				true);
	}
	
	public Resource readImage(Long id) throws MyRestPreconditionsException {// has to be 'id' so we can view other user's profile pic
		checkId(id);
		return storageServiceImpl.readImage(id, "profile_THUMBNAIL");
	}
	
	@Override
	public void delete(final Long id) throws MyRestPreconditionsException {
		getRepository().deleteById(id);
		storageServiceImpl.deleteImage(id, true);
	}
	
	public UserDTO getUserByUsernameOrEmail(final String username, final String email) throws MyRestPreconditionsException{
		if(RestPreconditions.checkString(username)){
			return convertJpaToModel(RestPreconditions.checkNotNull(userSecurityRepository.findByUsername(username), 
							"Finding user by parameters failed", 
							"There is no user in our database with that username.").getUserJpa());
		}
		if(RestPreconditions.checkString(email)){
			if(!email.matches("^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$")){
				throw new MyRestPreconditionsException("Finding user by parameters failed",
						"You must provide a valid email address.");
			}
			
			return convertJpaToModel(RestPreconditions.checkNotNull(userRepository.findByEmail(email), 
					"Finding user by parameters failed", 
					"There is no user in our database with that email."));
		}
		
		throw new MyRestPreconditionsException("Finding user by parameters failed",
				"When searching a user, you must provide at least one parameter - username or email.");
	}
	
	// add new user to db :
	public void addNew(final UserDTO model) throws MyRestPreconditionsException {
		// basic checks
		UserService.super.addNew(model);
		
		// check that username and email are unique :
		RestPreconditions.checkSuchEntityAlreadyExists(userSecurityRepository.findByUsername(model.getUsername()), 
				"Create user : the username "+ model.getUsername()+" belongs to another user.");
		
		if(!RestPreconditions.checkStringMatches(model.getEmail(),  "^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$")){
			throw new MyRestPreconditionsException("Create new user failed",
					"You must provide a valid email address.");
		}
		RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
				"Create user : Email " + model.getEmail() + " belongs to another user.");
		
		userRepository.save(convertModelToJPA(model));
	}

	@Override
	public UserDTO edit(UserDTO model, final Long id) throws MyRestPreconditionsException {
		// basic checks
		model = UserService.super.edit(model, id);
		
		UserSecurityJPA usJpa = userSecurityRepository.findByUsername(model.getUsername());
		RestPreconditions.assertTrue(usJpa.getId()==id, "Edit user error", "You cannot edit someone else's user account.");
		
		UserJPA jpa = usJpa.getUserJpa();
		
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
	}
	
	@Override
	public void changePassword(UserDTO model, String username) throws MyRestPreconditionsException {
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
		RestPreconditions.assertTrue(!model.getPassword().equals(model.getNewPassword()),
				"Change password error","Old password and new password should be different.");
		
		//check that user exists
		UserSecurityJPA jpa = RestPreconditions.checkNotNull(userSecurityRepository.findByUsername(username), 
				"Change password error : user you are changing the password for does not exist.");
		// check that ids match
		RestPreconditions.assertTrue(jpa.getId() == model.getId(), 
				"Access violation !!!","You are trying to change someone elses's password");
		// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
		RestPreconditions.assertTrue(BCrypt.checkpw(model.getPassword(), jpa.getPassword()), 
				"Change password error","Your entry for original password does not match with the DB value");
		
		jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
		userSecurityRepository.save(jpa);
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

	@Override
	public JpaRepository<UserJPA, Long> getRepository() {
		return userRepository;
	}
	
	@Override
	public String getName(){
		return "user";
	}
}
