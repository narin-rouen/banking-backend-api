package com.narinrouen.bankingapi.exception;

public class SameAccountTransferException extends RuntimeException {
	public SameAccountTransferException(String message) {
		super(message);
	}
}
