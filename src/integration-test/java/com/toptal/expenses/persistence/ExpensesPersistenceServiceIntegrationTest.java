package com.toptal.expenses.persistence;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;
import com.toptal.expenses.AbstractIntegrationTest;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.Expense;
import com.toptal.expenses.model.User;

public class ExpensesPersistenceServiceIntegrationTest extends AbstractIntegrationTest {

	private ExpensePersistenceService expensePersistenceService;
	private User owner;

	@BeforeMethod
	public void setup() {
		this.expensePersistenceService = this.getInstance(ExpensePersistenceService.class);
		this.owner = new User("jasantoro", "password", "Jorge", "Santoro");
		this.getInstance(UserPersistenceService.class).create(owner);
	}

	@Test
	public void getAll_empty() {
		Collection<Expense> all = this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap());
		assertTrue(all.isEmpty());
	}

	@Test
	public void create() {
		assertTrue(this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap()).isEmpty());
		this.expensePersistenceService.create(new Expense(this.owner, DateTime.now(), "description", new BigDecimal(
				"10"), "comment"));
		assertFalse(this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap()).isEmpty());
	}

	@Test(expectedExceptions = NotFoundException.class)
	public void get_no_found() {
		this.expensePersistenceService.get(new Long(1), this.owner.getUsername());
	}

	@Test
	public void get() {
		Expense expense = new Expense(this.owner, DateTime.now(), "description", new BigDecimal("10"), "comment");
		this.expensePersistenceService.create(expense);
		Expense getExpense = this.expensePersistenceService.get(expense.getId(), this.owner.getUsername());
		assertEquals(getExpense, expense);
	}

	@Test
	public void delete() {
		assertTrue(this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap()).isEmpty());
		this.expensePersistenceService.create(new Expense(this.owner, DateTime.now(), "description", new BigDecimal(
				"10"), "comment"));
		List<Expense> expenses = this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap());
		assertEquals(expenses.size(), 1);
		this.expensePersistenceService.delete(expenses.get(0));
		expenses = this.expensePersistenceService.getAll(this.owner.getUsername(), Maps.newHashMap());
		assertTrue(expenses.isEmpty());
	}

	@Test
	public void update() {
		Expense expense = new Expense(this.owner, DateTime.now(), "description", new BigDecimal("10"), "comment");
		this.expensePersistenceService.create(expense);
		expense = this.expensePersistenceService.get(expense.getId(), this.owner.getUsername());
		expense.setAmount(new BigDecimal("100"));
		this.expensePersistenceService.update(expense);
		expense = this.expensePersistenceService.get(expense.getId(), this.owner.getUsername());
		assertEquals(expense.getAmount(), new BigDecimal("100"));
	}

}
