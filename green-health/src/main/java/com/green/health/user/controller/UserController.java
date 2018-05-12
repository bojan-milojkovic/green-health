package com.green.health.user.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.service.UserService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/users")
public class UserController {

	@Autowired
	private UserService userServiceImpl;
	
	// .../gh/users/all
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<UserJPA> getAllUsers(){
		return userServiceImpl.getAll();
	}
	
	// .../gh/users/3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserJPA getUserById(@PathVariable("id") Long id){
		return userServiceImpl.getUserById(id);
	}
	
	// .../gh/users/ue?username=Koala2&email=blatruc@gmail.com
	@RequestMapping(value = "/ue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserJPA getUserByUsernameOrEmail(@RequestParam(value="username", required=false) String username,
														  @RequestParam(value="email", required=false) String email) 
														  throws MyRestPreconditionsException{
		return userServiceImpl.getUserByUsernameOrEmail(username, email);
	}	
	
	
	
	// .../gh/users/new
	// @Valid triggers the MyControllerAdvice when UserJPA is invalid
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveNewUser(@RequestBody @Valid UserJPA resource) throws MyRestPreconditionsException {
		userServiceImpl.addUserToDb(resource);
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody UserJPA editUserById(@RequestBody @Valid UserJPA resource, @PathVariable("id") Long id) throws MyRestPreconditionsException {
		return userServiceImpl.editUser(resource, id);
	}
	
	// .../gh/users/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUserById(@PathVariable("id") Long id) {
		userServiceImpl.deleteUser(id);
	}
}