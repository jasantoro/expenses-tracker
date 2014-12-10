package com.toptal.expenses.mvc.routes;

import static spark.Spark.after;
import static spark.Spark.before;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracking routes
 * 
 * @author jorge.santoro
 */
public class MiscRoutes implements RoutesConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(MiscRoutes.class);
	public static final BigDecimal ONE_MILLIS_IN_NANO = new BigDecimal(1000000);

	public void registerRoutes() {
		before("/*",
				(request, response) -> {
					request.attribute("startTime", System.nanoTime());
					LOGGER.info(
							"[REQUEST] -> {}: {}",
							request.requestMethod(),
							request.url()
									+ (StringUtils.isBlank(request.queryString()) ? "" : "?" + request.queryString()));
				});

		before("/*", (request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET, PUT, DELETE, POST, OPTIONS");
			response.header("Access-Control-Allow-Headers",
					"Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With, X-XSRF-TOKEN");
			response.type(JSON);
		});

		after("/*",
				(request, response) -> {
					long startTime = (long) request.attribute("startTime");
					BigDecimal spentTime = new BigDecimal(System.nanoTime() - startTime);
					LOGGER.info("[RESPONSE] spent -> {} millis",
							spentTime.divide(ONE_MILLIS_IN_NANO, 2, RoundingMode.CEILING));
				});

		new ExceptionRoutes().registerRoutes();
	}

}
