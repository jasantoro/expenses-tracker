package com.toptal.expenses.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A logged user in the application
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
public class LoggedUser implements Serializable {

	private User user;
	private String token;

	@SuppressWarnings("unused")
	private LoggedUser() {
	}

	public LoggedUser(User user, String token) {
		this.user = user;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
