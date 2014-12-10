package com.toptal.expenses.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.DateTime;

import com.google.inject.persist.Transactional;
import com.toptal.expenses.model.Expense;
import com.toptal.expenses.model.ExpensesAggregator;
import com.toptal.expenses.model.User;
import com.toptal.expenses.persistence.ExpensePersistenceService;
import com.toptal.expenses.persistence.ExpensePersistenceService.ExpenseProperty;
import com.toptal.expenses.persistence.UserPersistenceService;

/**
 * Services to handle the expenses
 * 
 * @author jorge.santoro
 */
@Singleton
public class ExpenseService {

	private UserPersistenceService userPersistenceService;
	private ExpensePersistenceService expensePersistenceService;

	@Inject
	public ExpenseService(UserPersistenceService userPersistenceService, ExpensePersistenceService expensePersistenceService) {
		this.userPersistenceService = userPersistenceService;
		this.expensePersistenceService = expensePersistenceService;
	}

	@Transactional
	public Expense create(DateTime date, String description, BigDecimal amount, String comment, String username) {
		User user = this.userPersistenceService.getUser(username);
		Expense expense = new Expense(user, date, description, amount, comment);
		this.expensePersistenceService.create(expense);
		return expense;
	}

	@Transactional
	public Expense update(Long id, DateTime date, String description, BigDecimal amount, String comment, String username) {
		Expense expense = this.get(id, username);
		expense.setAmount(amount);
		expense.setComment(comment);
		expense.setDescription(description);
		expense.setDate(date);
		this.expensePersistenceService.update(expense);
		return expense;
	}

	@Transactional
	public void delete(Long id, String username) {
		Expense expense = this.get(id, username);
		this.expensePersistenceService.delete(expense);
	}

	public Expense get(Long id, String username) {
		return this.expensePersistenceService.get(id, username);
	}

	public Collection<Expense> getAll(String username, Map<String, Object> filters) {
		filters.computeIfAbsent("orderBy", k -> ExpenseProperty.date);
		filters.computeIfAbsent("asc", k -> false);
		return this.expensePersistenceService.getAll(username, filters);
	}

	public ExpensesAggregator getAllByWeek(String username, Map<String, Object> filters) {
		filters.computeIfAbsent("week", k -> "current");
		filters.compute("week", (k, v) -> {
			String weeks = v.toString();
			switch (weeks) {
			case "current":
				return 0;
			case "previous":
				return 1;
			default:
				int previousWeeks = Integer.parseInt(weeks);
				if (previousWeeks < 0) {
					throw new IllegalArgumentException("Invalid week value '" + weeks + "'");
				}
				return weeks;
			}
		});
		this.analyzeWeekFilter(filters);
		DateTime startTime = (DateTime) filters.get("startWeek");
		DateTime endTime = (DateTime) filters.get("endWeek");
		Collection<Expense> expenses = this.getAll(username, filters);
		return new ExpensesAggregator(startTime, endTime, expenses);
	}

	/**
	 * Analyzes the filters and if contains a week filter populate the start and
	 * end week values
	 */
	private void analyzeWeekFilter(Map<String, Object> filters) {
		if (filters.containsKey("week")) {
			filters.remove("date");
			String week = filters.remove("week").toString();
			DateTime now = DateTime.now().plus(1).withTime(0, 0, 0, 0);
			int previousWeeks = Integer.parseInt(week);
			DateTime date = now.minusDays(previousWeeks * 7);
			filters.put("startWeek", date.minusDays(date.getDayOfWeek()));
			filters.put("endWeek", date.plusDays(7 - date.getDayOfWeek()));
		}
	}
}
