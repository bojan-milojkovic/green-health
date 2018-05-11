package com.green.health.util.exceptions;

public class MyRestPreconditionsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String description;
	private String details;
	
	public MyRestPreconditionsException(String description, String details) {
		super();
		this.description = description;
		this.details = details;
	}
	
	public String getDescription() {
		return description;
	}
	public String getDetails() {
		return details;
	}
}
