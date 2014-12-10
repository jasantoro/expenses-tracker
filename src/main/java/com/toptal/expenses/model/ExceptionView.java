package com.toptal.expenses.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation model of an error
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
public class ExceptionView implements Serializable {

	private int code;
	private String message;
	private List<String> causes;

	@SuppressWarnings("unused")
	private ExceptionView() {
	}

	public ExceptionView(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public List<String> getCauses() {
		return this.causes;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setCauses(List<String> causes) {
		this.causes = causes;
	}

	public void addCause(String cause) {
		if (this.causes == null) {
			this.causes = new ArrayList<>();
		}
		this.causes.add(cause);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
