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
	
	// .../gh/users?email=blatruc@gmail.com
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserJPA getUserByEmail(@RequestParam(value="email", required=true) String email){
		return userServiceImpl.getUserByEmail(email);
	}
	
	// .../gh/users/new
	// @Valid triggers the MyControllerAdvice when UserJPA is invalid
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveNewUser(@RequestBody @Valid UserJPA dao){
		System.out.println("\n\tinvoking method...");
		userServiceImpl.addUserToDb(dao);
	}
}
