package com.green.health.config;

import javax.servlet.HttpConstraintElement;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import javax.servlet.annotation.ServletSecurity;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.setConfigLocations(new String[]{"com.green.health.config", "com.green.health.security.*"});
		
		ServletRegistration.Dynamic apiSR = sc.addServlet("api-dispatcher", new DispatcherServlet(rootContext));
		apiSR.setLoadOnStartup(1);
		apiSR.addMapping("/");
		
		// cofigure multipart form requests :
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");        
		apiSR.setMultipartConfig(multipartConfigElement);
		
		HttpConstraintElement forceHttpsConstraint = new HttpConstraintElement(
				ServletSecurity.TransportGuarantee.CONFIDENTIAL);
		ServletSecurityElement servletSecurityElement = new ServletSecurityElement(forceHttpsConstraint);
		apiSR.setServletSecurity(servletSecurityElement);
	}
}