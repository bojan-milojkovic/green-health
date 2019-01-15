package com.green.health.config;

import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public class MyLocaleChangeInterceptor extends LocaleChangeInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
	        if (request.getCookies() != null){
	        	LocaleContextHolder.setLocale((new MyCookieLocaleResolver()).resolveLocale(request));
	        } else {
	        	super.preHandle(request, response, handler);
	        }
	    } catch (IllegalArgumentException e) {
	    	System.err.println("IllegalArgumentException : "+ e.getMessage());
	    	LocaleContextHolder.setLocale(Locale.forLanguageTag("en-US"));
	    } catch (ServletException e) {
	    	System.err.println("ServletException : "+ e.getMessage());
	    	LocaleContextHolder.setLocale(Locale.forLanguageTag("en-US"));
		}
		
	    return true;
	}
}
