package com.narinrouen.bankingapi.exception;

public class ResourceNotFoundException extends RuntimeException {

	private final String resourceName;
	private final String fieldName;
	private final Object fieldValue;

	// Constructor to create a new ResourceNotFoundException with details about the
	// missing resource
	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	// Constructor to create a new ResourceNotFoundException with a custom message
	public ResourceNotFoundException(String message) {
		super(message);
		this.resourceName = null;
		this.fieldName = null;
		this.fieldValue = null;
	}

}
