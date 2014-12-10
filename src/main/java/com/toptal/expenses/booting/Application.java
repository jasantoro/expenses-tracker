package com.toptal.expenses.booting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.servlet.SparkApplication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.toptal.expenses.mvc.routes.AuthenticationRoutes;
import com.toptal.expenses.mvc.routes.DocumentationRoutes;
import com.toptal.expenses.mvc.routes.ExpenseRoutes;
import com.toptal.expenses.mvc.routes.MiscRoutes;

public class Application implements SparkApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private static final Injector INJECTOR;

	static {
		JpaPersistModule jpaPersistModule = new JpaPersistModule("jpaUnit");
		try {
			Properties properties = new Properties();
			InputStream inputStream = Application.class.getClassLoader().getResourceAsStream("persistence.properties");
			if (inputStream != null) {
				properties.load(inputStream);
				jpaPersistModule.properties(properties);
			}
		} catch (IOException e) {
			LOGGER.info("No persistence.properties in classpath, using default configuration");
		}
		INJECTOR = Guice.createInjector(jpaPersistModule);
	}

	@Override
	public void init() {
		MiscRoutes miscRoutes = INJECTOR.getInstance(MiscRoutes.class);
		miscRoutes.registerRoutes();
		AuthenticationRoutes authenticationRoutes = INJECTOR.getInstance(AuthenticationRoutes.class);
		authenticationRoutes.registerRoutes();
		ExpenseRoutes expenseRoutes = INJECTOR.getInstance(ExpenseRoutes.class);
		expenseRoutes.registerRoutes();
		new DocumentationRoutes().registerRoutes();
	}

	public static Injector getInjector() {
		return INJECTOR;
	}
}
