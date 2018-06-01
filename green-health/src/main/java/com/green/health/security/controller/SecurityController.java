package com.green.health.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.green.health.security.dto.CredentialsDTO;
import com.green.health.security.service.SecurityService;

@Controller
public class SecurityController {

	@Autowired
	private SecurityService securityServiceImpl;
	
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateToken(@RequestBody CredentialsDTO credentials){
		return securityServiceImpl.generateTokenForUser(credentials);
	}
	
	@RequestMapping(value = "/descipher", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDetails descipherToken(HttpServletRequest request){
		String token = request.getHeaders("X-My-Security-Token").nextElement();
		return securityServiceImpl.getAuthoritiesFromToken(token);
	}
}