package com.toptal.expenses.utils;

import org.apache.commons.lang3.StringUtils;

import spark.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.toptal.expenses.mvc.ParamType;

/**
 * Helper to validate the request parameters
 * 
 * @author jorge.santoro
 */
public class RequestHelper {

	/**
	 * Gets from the request a not null value of param of type paramType
	 */
	public static <T> T getParam(Request request, String param, ParamType<T> paramType) {
		String value = request.params(param);
		value = validate(value, param, true);
		return toType(value, param, paramType);
	}

	/**
	 * Gets from the request a not null string with length at most maxlength of
	 * param
	 */
	public static String getStringParam(Request request, String param, int maxLength) {
		String value = getParam(request, param, ParamType.STRING);
		if (StringUtils.length(value) > maxLength) {
			throw new IllegalArgumentException("Param '" + param + "' is bigger than " + maxLength + " chars");
		}
		return value;
	}

	private static String validate(String value, String param, boolean required) {
		if (required && StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Param '" + param + "' is required");
		}
		return required ? value.trim() : value;
	}

	private static String validate(JsonNode value, String param) {
		if (value == null) {
			throw new IllegalArgumentException("Param '" + param + "' is required");
		}
		String textValue = value.asText();
		validate(textValue, param, true);
		return textValue.trim();
	}

	/**
	 * Gets from the json node a not null value of param of type paramType
	 */
	public static <T> T getParam(JsonNode json, String param, ParamType<T> paramType) {
		JsonNode jsonNode = json.get(param);
		String value = validate(jsonNode, param);
		T response = toType(value, param, paramType);
		return response;
	}

	/**
	 * Gets from the json node a not null string with length at most maxlength
	 * of param
	 */
	public static String getStringParam(JsonNode json, String param, int maxLength) {
		String response = getParam(json, param, ParamType.STRING);
		if (StringUtils.length(response) > maxLength) {
			throw new IllegalArgumentException("Param '" + param + "' is bigger than " + maxLength + " chars");
		}
		return response;
	}

	private static <T> T toType(String value, String param, ParamType<T> paramType) {
		if (value == null) {
			return null;
		}
		if (!paramType.isValid(value)) {
			throw new IllegalArgumentException("Invalid '" + value + "' value for '" + param + "' parameter");
		}
		return paramType.transform(value);
	}

	/**
	 * Gets from the request the query param of type paramType, could be null
	 */
	public static <T> T getQueryParam(Request request, String param, ParamType<T> paramType) {
		String value = request.queryParams(param);
		value = validate(value, param, false);
		T response = toType(value, param, paramType);
		return response;
	}
}
