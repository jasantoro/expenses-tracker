package com.toptal.expenses.mvc.routes;

import static spark.Spark.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toptal.expenses.exception.InvalidAuthenticationException;
import com.toptal.expenses.exception.NotAuthenticatedException;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.ExceptionView;
import com.toptal.expenses.serialization.JsonFactory;

/**
 * Routes to handle the different exceptions
 * 
 * @author jorge.santoro
 */
public class ExceptionRoutes implements RoutesConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionRoutes.class);

	public void registerRoutes() {
		exception(NotFoundException.class, (exception, request, response) -> {
			LOGGER.info("Entity not found", exception);
			response.status(404);
			ExceptionView exceptionView = new ExceptionView(404, "Entity not found");
			exceptionView.addCause(exception.getMessage());
			response.body(JsonFactory.instance.toJson(exceptionView));
		});

		exception(InvalidAuthenticationException.class, (exception, request, response) -> {
			LOGGER.info("Not valid credentials", exception);
			response.status(400);
			ExceptionView exceptionView = new ExceptionView(400, "Not valid credentials");
			response.body(JsonFactory.instance.toJson(exceptionView));
		});

		exception(NotAuthenticatedException.class, (exception, request, response) -> {
			LOGGER.info("Need authentication", exception);
			response.status(401);
			ExceptionView exceptionView = new ExceptionView(401, "Need valid authentication");
			response.body(JsonFactory.instance.toJson(exceptionView));
		});

		exception(IllegalArgumentException.class, (exception, request, response) -> {
			LOGGER.info("Invalid value", exception);
			response.status(400);
			ExceptionView exceptionView = new ExceptionView(400, exception.getMessage());
			response.body(JsonFactory.instance.toJson(exceptionView));
		});

		exception(RuntimeException.class, (exception, request, response) -> {
			LOGGER.error("Throwing error", exception);
			response.status(500);
			ExceptionView exceptionView = new ExceptionView(500, "Internal Error");
			exceptionView.addCause(exception.getMessage());
			response.body(JsonFactory.instance.toJson(exceptionView));
		});
	}

}
