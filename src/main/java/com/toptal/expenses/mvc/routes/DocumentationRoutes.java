package com.toptal.expenses.mvc.routes;

import java.io.InputStream;
import java.io.StringWriter;

import spark.utils.IOUtils;

import com.toptal.expenses.mvc.ParamType;
import com.toptal.expenses.mvc.routes.builder.RouteBuilder;
import com.toptal.expenses.utils.RequestHelper;

/**
 * Documentation routes
 * 
 * @author jorge.santoro
 */
public class DocumentationRoutes implements RoutesConstants {

	public void registerRoutes() {
		RouteBuilder.get(API_DOC).withRoute((request, response) -> {
			return this.getContent("documentation/api-docs.json");
		}).register();
		RouteBuilder.get(GROUP_DOC).withRoute((request, response) -> {
			String group = RequestHelper.getParam(request, "id", ParamType.STRING);
			switch (group) {
			case "expenses":
				return this.getContent("documentation/expenses.json");
			case "user":
				return this.getContent("documentation/user.json");
			default:
				throw new IllegalArgumentException("Invalid documentation group " + group);
			}
		}).register();
	}

	private String getContent(String filePath) {
		try {
			StringWriter writer = new StringWriter();
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filePath);
			IOUtils.copy(stream, writer);
			stream.close();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error reading file", e);
		}
	}
}
