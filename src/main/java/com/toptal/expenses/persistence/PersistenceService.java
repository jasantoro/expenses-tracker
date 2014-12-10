package com.toptal.expenses.persistence;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.DataException;

import com.google.inject.Provider;

/**
 * Service which handle the basic operations to the database
 * 
 * @author jorge.santoro
 */
public abstract class PersistenceService<T> {

	private Provider<EntityManager> entityManagerProvider;

	public PersistenceService(Provider<EntityManager> entityManagerProvider) {
		this.entityManagerProvider = entityManagerProvider;
	}

	public void create(T t) {
		this.handle(entityManager -> entityManager.persist(t));
	}

	public void update(T t) {
		this.handle(entityManager -> entityManager.persist(t));
	}

	public void delete(T t) {
		this.handle(entityManager -> entityManager.remove(t));
	}

	protected EntityManager getEntityManager() {
		EntityManager entityManager = this.entityManagerProvider.get();
		return entityManager;
	}

	private void handle(Consumer<EntityManager> consumer) {
		try {
			consumer.accept(this.getEntityManager());
		} catch (PersistenceException exception) {
			if (DataException.class.isInstance(exception.getCause())) {
				throw new IllegalArgumentException("Invalid data constrains", exception);
			}
			throw new RuntimeException("Error executing persistence command", exception);
		}
	}
}
