package com.toptal.expenses.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

public class DayExpensesAggregatorTest {

	@Test
	public void addExpense() {
		Expense expense1 = mock(Expense.class);
		when(expense1.getAmount()).thenReturn(new BigDecimal("12"));
		Expense expense2 = mock(Expense.class);
		when(expense2.getAmount()).thenReturn(new BigDecimal("10"));
		DayExpensesAggregator aggregator = new DayExpensesAggregator(DateTime.now().toLocalDate());
		aggregator.addExpense(expense1);
		assertEquals(new BigDecimal("12"), aggregator.getTotalAmount());
		assertEquals(aggregator.getExpenses().size(), 1);
		aggregator.addExpense(expense2);
		assertEquals(new BigDecimal("22"), aggregator.getTotalAmount());
		assertEquals(aggregator.getExpenses().size(), 2);
	}
}
