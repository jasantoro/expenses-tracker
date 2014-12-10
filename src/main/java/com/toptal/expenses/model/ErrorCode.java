package com.toptal.expenses.model;

/**
 * The different error codes
 * 
 * @author jorge.santoro
 */
public enum ErrorCode {

	NOT_NULL("Missing %s field"), INVALID_VALUE("Invalid %s value");

	private String descriptionFormat;

	private ErrorCode(String descriptionFormat) {
		this.descriptionFormat = descriptionFormat;
	}

	public String getDescription(String param) {
		return String.format(descriptionFormat, param);
	}
}
