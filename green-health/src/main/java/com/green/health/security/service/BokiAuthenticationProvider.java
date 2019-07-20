package com.green.health.security.service;

import java.util.List;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.UsernameNotFoundException;

//@Component
public class BokiAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName();
		
		if(RestPreconditions.checkString(username)){
			@SuppressWarnings("unchecked")
			List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

			return new UsernamePasswordAuthenticationToken(
					username,
					null,
					authorities);
		}
		
		throw new UsernameNotFoundException("You did not provide a valid username.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}