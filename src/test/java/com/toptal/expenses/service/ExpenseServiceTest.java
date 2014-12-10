package com.toptal.expenses.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.DateTime;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;
import com.toptal.expenses.model.Expense;
import com.toptal.expenses.persistence.ExpensePersistenceService;
import com.toptal.expenses.persistence.UserPersistenceService;

public class ExpenseServiceTest {

	private ExpenseService expenseService;
	@Mock
	private ExpensePersistenceService expensePersistenceService;
	@Mock
	private UserPersistenceService userPersistenceService;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.expenseService = new ExpenseService(userPersistenceService, expensePersistenceService);
	}

	@Test
	public void create() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		Expense expense = this.expenseService.create(DateTime.now(), "description", new BigDecimal("123.45"),
				"comment", "username");
		assertNotNull(expense);
		inOrder.verify(userPersistenceService).getUser(eq("username"));
		inOrder.verify(expensePersistenceService).create(eq(expense));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void update() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		Expense toUpdate = mock(Expense.class);
		when(expensePersistenceService.get(eq(new Long(1)), eq("username"))).thenReturn(toUpdate);
		Expense expense = this.expenseService.update(new Long(1), DateTime.now(), "description", new BigDecimal(
				"123.45"), "comment", "username");
		assertEquals(expense, toUpdate);
		verify(toUpdate).setAmount(eq(new BigDecimal("123.45")));
		verify(toUpdate).setComment(eq("comment"));
		verify(toUpdate).setDescription(eq("description"));
		inOrder.verify(expensePersistenceService).update(eq(toUpdate));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void delete() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		Expense toRemove = mock(Expense.class);
		when(expensePersistenceService.get(eq(new Long(1)), eq("username"))).thenReturn(toRemove);
		this.expenseService.delete(new Long(1), "username");
		inOrder.verify(expensePersistenceService).delete(eq(toRemove));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void get() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		this.expenseService.get(new Long(1), "username");
		inOrder.verify(expensePersistenceService).get(eq(new Long(1)), eq("username"));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void getAll() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		Map<String, Object> filters = Maps.newHashMap();
		this.expenseService.getAll("username", filters);
		inOrder.verify(expensePersistenceService).getAll(eq("username"), eq(filters));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void getAllByWeek() {
		InOrder inOrder = inOrder(userPersistenceService, expensePersistenceService);
		Map<String, Object> filters = Maps.newHashMap();
		this.expenseService.getAllByWeek("username", filters);
		assertNotNull(filters.get("startWeek"));
		assertNotNull(filters.get("endWeek"));
		inOrder.verify(expensePersistenceService).getAll(eq("username"), eq(filters));
		inOrder.verifyNoMoreInteractions();
	}
}
