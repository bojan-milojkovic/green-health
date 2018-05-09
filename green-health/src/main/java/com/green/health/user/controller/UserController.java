package com.green.health.user.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userServiceImpl;
	
	// .../users/all
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserJPA> getAllUsers(){
		return userServiceImpl.getAll();
	}
	
	// .../users/3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserJPA getUserById(@PathVariable("id") Long id){
		return userServiceImpl.getUserById(id);
	}
	
	// .../users?email=blatruc@gmail.com
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserJPA getUserByEmail(@RequestParam(value="email", required=true) String email){
		return userServiceImpl.getUserByEmail(email);
	}
	
	// .../users
	// @Valid triggers the MyControllerAdvice when UserJPA is invalid
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveNewUser(@RequestBody @Valid UserJPA dao){
		userServiceImpl.addUserToDb(dao);
	}
}
