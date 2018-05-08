package com.green.health.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * An implementation of {@link WebMvcRegistrations} with empty methods allowing
 * sub-classes to override only the methods they're interested in.
 *
 * @author Brian Clozel
 * @since 1.4.0
 */
public class WebMvcRegistrationsAdapter implements WebMvcRegistrations {

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return null;
	}

	@Override
	public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
		return null;
	}

	@Override
	public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
		return null;
	}

}