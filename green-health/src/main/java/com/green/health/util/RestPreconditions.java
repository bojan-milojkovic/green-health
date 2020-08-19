package com.green.health.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.green.health.util.exceptions.MyRestPreconditionsException;

public class RestPreconditions {
	
	public static void checkSuchEntityAlreadyExists(Object entity, String message) throws MyRestPreconditionsException{
		if(entity!=null) {
			throw new MyRestPreconditionsException("The value you are tryiong to enter is already being used", message);
		}
	}
	
	public static void assertTrue(boolean condition, String title, String message) throws MyRestPreconditionsException{
		if(!condition) {
			throw new MyRestPreconditionsException(title, message);
		}
	}
	
	public static boolean checkString(final String s){
		return !(s==null || s.trim().isEmpty());
	}
	
	public static boolean checkStringMatches(final String s, final String regExp){
		return checkString(s) && s.matches(regExp);
	}
	
	public static <T> void checkNull(T object, String description, String details)throws MyRestPreconditionsException {
		if(object!=null){
			throw new MyRestPreconditionsException(description, details);
		}
	}
	
	public static <T> T checkNotNull(T object, String description, String details)throws MyRestPreconditionsException {
		if(object!=null){
			return object;
		}
		throw new MyRestPreconditionsException(description, details);
	}
	
	public static boolean checkLocaleIsEnglish() {
		return LocaleContextHolder.getLocale()==null || Locale.ENGLISH.equals(LocaleContextHolder.getLocale());
	}
	
	public static String assertLocaleInString() {
		return "We assert your language as " + 
				(LocaleContextHolder.getLocale()==null ? "English" :
					LocaleContextHolder.getLocale().getDisplayLanguage()) +" ; ";
	}
}