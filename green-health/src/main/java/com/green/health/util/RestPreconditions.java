package com.green.health.util;

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

}