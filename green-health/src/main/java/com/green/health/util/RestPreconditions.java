package com.green.health.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.green.health.util.exceptions.MyRestPreconditionsException;

public class RestPreconditions {
	
	public static void checkSuchEntityAlreadyExists(Object entity, String message) throws MyRestPreconditionsException{
		if(entity!=null) {
			throw new MyRestPreconditionsException("The value you have entered is already being used by another user.", message);
		}
	}
	
	public static void assertTrue(boolean condition, String message) throws MyRestPreconditionsException {
		if(!condition) {
			throw new MyRestPreconditionsException("Assertion failed for this operation.", message);
		}
	}
	
	public static void assertTrue(boolean condition, String title, String message) throws MyRestPreconditionsException{
		if(!condition) {
			throw new MyRestPreconditionsException(title, message);
		}
	}
	
	public static boolean checkString(final String s){
		return s!=null && !s.trim().isEmpty();
	}
	
	public static boolean checkStringMatches(final String s, final String regExp){
		return s!=null && s.matches(regExp);
	}
	
	public static ResponseEntity<Resource> getImage(Resource resource, HttpServletRequest request){
		String contentType = null;
	    try {
	        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	    } catch (Exception e) {
	    	contentType = "application/octet-stream";
	    }
	    
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	}
}