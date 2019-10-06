package com.green.health.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.email.EmailUtil;
import com.green.health.images.storage.StorageService;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.store.entities.StoreJPA;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;

	private UserSecurityRepository userSecurityRepository;
	
	private StorageService storageServiceImpl;
	
	private EmailUtil emailUtil;
	
	private String currentUsername;
	
	public void setCurrentUsername(final String cun){
		currentUsername = cun;
	}
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository,
			UserSecurityRepository userSecurityRepository,
			StorageService storageServiceImpl, EmailUtil emailUtil) {
		this.userRepository = userRepository;
		this.userSecurityRepository = userSecurityRepository;
		this.storageServiceImpl = storageServiceImpl;
		this.emailUtil = emailUtil;
	}
	
	public Set<StoreJPA> getStoreByUser(final String username) throws MyRestPreconditionsException {
		return RestPreconditions.checkNotNull(userSecurityRepository.findByUsername(username), 
				"Error while retrieving the store for the user "+username, 
				"No such user exists in the database.")
				.getUserJpa().getStoreJpa();
	}

	// get all users :
	public List<UserDTO> getAll(){
		return UserService.super.getAll();
	}
	
	// get a specific user :
	public UserDTO getOneById(final Long id) throws MyRestPreconditionsException{
		return UserService.super.getOneById(id);
	}
	
	public String activateUser(final String key) throws MyRestPreconditionsException {
		UserSecurityJPA jpa = RestPreconditions.checkNotNull(userSecurityRepository.findByHashKey(key), "User activation error", "Cannot find user with that activation key");
		
		jpa.setHashKey(null);
		jpa.setActive(true);
		userSecurityRepository.save(jpa);
		
		return jpa.getUsername();
	}
	
	public boolean isPatchDataPresent(final UserDTO model) {
		return RestPreconditions.checkString(model.getEmail()) || 
				RestPreconditions.checkString(model.getFirstName()) || 
				RestPreconditions.checkString(model.getLastName()) ||
				RestPreconditions.checkString(model.getCity()) ||
				RestPreconditions.checkString(model.getCountry()) ||
				RestPreconditions.checkString(model.getPostalCode()) ||
				RestPreconditions.checkString(model.getAddress1()) ||
				RestPreconditions.checkString(model.getAddress2()) ||
				RestPreconditions.checkString(model.getPhone1()) ||
				RestPreconditions.checkString(model.getPhone2())
				;
	}
	
	public void saveProfilePicture(MultipartFile file, final String username) throws MyRestPreconditionsException{
		storageServiceImpl.saveImage(file, 
					RestPreconditions.checkNotNull(userSecurityRepository.findByUsername(username),
					"Save profile picture error","The user '"+username+"' doesn't exist")
				.getId(), 
				true);
	}
	
	@Override
	public void delete(final Long id) throws MyRestPreconditionsException {
		UserService.super.delete(id);
		storageServiceImpl.deleteImage(id, true);
	}
	
	public UserDTO getUserByUsernameOrEmail(final String username, final String email, final String phone) throws MyRestPreconditionsException{
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
		if(RestPreconditions.checkString(phone)){
			if(!phone.matches("^[+]?[0-9 ]$")){
				throw new MyRestPreconditionsException("Finding user by parameters failed",
						"You must provide a valid phone number.");
			}
			
			return convertJpaToModel(RestPreconditions.checkNotNull(userRepository.findByPhone(phone), 
					"Finding user by parameters failed", 
					"There is no user in our database with that phone number."));
		}
		
		throw new MyRestPreconditionsException("Finding user by parameters failed",
				"When searching a user, you must provide at least one parameter - username, phone or email.");
	}
	
	// add new user to db :
	public void addNew(final UserDTO model) throws MyRestPreconditionsException {
		// basic checks
		UserService.super.addNew(model);
		
		// check that username and email are unique :
		RestPreconditions.checkSuchEntityAlreadyExists(userSecurityRepository.findByUsername(model.getUsername()), 
				"Create user : Username "+ model.getUsername()+" belongs to another user.");
		
		userRepository.save(convertModelToJPA(model));
	}

	@Override
	public UserDTO edit(UserDTO model, final Long id) throws MyRestPreconditionsException {
		// basic checks
		model = UserService.super.edit(model, id);
		
		UserSecurityJPA usJpa = RestPreconditions.checkNotNull(
				userSecurityRepository.findByUsername(model.getUsername()), 
				"Edit user error", "No user exists for that username");
		RestPreconditions.assertTrue(usJpa.getId()==id, 
				"Access violation", "You cannot edit someone else's user account.");
		
		// update user security :
		usJpa.setLastUpdate(LocalDateTime.now());
		userSecurityRepository.save(usJpa);
		
		return convertJpaToModel(userRepository.save(convertModelToJPA(model)));
	}
	
	@Override
	public void changePassword(UserDTO model, String username) throws MyRestPreconditionsException {
		{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("Change password error","request json is missing some elements.");
	
			checkId(model.getId(), "Change user password error");
			
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
				"Change password error","User you are changing the password for does not exist.");
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
	public UserJPA convertModelToJPA(final UserDTO model) throws MyRestPreconditionsException {
		UserJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new UserJPA();
			jpa.setRegistration(LocalDateTime.now());
			
			UserSecurityJPA usJpa = new UserSecurityJPA();
			usJpa.setActive(false);
			usJpa.setNotLocked(true);
			usJpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
			usJpa.setUsername(model.getUsername());
			usJpa.setUserJpa(jpa);
			usJpa.setUserHasRoles("ROLE_USER");
			jpa.setUserSecurityJpa(usJpa);
			
			String key = UUID.randomUUID().toString();
			usJpa.setHashKey(key);
			
			emailUtil.confirmRegistration(key, model.getFirstName()+" "+model.getLastName(), model.getEmail());
		} else {
			jpa = RestPreconditions.checkNotNull(userRepository.getOne(model.getId()),"User edit error",
					"User with id "+model.getId()+" does not exist in our database");
		}
		
		jpa.getUserSecurityJpa().setLastUpdate(LocalDateTime.now());
		
		if(RestPreconditions.checkString(model.getEmail())){
			if(!model.getEmail().equals(jpa.getEmail())){
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByEmail(model.getEmail()), 
						"User email "+model.getEmail()+" belongs to another user.");
			}
			jpa.setEmail(model.getEmail());
		}
		if(RestPreconditions.checkString(model.getFirstName()))
			jpa.setFirstName(model.getFirstName());
		if(RestPreconditions.checkString(model.getLastName()))
			jpa.setLastName(model.getLastName());
		if(RestPreconditions.checkString(model.getAddress1()))
			jpa.setAddress1(model.getAddress1());
		if(RestPreconditions.checkString(model.getAddress2()))
			jpa.setAddress2(model.getAddress2());
		if(RestPreconditions.checkString(model.getCity()))
			jpa.setCity(model.getCity());
		if(RestPreconditions.checkString(model.getCountry()))
			jpa.setCountry(model.getCountry());
		if(RestPreconditions.checkString(model.getPhone1())){
			if(!(model.getPhone1().equals(jpa.getPhone1()) || model.getPhone1().equals(jpa.getPhone2()))){
				// check this new phone number isn't in the db already :
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByPhone(model.getPhone1()), 
						"User phone number "+model.getPhone1()+" belongs to another user.");
			}
			jpa.setPhone1(model.getPhone1());
		}
		if(RestPreconditions.checkString(model.getPhone2())){
			if(!(model.getPhone2().equals(jpa.getPhone1()) || model.getPhone2().equals(jpa.getPhone2()))){
				RestPreconditions.checkSuchEntityAlreadyExists(userRepository.findByPhone(model.getPhone2()), 
						"User phone number "+model.getPhone2()+" belongs to another user.");
			}
			jpa.setPhone2(model.getPhone2());
		}
		return jpa;
	}

	@Override
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setId(jpa.getId());
		model.setRegistration(jpa.getRegistration());
		model.setUsername(jpa.getUserSecurityJpa().getUsername());
		model.setFirstName(jpa.getFirstName());
		
		if(jpa.getUserSecurityJpa().getUsername().equals(currentUsername)){
			model.setLastName(jpa.getLastName());
			model.setEmail(jpa.getEmail());
			model.setAddress1(jpa.getAddress1());
			model.setAddress2(jpa.getAddress2());
			model.setCity(jpa.getCity());
			model.setCountry(jpa.getCountry());
			model.setPhone1(jpa.getPhone1());
			model.setPhone2(jpa.getPhone2());
		}
		
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

	@Override
	public ResponseEntity<Resource> getProfilePictureThumb(final Long id, final String name)
			throws MyRestPreconditionsException {
		checkId(id, "Get user profile picture error");
		return storageServiceImpl.getImage(id, "profile_THUMBNAIL");
	}

	@Override
	public void getPostValidationErrors(UserDTO model, List<String> list) {
		if(!RestPreconditions.checkString(model.getEmail())){
			list.add("email");
		}
		if(!RestPreconditions.checkString(model.getFirstName())){
			list.add("first name");
		}
		if(!RestPreconditions.checkString(model.getLastName())){
			list.add("last name");
		}
		if(!RestPreconditions.checkString(model.getPassword())){
			list.add("password");
		}
		if(!RestPreconditions.checkString(model.getUsername())){
			list.add("username");
		}
		if(!RestPreconditions.checkString(model.getCity())){
			list.add("City");
		}
		if(!RestPreconditions.checkString(model.getCountry())){
			list.add("Country");
		}
		if(!RestPreconditions.checkString(model.getPostalCode())){
			list.add("postal code");
		}
		if(!RestPreconditions.checkString(model.getAddress1())){
			list.add("Address");
		}
		if(!RestPreconditions.checkString(model.getPhone1())){
			list.add("Pnone number 1");
		}
	}
}
