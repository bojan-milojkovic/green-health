package com.green.health.config;

import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
public class LocaleConfig implements WebMvcConfigurer {
	
/*	@Bean("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
      	messageSource.setBasename("classpath:locale");
      	messageSource.setDefaultEncoding("UTF-8");
      	messageSource.setUseCodeAsDefaultMessage(true);
      	messageSource.setFallbackToSystemLocale(false);
      	return messageSource;
	}*/
	
	//https://stackoverflow.com/questions/27073953/when-i-used-cookielocaleresolver-i-can-set-invalid-cookie-to-crash-spring-web-a
	//https://stackoverflow.com/questions/24786962/define-default-locale-and-treat-exceptions-for-spring-locale-interceptor
	
	@Bean
	public LocaleResolver localeResolver() {
		MyCookieLocaleResolver localeResolver = new MyCookieLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		localeResolver.setCookieName("localeInfo");
		localeResolver.setCookieMaxAge(24*60*60);
	    return localeResolver;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("lang");
	    registry.addInterceptor(localeChangeInterceptor);
	}
}