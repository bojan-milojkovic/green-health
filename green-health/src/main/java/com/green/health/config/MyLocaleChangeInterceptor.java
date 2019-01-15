package com.green.health.config;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public class MyLocaleChangeInterceptor extends LocaleChangeInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null){
	            for (Cookie cookie : cookies){
	                if ("localeInfo".equals(cookie.getName())){
	                	String value = cookie.getValue();
	                	if(value.matches("[a-z]{2}[_-][A-Z]{2}.*")){
		                    LocaleContextHolder.setLocale(
		                    		new Locale.Builder()
		    						.setLanguage(cookie.getValue().split("[_-]")[0])
		    						.setRegion(cookie.getValue().split("[_-]")[1].split("[^A-Z]")[0])
		    						.build());
	                	}
	                }
	            }
	        }
	        super.preHandle(request, response, handler);
	    } catch (IllegalArgumentException e) {
	    	System.err.println("IllegalArgumentException : "+ e.getMessage());
	    } catch (ServletException e) {
	    	System.err.println("ServletException : "+ e.getMessage());
		}
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
	    return true;
	}
}
