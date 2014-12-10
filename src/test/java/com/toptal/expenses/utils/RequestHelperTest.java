package com.toptal.expenses.utils;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import spark.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.toptal.expenses.mvc.ParamType;

public class RequestHelperTest {

	@DataProvider
	public static Object[][] getDataProvider() {
		return new Object[][] { { "string", ParamType.STRING, "value" }, { "string", ParamType.BIG_DECIMAL, null },
				{ "string", ParamType.DATE, null }, { "string", ParamType.DATE_TIME, null },
				{ "string", ParamType.LONG, null },

				{ "long", ParamType.LONG, new Long(123) }, { "long", ParamType.BIG_DECIMAL, new BigDecimal("123") },
				{ "long", ParamType.DATE, null }, { "long", ParamType.DATE_TIME, null },
				{ "long", ParamType.STRING, "123" },

				{ "bigDecimal", ParamType.LONG, null },
				{ "bigDecimal", ParamType.BIG_DECIMAL, new BigDecimal("123.45") },
				{ "bigDecimal", ParamType.DATE, null }, { "bigDecimal", ParamType.DATE_TIME, null },
				{ "bigDecimal", ParamType.STRING, "123.45" },

				{ "dateTime", ParamType.LONG, null }, { "dateTime", ParamType.BIG_DECIMAL, null },
				{ "dateTime", ParamType.DATE, null },
				{ "dateTime", ParamType.DATE_TIME, DateTime.parse("2014-11-29T12:34:35") },
				{ "dateTime", ParamType.STRING, "2014-11-29T12:34:35" },

				{ "date", ParamType.LONG, null }, { "date", ParamType.BIG_DECIMAL, null },
				{ "date", ParamType.DATE, DateTime.parse("2014-11-29") }, { "date", ParamType.DATE_TIME, null },
				{ "date", ParamType.STRING, "2014-11-29" } };
	}

	@Test(dataProvider = "getDataProvider")
	public <T> void getParam(String param, ParamType<T> paramType, T expected) {
		Request request = this.createRequest();
		try {
			T actual = RequestHelper.getParam(request, param, paramType);
			assertEquals(actual, expected);
		} catch (IllegalArgumentException exception) {
			if (expected != null) {
				fail("expected value " + expected, exception);
			}
		}
	}

	@DataProvider
	public static Object[][] getStringParam_dataProvider() {
		return new Object[][] { { "string", 10, "value" }, { "string", 5, "value" }, { "string", 4, null } };
	}

	@Test(dataProvider = "getStringParam_dataProvider")
	public void getStringParam(String param, int maxLength, String expected) {
		Request request = this.createRequest();
		try {
			String actual = RequestHelper.getStringParam(request, param, maxLength);
			assertEquals(actual, expected);
		} catch (IllegalArgumentException exception) {
			if (expected != null) {
				fail("expected value " + expected, exception);
			}
		}
	}

	@Test(dataProvider = "getDataProvider")
	public <T> void getQueryParam(String param, ParamType<T> paramType, T expected) {
		Request request = this.createRequest();
		try {
			T actual = RequestHelper.getQueryParam(request, param, paramType);
			assertEquals(actual, expected);
		} catch (IllegalArgumentException exception) {
			if (expected != null) {
				fail("expected value " + expected, exception);
			}
		}
	}

	@Test(dataProvider = "getDataProvider")
	public <T> void getParamFromJson(String param, ParamType<T> paramType, T expected) {
		JsonNode json = this.createJson();
		try {
			T actual = RequestHelper.getParam(json, param, paramType);
			assertEquals(actual, expected);
		} catch (IllegalArgumentException exception) {
			if (expected != null) {
				fail("expected value " + expected, exception);
			}
		}
	}

	@Test(dataProvider = "getStringParam_dataProvider")
	public void getStringParamFromJson(String param, int maxLength, String expected) {
		JsonNode json = this.createJson();
		try {
			String actual = RequestHelper.getStringParam(json, param, maxLength);
			assertEquals(actual, expected);
		} catch (IllegalArgumentException exception) {
			if (expected != null) {
				fail("expected value " + expected, exception);
			}
		}
	}

	private Request createRequest() {
		Request request = mock(Request.class);
		when(request.params(eq("string"))).thenReturn("value");
		when(request.params(eq("bigDecimal"))).thenReturn("123.45");
		when(request.params(eq("long"))).thenReturn("123");
		when(request.params(eq("dateTime"))).thenReturn("2014-11-29T12:34:35");
		when(request.params(eq("date"))).thenReturn("2014-11-29");
		when(request.queryParams(eq("string"))).thenReturn("value");
		when(request.queryParams(eq("bigDecimal"))).thenReturn("123.45");
		when(request.queryParams(eq("long"))).thenReturn("123");
		when(request.queryParams(eq("dateTime"))).thenReturn("2014-11-29T12:34:35");
		when(request.queryParams(eq("date"))).thenReturn("2014-11-29");
		return request;
	}

	private JsonNode createJson() {
		JsonNode jsonNode = mock(JsonNode.class);
		when(jsonNode.get(eq("string"))).thenReturn(new TextNode("value"));
		when(jsonNode.get(eq("bigDecimal"))).thenReturn(new TextNode("123.45"));
		when(jsonNode.get(eq("long"))).thenReturn(new TextNode("123"));
		when(jsonNode.get(eq("dateTime"))).thenReturn(new TextNode("2014-11-29T12:34:35"));
		when(jsonNode.get(eq("date"))).thenReturn(new TextNode("2014-11-29"));
		return jsonNode;
	}
}
