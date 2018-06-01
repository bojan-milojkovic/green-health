package com.green.health.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import com.green.health.security.dto.CredentialsDTO;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials);

	UserDetails getAuthoritiesFromToken(final String token);
}
