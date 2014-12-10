package com.toptal.expenses.mvc.routes;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.toptal.expenses.model.LoggedUser;
import com.toptal.expenses.mvc.ParamType;
import com.toptal.expenses.mvc.routes.builder.RouteBuilder;
import com.toptal.expenses.serialization.JsonFactory;
import com.toptal.expenses.service.UserService;
import com.toptal.expenses.utils.RequestHelper;

/**
 * Routes for user authentication and sign-up
 * 
 * @author jorge.santoro
 */
public class AuthenticationRoutes implements RoutesConstants {

	private UserService userService;

	@Inject
	public AuthenticationRoutes(UserService userService) {
		this.userService = userService;
	}

	public void registerRoutes() {
		RouteBuilder.post(SIGN_IN).withRoute((request, response) -> {
			String body = request.body();
			JsonNode json = JsonFactory.instance.readTree(body);
			String username = RequestHelper.getStringParam(json, "username", 100);
			String password = RequestHelper.getParam(json, "password", ParamType.STRING);
			LoggedUser loggedUser = this.userService.authenticate(username, password);
			return loggedUser;
		}).asJson().register();
		RouteBuilder.post(SIGN_UP).withRoute((request, response) -> {
			String body = request.body();
			JsonNode json = JsonFactory.instance.readTree(body);
			String username = RequestHelper.getStringParam(json, "username", 100);
			String password = RequestHelper.getParam(json, "password", ParamType.STRING);
			String verifyPassword = RequestHelper.getParam(json, "verify_password", ParamType.STRING);
			String firstName = RequestHelper.getStringParam(json, "first_name", 50);
			String lastName = RequestHelper.getStringParam(json, "last_name", 50);
			return this.userService.create(username, password, verifyPassword, firstName, lastName);
		}).asJson().register();
	}
}
