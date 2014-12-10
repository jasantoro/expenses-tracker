package com.toptal.expenses.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;

import com.google.inject.Provider;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.Expense;

/**
 * Manages the persistent layer for expenses
 * 
 * @author jorge.santoro
 */
@Singleton
public class ExpensePersistenceService extends PersistenceService<Expense> {

	@Inject
	public ExpensePersistenceService(Provider<EntityManager> entityManagerProvider) {
		super(entityManagerProvider);
	}

	public static enum ExpenseProperty {
		date, description, amount;
	}

	/**
	 * Gets the expense with id which belong to the username
	 */
	public Expense get(Long id, String username) {
		EntityManager entityManager = this.getEntityManager();
		TypedQuery<Expense> query = entityManager.createQuery(
				"select e from Expense e join e.owner u where e.id=:id and u.username=:username", Expense.class);
		query.setParameter("id", id);
		query.setParameter("username", username);
		try {
			return query.getSingleResult();
		} catch (NoResultException exception) {
			throw new NotFoundException("id", id, exception);
		}
	}

	/**
	 * Gets all expenses which belong to the username and are filtered
	 */
	public List<Expense> getAll(String username, Map<String, Object> filters) {
		EntityManager entityManager = this.getEntityManager();
		StringBuilder selectQuery = new StringBuilder(
				"select e from Expense e join e.owner u where u.username=:username");
		selectQuery.append(this.computeFilters(filters));
		if (filters.containsKey("orderBy")) {
			ExpenseProperty orderBy = (ExpenseProperty) filters.remove("orderBy");
			selectQuery.append(" order by e.").append(orderBy);
			boolean asc = false;
			if (filters.containsKey("asc")) {
				asc = (boolean) filters.remove("asc");
			}
			selectQuery.append(" ").append(asc ? "asc" : "desc");
		}
		TypedQuery<Expense> query = entityManager.createQuery(selectQuery.toString(), Expense.class);
		query.setParameter("username", username);
		filters.forEach((k, v) -> {
			if (String.class.isInstance(v)) {
				query.setParameter(k, "%" + v + "%");
			} else {
				query.setParameter(k, v);
			}
		});
		return query.getResultList();
	}

	private String computeFilters(Map<String, Object> filters) {
		StringBuilder builder = new StringBuilder();
		filters.forEach((k, v) -> {
			if (!"orderBy".equalsIgnoreCase(k) && !"asc".equalsIgnoreCase(k)) {
				builder.append(this.renderFilter(k, v));
			}
		});
		return builder.toString();
	}

	/**
	 * Renders the different filters in a sql query where clause
	 */
	private String renderFilter(String k, Object v) {
		StringBuilder builder = new StringBuilder(" and e.");
		switch (k) {
		case "startWeek":
			builder.append("date >= ");
			break;
		case "endWeek":
			builder.append("date <= ");
			break;
		default:
			builder.append(k);
			if (String.class.isInstance(v)) {
				builder.append(" like ");
			} else if (BigDecimal.class.isInstance(v)) {
				builder.append(" >= ");
			} else if (DateTime.class.isInstance(v)) {
				builder.append(" >= ");
			} else {
				builder.append(" = ");
			}
			break;
		}
		return builder.append(":").append(k).toString();
	}
}
