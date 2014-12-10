package com.toptal.expenses.exception;

@SuppressWarnings("serial")
public class NotFoundException
    extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entity, Object name) {
        this("No " + entity + " found for id '" + name + "'");
    }

    public NotFoundException(String entity, Object name, String message) {
        this("No " + entity + " found for id '" + name + "' - " + message);
    }

    public NotFoundException(String entity, Object name, Throwable throwable) {
        super("No " + entity + " found for id '" + name + "'", throwable);
    }
}
