package com.green.health.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.setConfigLocations(new String[]{"com.green.health.config", "com.green.health.security"});
		
		ServletRegistration.Dynamic apiSR = sc.addServlet("api-dispatcher", new DispatcherServlet(rootContext));
		apiSR.setLoadOnStartup(1);
		apiSR.addMapping("/");
	}
}