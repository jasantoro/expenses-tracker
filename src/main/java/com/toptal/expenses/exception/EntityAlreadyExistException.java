package com.toptal.expenses.exception;

@SuppressWarnings("serial")
public class EntityAlreadyExistException extends IllegalArgumentException {

	public EntityAlreadyExistException(String message) {
		super(message);
	}

	public EntityAlreadyExistException(String entity, Object name) {
		this("Already exist " + entity + " with '" + name + "'");
	}
}
