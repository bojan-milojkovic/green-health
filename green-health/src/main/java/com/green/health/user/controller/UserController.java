package com.green.health.user.controller;

import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import com.green.health.images.storage.StorageService;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/users")
public class UserController {

	private UserService userServiceImpl;
	
	private StorageService storageServiceImpl;
	
	@Autowired
	public UserController(UserService userServiceImpl, StorageService storageServiceImpl) {
		this.userServiceImpl = userServiceImpl;
		this.storageServiceImpl = storageServiceImpl;
	}

	// .../gh/users
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<UserDTO> getAllUsers(){
		return userServiceImpl.getAll();
	}
	
	// .../gh/users/3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserById(@PathVariable("id") final Long id) throws MyRestPreconditionsException{
		return userServiceImpl.getOneById(id);
	}
	
	// .../gh/users/ue?username=Koala2&email=blatruc@gmail.com
	@RequestMapping(value = "/ue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserByUsernameOrEmail(@RequestParam(value="username", required=false) final String username,
														  @RequestParam(value="email", required=false) final String email) 
														  throws MyRestPreconditionsException{
		return userServiceImpl.getUserByUsernameOrEmail(username, email);
	}
	
	@RequestMapping(value = "/prfimg/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageAsResource(@PathVariable("id") final Long id, HttpServletRequest request) throws MyRestPreconditionsException {
	    return storageServiceImpl.getImage(userServiceImpl.readImage(id), request);
	}
	
	// .../gh/users
	// @Valid triggers the MyControllerAdvice when UserJPA is invalid
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveNewUser(@RequestBody @Valid UserDTO model) throws MyRestPreconditionsException {
		userServiceImpl.addNew(model);
	}
	
	// .../gh/users/prfimg
	@RequestMapping(value = "/prfimg", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public void uploadProfilePic(@RequestParam(value="file", required=true) MultipartFile file, Principal principal) throws MyRestPreconditionsException{
		userServiceImpl.saveProfilePicture(file, principal.getName());
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody UserDTO editUserById(@RequestBody @Valid UserDTO model, @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model!=null, "Edit user error !", "Edit user cannot be performed without the user object.");
		model.setUsername(principal.getName());
		return userServiceImpl.edit(model, id);
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUserById(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		UserDTO model = userServiceImpl.getUserByUsernameOrEmail(principal.getName(), null);
		
		RestPreconditions.assertTrue(model!=null, "Delete user error", "Your user account no longer exists");
		RestPreconditions.assertTrue(model.getId() != id, "Access violation !!!", "You are trying to delete someone elses's user");
		
		userServiceImpl.delete(id, "User");
		
		storageServiceImpl.deleteImage(id, true);
	}
	
	// .../gh/users/cpw
	@RequestMapping(value="/cpw/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void changePassword(@RequestBody @Valid UserDTO model, @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model!=null, "User password edit error !!!", "You are sending a request without the object");
		RestPreconditions.assertTrue(id!=null, "User password edit error !!!", "User id is mandatory.");
		model.setId(id);
		userServiceImpl.changePassword(model, principal.getName());
	}
}