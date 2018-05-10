package com.green.health.util.exceptions;

public class MyValueAlreadyTakenException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private String duplicateValue;

	public MyValueAlreadyTakenException(String duplicateValue) {
		super();
		this.duplicateValue = duplicateValue;
	}

	public String getDuplicateValue() {
		return duplicateValue;
	}

	public void setDuplicateValue(String duplicateValue) {
		this.duplicateValue = duplicateValue;
	}
}