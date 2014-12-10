package com.toptal.expenses.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.DateTime;

import com.google.inject.persist.Transactional;
import com.toptal.expenses.exception.EntityAlreadyExistException;
import com.toptal.expenses.exception.InvalidAuthenticationException;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.LoggedUser;
import com.toptal.expenses.model.User;
import com.toptal.expenses.persistence.UserPersistenceService;

/**
 * Services to handle the user
 * 
 * @author jorge.santoro
 */
@Singleton
public class UserService {

	private static final String SIGNING_KEY = "3xp3ns3s-@pp-f0r-t0pt@l";
	private static final int TOKEN_EXPIRATION = 2;
	public static final JwtParser JWT_PARSER;

	static {
		JWT_PARSER = Jwts.parser().setSigningKey(SIGNING_KEY);
	}

	private UserPersistenceService userPersistenceService;

	@Inject
	public UserService(UserPersistenceService userPersistenceService) {
		this.userPersistenceService = userPersistenceService;
	}

	/**
	 * Creates a new user
	 */
	@Transactional
	public User create(String username, String password, String verifyPassword, String firstName, String lastName) {
		if (!password.equals(verifyPassword)) {
			throw new IllegalArgumentException("Password and verifyPassword aren't equals");
		}
		if (this.userPersistenceService.exists(username)) {
			throw new EntityAlreadyExistException("user", username);
		}
		User user = new User(username, password, firstName, lastName);
		this.userPersistenceService.create(user);
		return user;
	}

	/**
	 * Authenticate a user
	 */
	public LoggedUser authenticate(String username, String password) {
		try {
			User user = this.getUser(username);
			user.authenticate(password);
			String token = Jwts.builder().setExpiration(new DateTime().plusDays(TOKEN_EXPIRATION).toDate())
					.setSubject(username).signWith(HS256, SIGNING_KEY).compact();
			return new LoggedUser(user, token);
		} catch (NotFoundException exception) {
			throw new InvalidAuthenticationException(exception);
		}
	}

	/**
	 * Gets a user
	 */
	public User getUser(String username) {
		return this.userPersistenceService.getUser(username);
	}
}
