package com.green.health.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.green.health.security.dto.CredentialsDTO;
import com.green.health.security.service.SecurityService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
public class SecurityController {

	@Autowired
	private SecurityService securityServiceImpl;
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateToken(@RequestBody CredentialsDTO credentials) throws MyRestPreconditionsException{
		logger.debug("User "+credentials.getUsername()+" logging in ...");
		String token = securityServiceImpl.generateTokenForUser(credentials);
		logger.debug("User "+credentials.getUsername()+" log in successfull.");
		return token;
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String refreshToken(@RequestHeader("X-My-Security-Token") String token) throws MyRestPreconditionsException{
		return securityServiceImpl.refreshToken(token);
	}
}