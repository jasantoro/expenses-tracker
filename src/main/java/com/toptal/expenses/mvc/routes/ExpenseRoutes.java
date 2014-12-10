package com.toptal.expenses.mvc.routes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.toptal.expenses.model.Expense;
import com.toptal.expenses.mvc.ParamType;
import com.toptal.expenses.mvc.routes.builder.RouteBuilder;
import com.toptal.expenses.serialization.JsonFactory;
import com.toptal.expenses.service.ExpenseService;
import com.toptal.expenses.utils.RequestHelper;

/**
 * Routes to handle the expense resource
 * 
 * @author jorge.santoro
 */
public class ExpenseRoutes implements RoutesConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionRoutes.class);
	private ExpenseService expenseService;

	@Inject
	public ExpenseRoutes(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	public void registerRoutes() {
		RouteBuilder.post(EXPENSES).withRoute((request, response) -> {
			return this.handleAuthenticated(request, user -> {
				String body = request.body();
				if (StringUtils.isBlank(body)) {
					throw new IllegalArgumentException("Empty body");
				}
				JsonNode json = JsonFactory.instance.readTree(body);
				DateTime date = RequestHelper.getParam(json, "date", ParamType.DATE_TIME);
				String description = RequestHelper.getStringParam(json, "description", 140);
				BigDecimal amount = RequestHelper.getParam(json, "amount", ParamType.BIG_DECIMAL);
				String comment = RequestHelper.getStringParam(json, "comment", 1000);
				return this.expenseService.create(date, description, amount, comment, user);
			});
		}).asJson().register();
		RouteBuilder.put(EXPENSE).withRoute((request, response) -> {
			return this.handleAuthenticated(request, user -> {
				Long id = RequestHelper.getParam(request, "id", ParamType.LONG);
				String body = request.body();
				if (StringUtils.isBlank(body)) {
					throw new IllegalArgumentException("Empty body");
				}
				JsonNode json = JsonFactory.instance.readTree(body);
				DateTime date = RequestHelper.getParam(json, "date", ParamType.DATE_TIME);
				String description = RequestHelper.getStringParam(json, "description", 140);
				BigDecimal amount = RequestHelper.getParam(json, "amount", ParamType.BIG_DECIMAL);
				String comment = RequestHelper.getStringParam(json, "comment", 1000);
				return this.expenseService.update(id, date, description, amount, comment, user);
			});
		}).asJson().register();
		RouteBuilder.delete(EXPENSE).withRoute((request, response) -> {
			return this.handleAuthenticated(request, user -> {
				Long id = RequestHelper.getParam(request, "id", ParamType.LONG);
				this.expenseService.delete(id, user);
				return ImmutableMap.of("id", id, "deleted", true);
			});
		}).asJson().register();
		RouteBuilder.get(EXPENSE).withRoute((request, response) -> {
			return this.handleAuthenticated(request, user -> {
				Long id = RequestHelper.getParam(request, "id", ParamType.LONG);
				return this.expenseService.get(id, user);
			});
		}).asJson().register();
		RouteBuilder.get(EXPENSES).withRoute((request, response) -> {
			return this.handleAuthenticated(request, user -> {
				Map<String, Object> filters = this.createFilterMap(request);
				if ("week".equals(request.queryParams("group"))) {
					return this.expenseService.getAllByWeek(user, filters);
				}
				Collection<Expense> expenses = this.expenseService.getAll(user, filters);
				LOGGER.info("There were {} expenses", expenses.size());
				return expenses;
			});
		}).asJson().register();
	}

	private Map<String, Object> createFilterMap(Request request) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			put(result, request, "date", ParamType.DATE_TIME);
		} catch (Exception exception) {
			put(result, request, "date", ParamType.DATE);
		}
		put(result, request, "description", ParamType.STRING);
		put(result, request, "amount", ParamType.BIG_DECIMAL);
		put(result, request, "comment", ParamType.STRING);
		put(result, request, "week", ParamType.STRING);
		put(result, request, "orderBy", ParamType.EXPENSE_PROPERTY);
		put(result, request, "asc", ParamType.BOOLEAN);
		return result;
	}

	private <T> void put(Map<String, Object> result, Request request, String queryParam, ParamType<T> paramType) {
		T value = RequestHelper.getQueryParam(request, queryParam, paramType);
		if (value != null) {
			result.put(queryParam, value);
		}
	}
}
