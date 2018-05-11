package com.green.health.util.exceptions;

import java.util.ArrayList;
import java.util.List;

public class MyRestPreconditionsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String description;
	private String details;
	private List<String> errors;
	
	public MyRestPreconditionsException(String description, String details) {
		super();
		this.description = description;
		this.details = details;
		errors = new ArrayList<String>();
	}
	
	public String getDescription() {
		return description;
	}
	public String getDetails() {
		return details;
	}
	public List<String> getErrors() {
		return errors;
	}
}