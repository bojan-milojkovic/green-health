package com.green.health.security.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.UsernameNotFoundException;

@Component
public class BokiAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserSecurityRepository userSecurityRepository;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName();
		
		System.err.println("\n\t\t\t3) check username ");
		
		if(RestPreconditions.checkString(username)){
			UserSecurityJPA jpa = userSecurityRepository.findByUsername(username);
			
			if(jpa!=null){
				String password = auth.getCredentials().toString();
				System.err.println("\n\t\t\t4) check password ");
				if(BCrypt.checkpw(password, jpa.getPassword())){

					@SuppressWarnings("unchecked")
					List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

					System.err.println("\n\t\t\t5) return authenticated ");
					return new UsernamePasswordAuthenticationToken(
							jpa.getUsername(),
							null,
							authorities);
				}
				throw new UsernameNotFoundException("Passwords is missing or invalid.");
			}
			throw new UsernameNotFoundException("There is no user with username = "+username);
		}
		
		throw new UsernameNotFoundException("You did not provide a username.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
