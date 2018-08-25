package com.green.health.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.green.health.security.service.BokiAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BokiAuthenticationProvider bokiAuthenticationProvider;
	
	@Autowired
	private CredentialsFilter credentialsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// request handling
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/gh/users").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/users/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/gh/users").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/users/prfimg/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/gh/users/prfimg").hasRole("USER")
			.antMatchers(HttpMethod.PATCH, "/gh/users/*").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/gh/users/*").hasRole("USER")
			
			.antMatchers(HttpMethod.GET, "/gh/herb").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/herb/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/herb/thumbnail/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/herb/image/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/gh/herb").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/gh/herb/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.POST, "/gh/herb/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.DELETE, "/gh/herb/*").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.GET, "/gh/illness").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/gh/illness/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/gh/illness").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/gh/illness/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.DELETE, "/gh/illness/*").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.POST, "/gh/roles").permitAll()
			;

		// disable csrf
		http.csrf().disable();
		
		// app session is stateless
		http.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);		
		
		http.addFilterBefore(credentialsFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false)
			.authenticationProvider(bokiAuthenticationProvider);
    }
}