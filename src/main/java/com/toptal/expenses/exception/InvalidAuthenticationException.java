package com.toptal.expenses.exception;

@SuppressWarnings("serial")
public class InvalidAuthenticationException extends RuntimeException {

	public InvalidAuthenticationException() {
	}

	public InvalidAuthenticationException(Exception exception) {
		super(exception);
	}

}
