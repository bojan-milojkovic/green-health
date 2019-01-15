package com.green.health.config;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class MyCookieLocaleResolver extends CookieLocaleResolver {
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
	    try {
	    	if(request.getCookies() != null){
		    	for(Cookie c1 : request.getCookies()){
		    		if("localeInfo".equals(c1.getName())){
		    			String value = c1.getValue().split("(")[0]; // sr-RS(*)
		    			if (value.matches("[a-z]{2}-[A-Z]{2}(-.+)?")){ // sr-RS  and  no-NO-x-lvariant-NY
		    				return new Locale.Builder()
		    						.setLanguage(value.split("-")[0])
		    						.setRegion(value.split("-")[1])
		    						.build();
		    			}
		    			if(value.matches("[a-z]{2}-Latn-[A-Z]{2}")){// sr-Latn-RS
		    				return new Locale.Builder()
		    						.setLanguage(value.split("-")[0])
		    						.setRegion(value.split("-")[2])
		    						.build();
		    			}
		    		} 
		    	}
	    	}
	        return super.resolveLocale(request);
	    } catch (Exception exception) {
	        return Locale.forLanguageTag("en-US");
	    }
	}

	@Override
	public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
	    try {
	        return super.resolveLocaleContext(request);
	    } catch (Exception exception) {
	        return new LocaleContext() {
	            @Override
	            public Locale getLocale() {
	                return Locale.forLanguageTag("en-US");
	            }
	        };
	    }
	}

}
