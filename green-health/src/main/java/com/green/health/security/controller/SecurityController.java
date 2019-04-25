package com.green.health.security.controller;

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
	
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateToken(@RequestBody CredentialsDTO credentials) throws MyRestPreconditionsException{
		return securityServiceImpl.generateTokenForUser(credentials);
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String refreshToken(@RequestHeader("X-My-Security-Token") String token) throws MyRestPreconditionsException{
		return securityServiceImpl.refreshToken(token);
	}
}