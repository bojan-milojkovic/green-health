package com.green.health.util;

import com.green.health.util.exceptions.MyResourceDoesNotExistException;
import com.green.health.util.exceptions.MyValueAlreadyTakenException;

public class RestPreconditions {
	
	public static void checkEntityDoesNotExist(Object entity) throws MyResourceDoesNotExistException{
		if(entity == null) {
			throw new MyResourceDoesNotExistException("The requested resource does not exist.");
		}
	}
			
	public static void checkEntityDoesNotExist(Object entity, String message) throws MyResourceDoesNotExistException{
		if(entity == null) {
			throw new MyResourceDoesNotExistException(message);
		}
	}
	
	public static void checkSuchEntityAlreadyExists(Object entity, String message) throws MyValueAlreadyTakenException{
		if(entity!=null) {
			throw new MyValueAlreadyTakenException(message);
		}
	}

}
