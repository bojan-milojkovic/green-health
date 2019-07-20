package com.green.health.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableScheduling
public class EmailConfiguration {
	
	@Bean
	public JavaMailSender javaMailSender(Environment env) throws GeneralSecurityException {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(env.getProperty("mail.host"));
	    mailSender.setPort(Integer.parseInt((env.getProperty("mail.port"))));
	    mailSender.setProtocol(env.getProperty("mail.protocol"));
	    mailSender.setUsername("bojan.milojkovic.83");
	    mailSender.setPassword("bmeugpnkrsqvzfbp");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	    
	    mailSender.setJavaMailProperties(props);
	     
	    return mailSender;
	}
	
	@Bean
	public VelocityEngine velocityEngine() throws VelocityException, IOException {
	    VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
	    Properties props = new Properties();
	    props.put("resource.loader", "class");
	    props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	    factory.setVelocityProperties(props);

	    return factory.createVelocityEngine();
	}
	
	@Bean
	public String hostName(Environment env){
		try{
			String host = InetAddress.getLocalHost().getHostName();
			return host.contains("HAL") ? env.getProperty("server.host") : host;
		} catch (UnknownHostException e) {
			return env.getProperty("server.host");
		}
	}
}