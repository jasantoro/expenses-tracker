package com.toptal.expenses.mvc.routes;

import io.jsonwebtoken.Claims;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import spark.Request;

import com.toptal.expenses.exception.InvalidAuthenticationException;
import com.toptal.expenses.exception.NotAuthenticatedException;
import com.toptal.expenses.service.UserService;

/**
 * The different route constants
 * 
 * @author jorge.santoro
 */
public interface RoutesConstants {

	public static final String CHARSET = "charset=UTF-8";
	public static final String HTML = "text/html; " + CHARSET;
	public static final String JSON = "application/json; " + CHARSET;
	public static final String PLAIN_TEXT = "text/plain; " + CHARSET;

	public static final String DEBUG = "/debug";

	public static final String SIGN_IN = "/signin";
	public static final String SIGN_UP = "/signup";

	public static final String API_DOC = "/api-docs";
	public static final String GROUP_DOC = "/api-docs/:id";

	public static final String EXPENSES = "/expenses";
	public static final String EXPENSE = EXPENSES + "/:id";

	public static final String API_DOCUMENTATION = "/api-docs";
	public static final String GROUP_DOCUMENTATION = "/api-docs/:group_name";

	default String getUser(Request request) {
		String token = request.headers("token");
		if (StringUtils.isEmpty(token)) {
			throw new NotAuthenticatedException("There is not token to get the logged user");
		}
		try {
			Claims claims = UserService.JWT_PARSER.parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (Exception exception) {
			throw new InvalidAuthenticationException(exception);
		}
	}

	default <T> T handleAuthenticated(Request request, Function<String, T> function) {
		String user = getUser(request);
		T result = function.apply(user);
		return result;
	}
}
