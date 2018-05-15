package com.green.health.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// disable csrf
		http.csrf().disable();
		
		// app session is stateless
		http.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// request handling
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/gh/users").permitAll()
			.antMatchers(HttpMethod.GET, "/gh/users/*").permitAll()
			.antMatchers(HttpMethod.POST, "/gh/users").permitAll()
			.antMatchers(HttpMethod.PATCH, "/gh/users/*").permitAll()
			.antMatchers(HttpMethod.DELETE, "/gh/users/*").permitAll()
			;
		
	}
}