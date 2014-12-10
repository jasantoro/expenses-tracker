package com.toptal.expenses.booting;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.servlet.SparkFilter;

import com.google.common.collect.Lists;
import com.google.inject.persist.PersistFilter;

/**
 * Main class to start the application
 * 
 * @author jorge.santoro
 */
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	private static final String BASE_CONTEXT = "/expenses-tracker";

	public static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(64, 128, 60, TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>());

	public static void main(String[] args) throws Exception {
		if (args != null && args.length > 0) {
			int port = Integer.valueOf(args[0]);
			App app = new App();
			app.init(port);
		} else {
			System.out.println("You need to specify at least the [port] to start the application.\nExample:\n"
					+ App.class.getName() + " port");
		}
	}

	private Server server;

	public void init(int port) {
		LOGGER.info("Starting the application...");
		this.server = new Server(new ExecutorThreadPool(THREAD_POOL));

		ContextHandlerCollection handler = new ContextHandlerCollection();
		ServletContextHandler serviceHandler = new ServletContextHandler(handler, BASE_CONTEXT, false, false);

		this.addResourceContext(handler, "app");
		this.addResourceContext(handler, "docs");

		FilterHolder gzipFilter = new SparkFilterHolder(new GzipFilter());
		FilterHolder sparkFilter = new SparkFilterHolder(new SparkFilter(), h -> {
			try {
				h.getFilter().init(new SparkFilterConfig(serviceHandler));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		FilterHolder transactionFilter = new SparkFilterHolder(Application.getInjector().getInstance(
				PersistFilter.class));

		addFilters(serviceHandler, gzipFilter, transactionFilter, sparkFilter);

		this.server.setHandler(handler);

		try {
			this.server.start();

			ServerConnector connector = new ServerConnector(this.server);
			connector.setPort(port);
			this.server.setConnectors(new Connector[] { connector });
			connector.start();

			this.server.join();
		} catch (Exception exception) {
			LOGGER.error("Error starting the application", exception);
		}
		LOGGER.info("Application started");
	}

	private void addResourceContext(HandlerContainer parent, String publicPath) {
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("/webapp/" + publicPath));
		ContextHandler baseHandler = new ContextHandler(parent, BASE_CONTEXT + "/" + publicPath);
		baseHandler.setHandler(resourceHandler);
	}

	private static void addFilters(ServletContextHandler servletContextHandler, FilterHolder... holders) {
		for (FilterHolder filterHolder : holders) {
			servletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
		}
	}

	static class SparkFilterHolder extends FilterHolder {

		private boolean initialized = false;
		private Consumer<FilterHolder> consumer;

		public SparkFilterHolder(Filter filter) {
			this(filter, null);
		}

		public SparkFilterHolder(Filter filter, Consumer<FilterHolder> consumer) {
			super(filter);
			this.consumer = consumer;
		}

		@Override
		public void initialize() throws Exception {
			if (!this.initialized) {
				this.initialized = true;
				if (this.consumer != null) {
					this.consumer.accept(this);
				} else {
					super.initialize();
				}
			}
		}
	}

	static class SparkFilterConfig implements FilterConfig {

		private ServletContextHandler contextHandler;

		public SparkFilterConfig(ServletContextHandler contextHandler) {
			this.contextHandler = contextHandler;
		}

		@Override
		public ServletContext getServletContext() {
			return this.contextHandler.getServletContext();
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return Collections.enumeration(Lists.newArrayList("applicationClass"));
		}

		@Override
		public String getInitParameter(String name) {
			switch (name) {
			case "applicationClass":
				return Application.class.getName();
			}
			return null;
		}

		@Override
		public String getFilterName() {
			return "SparkFilter";
		}
	}
}
