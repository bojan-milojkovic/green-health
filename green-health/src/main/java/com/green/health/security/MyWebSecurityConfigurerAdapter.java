package com.green.health.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import com.green.health.security.service.BokiAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	/*@Autowired
	private BokiAuthenticationProvider bokiAuthenticationProvider;*/
	
	@Autowired
	private CredentialsFilter credentialsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// request handling
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/users").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/users/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/users").permitAll() // everyone should access register-api
			.antMatchers(HttpMethod.GET, "/users/prfimg/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/users/prfimg").hasRole("USER")
			.antMatchers(HttpMethod.PATCH, "/users/*").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/users/*").hasRole("USER")
			
			.antMatchers(HttpMethod.GET, "/herb").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/herb/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/herb/thumbnail/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/herb/image/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/herb").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/herb/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.POST, "/herb/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.DELETE, "/herb/*").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.GET, "/illness").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/illness/*").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/illness").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/illness/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.DELETE, "/illness/*").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.POST, "/rating/link").hasRole("HERBALIST")
			.antMatchers(HttpMethod.POST, "/rating/store").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/rating/product").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/rating/*").hasRole("USER")
			
			.antMatchers(HttpMethod.GET, "/store/my").hasRole("HERBALIST")
			.antMatchers(HttpMethod.GET, "/store/*").hasRole("USER")
			.antMatchers(HttpMethod.PUT, "/store/bypar").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/store").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/store/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.DELETE, "/store/*").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.GET, "/store/pr/*").hasRole("USER")
			.antMatchers(HttpMethod.PUT, "/store/pr").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/store/pr").hasRole("HERBALIST")
			.antMatchers(HttpMethod.POST, "/store/pr/*").hasRole("HERBALIST")
			.antMatchers(HttpMethod.PATCH, "/store/pr").hasRole("HERBALIST")
			
			.antMatchers(HttpMethod.POST, "/roles").permitAll() // everyone should access login-api
			;

		// disable csrf
		http.csrf().disable();
		
		// app session is stateless
		http.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);		
		
		http.addFilterBefore(credentialsFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	/*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false)
			.authenticationProvider(bokiAuthenticationProvider);
    }*/
}