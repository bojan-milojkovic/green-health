package com.green.health.user.controller;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/users")
public class UserController {

	private UserService userServiceImpl;
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	public UserController(UserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	// .../gh/users
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<UserDTO> getAllUsers(Principal principal){
		logger.debug("User "+principal.getName()+" get all users");
		userServiceImpl.setCurrentUsername(principal.getName());
		return userServiceImpl.getAll();
	}
	
	// .../gh/users/3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserById(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" get user by id=" +id);
		userServiceImpl.setCurrentUsername(principal.getName());
		return userServiceImpl.getOneById(id);
	}
	
	@RequestMapping(value = "/act/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void activateUser(@PathVariable("key") final String key) throws MyRestPreconditionsException{
		logger.debug("Activate user "+userServiceImpl.activateUser(key)+" with key="+key);
	}
	
	// .../gh/users/ue?username=Koala2&email=blatruc@gmail.com
	@RequestMapping(value = "/ue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserByUsernameOrEmail(@RequestParam(value="username", required=false) final String username,
														  @RequestParam(value="email", required=false) final String email,
														  @RequestParam(value="phone", required=false) final String phone,
														  Principal principal) 
														  throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" finding user by username="+username+" or email="+email+" or phone="+phone);
		userServiceImpl.setCurrentUsername(principal.getName());
		return userServiceImpl.getUserByUsernameOrEmail(username, email, phone);
	}
	
	@RequestMapping(value = "/prfimg/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageAsResource(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" getting the thumbnail image for user id="+id);
	    return userServiceImpl.getProfilePictureThumb(id, "profile_THUMBNAIL");
	}
	
	// .../gh/users
	// @Valid triggers the MyControllerAdvice when UserJPA is invalid
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveNewUser(@RequestBody @Valid UserDTO model) throws MyRestPreconditionsException {
		logger.debug("User "+model.getUsername()+" registerring...");
		userServiceImpl.addNew(model);
		logger.debug("User "+model.getUsername()+" registerred successfully.");
	}
	
	// .../gh/users/prfimg
	@RequestMapping(value = "/prfimg", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public void uploadProfilePic(@RequestParam(value="file", required=true) MultipartFile file, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" saving profile picture...");
		userServiceImpl.saveProfilePicture(file, principal.getName());
		logger.debug("User "+principal.getName()+" saved profile picture.");
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody UserDTO editUserById(@RequestBody @Valid UserDTO model, @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" editing user id="+id+" ...");
		model.setUsername(principal.getName());
		model = userServiceImpl.edit(model, id);
		logger.debug("User "+principal.getName()+" edit successfull.");
		return model;
		
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUserById(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" deleting user id="+id+" ...");
		RestPreconditions.assertTrue(
					(RestPreconditions.checkNotNull(userServiceImpl.getUserByUsernameOrEmail(principal.getName(), null, null)
							, "Delete user error", "Your user account no longer exists"))
				.getId() != id, "Access violation !!!", "You are trying to delete someone elses's user");
		
		userServiceImpl.delete(id);
		logger.debug("User "+principal.getName()+" deleted.");
	}
	
	// .../gh/users/cpw
	@RequestMapping(value="/cpw/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void changePassword(@RequestBody @Valid UserDTO model, @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" changing password for user id="+id+" ...");
		RestPreconditions.assertTrue(model!=null, "User password edit error !!!", "You are sending a request without the object");
		model.setId(id);
		userServiceImpl.changePassword(model, principal.getName());
		logger.debug("User "+principal.getName()+" changed password successfully.");
	}
}