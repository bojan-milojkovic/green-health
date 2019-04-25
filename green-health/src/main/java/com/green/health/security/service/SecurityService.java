package com.green.health.security.service;

import com.green.health.security.dto.CredentialsDTO;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials) throws MyRestPreconditionsException;
	
	public String refreshToken(final String token) throws MyRestPreconditionsException;
}
