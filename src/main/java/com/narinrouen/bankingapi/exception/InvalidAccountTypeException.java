package com.narinrouen.bankingapi.exception;

public class InvalidAccountTypeException extends RuntimeException {
	public InvalidAccountTypeException(String message) {
		super(message);
	}

}
