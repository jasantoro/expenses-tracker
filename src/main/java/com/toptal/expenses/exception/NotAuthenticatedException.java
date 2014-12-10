package com.toptal.expenses.exception;

@SuppressWarnings("serial")
public class NotAuthenticatedException extends RuntimeException {

	public NotAuthenticatedException(String message) {
		super(message);
	}
}
