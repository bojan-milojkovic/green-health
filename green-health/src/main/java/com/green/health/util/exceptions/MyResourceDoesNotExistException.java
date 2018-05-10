package com.green.health.util.exceptions;

public class MyResourceDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String missingEntityMessage;

	public MyResourceDoesNotExistException(String missingEntityMessage) {
		super();
		this.missingEntityMessage = missingEntityMessage;
	}

	public String getMissingEntityMessage() {
		return missingEntityMessage;
	}

	public void setMissingEntityMessage(String missingEntityMessage) {
		this.missingEntityMessage = missingEntityMessage;
	}
}
