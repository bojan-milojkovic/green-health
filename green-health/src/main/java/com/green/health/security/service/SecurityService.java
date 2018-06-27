package com.green.health.security.service;

import com.green.health.security.dto.CredentialsDTO;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials);
}
