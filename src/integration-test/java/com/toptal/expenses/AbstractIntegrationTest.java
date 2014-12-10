package com.toptal.expenses;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.inject.persist.PersistService;
import com.toptal.expenses.booting.Application;

public abstract class AbstractIntegrationTest {

	static {
		PersistService persistService = Application.getInjector().getInstance(PersistService.class);
		persistService.start();
	}

	@BeforeMethod
	public final void begin() {
		EntityManager entityManager = Application.getInjector().getInstance(EntityManager.class);
		entityManager.getTransaction().begin();
	}

	@AfterMethod
	public final void end() {
		EntityManager entityManager = Application.getInjector().getInstance(EntityManager.class);
		entityManager.getTransaction().rollback();
	}

	protected <T> T getInstance(Class<T> clazz) {
		return Application.getInjector().getInstance(clazz);
	}
}
