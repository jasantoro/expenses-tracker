package com.toptal.expenses.mvc.routes.builder;

import spark.ResponseTransformer;
import spark.ResponseTransformerRouteImpl;
import spark.Route;
import spark.RouteImpl;
import spark.SparkBase;
import spark.route.HttpMethod;

import com.toptal.expenses.serialization.JsonFactory;

/**
 * Route builder
 * 
 * @author jorge.santoro
 */
public class RouteBuilder extends SparkBase {

	public static RouteBuilder get(String service) {
		return new RouteBuilder(HttpMethod.get, service);
	}

	public static RouteBuilder delete(String service) {
		return new RouteBuilder(HttpMethod.delete, service);
	}

	public static RouteBuilder post(String service) {
		return new RouteBuilder(HttpMethod.post, service);
	}

	public static RouteBuilder patch(String service) {
		return new RouteBuilder(HttpMethod.patch, service);
	}

	public static RouteBuilder put(String service) {
		return new RouteBuilder(HttpMethod.put, service);
	}

	private HttpMethod method;
	private String service;
	private Route route;
	private ResponseTransformer transformer;

	public RouteBuilder(HttpMethod method, String service) {
		this.method = method;
		this.service = service;
	}

	public RouteBuilder withRoute(Route route) {
		this.route = route;
		return this;
	}

	public RouteBuilder asJson() {
		this.transformer = JsonResponseTransformer.create();
		return this;
	}

	static class JsonResponseTransformer implements ResponseTransformer {

		public static JsonResponseTransformer create() {
			return new JsonResponseTransformer();
		}

		private JsonFactory jsonFactory;

		private JsonResponseTransformer() {
			this.jsonFactory = JsonFactory.instance;
		}

		@Override
		public String render(Object model) throws Exception {
			return this.jsonFactory.toJson(model);
		}
	}

	public void register() {
		RouteImpl routeImpl = this.transformer != null ? ResponseTransformerRouteImpl.create(this.service, this.route,
				this.transformer) : wrap(this.service, this.route);
		addRoute(this.method.name(), routeImpl);
	}
}
