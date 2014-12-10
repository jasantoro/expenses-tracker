package com.toptal.expenses.persistence;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.inject.Provider;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.User;

/**
 * Manages the persistent layer for users
 * 
 * @author jorge.santoro
 */
@Singleton
public class UserPersistenceService extends PersistenceService<User> {

	@Inject
	public UserPersistenceService(Provider<EntityManager> entityManagerProvider) {
		super(entityManagerProvider);
	}

	/**
	 * Verify if exists a user with the username
	 */
	public boolean exists(String username) {
		EntityManager entityManager = this.getEntityManager();
		Query query = entityManager.createQuery("select count(1) from User u where u.username=:username");
		query.setParameter("username", username);
		return Long.class.cast(query.getSingleResult()) > 0;
	}

	/**
	 * Gets the user identified by usernma
	 */
	public User getUser(String username) {
		EntityManager entityManager = this.getEntityManager();
		TypedQuery<User> query = entityManager.createQuery("select u from User u where u.username=:username",
				User.class);
		query.setParameter("username", username);
		try {
			return query.getSingleResult();
		} catch (NoResultException exception) {
			throw new NotFoundException("user", username, exception);
		}
	}
}
