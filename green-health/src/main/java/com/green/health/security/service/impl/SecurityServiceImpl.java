package com.green.health.security.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.security.JwtTokenUtil;
import com.green.health.security.dto.CredentialsDTO;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.security.service.SecurityService;
import com.green.health.util.exceptions.MyRestPreconditionsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserSecurityRepository userSecurityRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public String generateTokenForUser(CredentialsDTO credentials) throws MyRestPreconditionsException{
		
		// check username :
		UserSecurityJPA jpa = userSecurityRepository.findByUsername(credentials.getUsername());
		if(jpa != null){
			// check password - BCrypt.checkpw(password_plaintext, stored_hash)
			if(BCrypt.checkpw(credentials.getPassword(), jpa.getPassword())){
				
				String token = jwtTokenUtil.generateToken(new User(
							jpa.getUsername(),
							credentials.getPassword(),
							jpa.isActive(),
							true,
							true,
							jpa.isNotLocked(),
							Arrays.asList(jpa.getUserHasRoles().split("#"))
									.stream()
									.map(a -> new SimpleGrantedAuthority(a))
									.collect(Collectors.toList())
						));
				
				// update last login datetime
				jpa.setLastLogin(LocalDateTime.now());
				userSecurityRepository.save(jpa);
				
				return token;
			}
			
			throw new MyRestPreconditionsException("Login error","Credentials do not match.");
		}
		
		throw new MyRestPreconditionsException("Login error","No user for those credentials.");
	}
	
	@Override
	public String refreshToken(final String token) throws MyRestPreconditionsException {
		return jwtTokenUtil.refreshToken(token);
	}
}