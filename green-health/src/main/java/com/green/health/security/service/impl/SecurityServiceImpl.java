package com.green.health.security.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.green.health.security.JwtTokenUtil;
import com.green.health.security.dto.CredentialsDTO;
import com.green.health.security.entities.RoleJPA;
import com.green.health.security.entities.UserHasRolesJPA;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.security.service.SecurityService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserSecurityRepository userSecurityRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public String generateTokenForUser(CredentialsDTO credentials){
		
		// check username :
		UserSecurityJPA jpa = userSecurityRepository.findByUsername(credentials.getUsername());
		if(jpa != null){
			// check password - BCrypt.checkpw(password_plaintext, stored_hash)
			if(BCrypt.checkpw(credentials.getPassword(), jpa.getPassword())){
				
				List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
				
				for(UserHasRolesJPA userRoles : jpa.getUserHasRolesJpa()){
					RoleJPA role = userRoles.getRoleJpa();
					
					authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
					/*for(AuthorityJPA authority : role.getAuthoritiesJpa()){
						authorities.add(new SimpleGrantedAuthority(authority.getAuthName()));
					}*/
				}
				
				String token = jwtTokenUtil.generateToken(new User(
							jpa.getUsername(),
							credentials.getPassword(),
							jpa.isActive(),
							true,
							true,
							jpa.isNotLocked(),
							authorities
						));
				
				// update last login datetime
				jpa.setLastLogin(LocalDateTime.now());
				userSecurityRepository.save(jpa);
				
				return token;
			}
			
			return "Error - passwords do not match.";
		}
		
		return "Error - no user with those credentials.";
	}
}