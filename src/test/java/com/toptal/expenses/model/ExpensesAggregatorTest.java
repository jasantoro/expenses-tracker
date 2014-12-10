package com.toptal.expenses.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Collection;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class ExpensesAggregatorTest {

	@Test
	public void init() {
		Expense expense1 = mock(Expense.class);
		when(expense1.getAmount()).thenReturn(new BigDecimal("12"));
		when(expense1.getDate()).thenReturn(DateTime.parse("2014-11-28"));
		Expense expense2 = mock(Expense.class);
		when(expense2.getAmount()).thenReturn(new BigDecimal("10"));
		when(expense2.getDate()).thenReturn(DateTime.parse("2014-11-27"));
		Expense expense3 = mock(Expense.class);
		when(expense3.getAmount()).thenReturn(new BigDecimal("12"));
		when(expense3.getDate()).thenReturn(DateTime.parse("2014-11-26"));
		Expense expense4 = mock(Expense.class);
		when(expense4.getAmount()).thenReturn(new BigDecimal("10"));
		when(expense4.getDate()).thenReturn(DateTime.parse("2014-11-28"));
		Collection<Expense> expenses = Lists.newArrayList(expense1, expense2, expense3, expense4);
		ExpensesAggregator aggregator = new ExpensesAggregator(new DateTime().minus(7), new DateTime(), expenses);
		assertNull(aggregator.getSundayExpenses());
		assertNull(aggregator.getMondayExpenses());
		assertNull(aggregator.getTuesdayExpenses());
		assertNotNull(aggregator.getWednesdayExpenses());
		assertEquals(aggregator.getWednesdayExpenses().getExpenses().size(), 1);
		assertNotNull(aggregator.getThursdayExpenses());
		assertEquals(aggregator.getThursdayExpenses().getExpenses().size(), 1);
		assertNotNull(aggregator.getFridayExpenses());
		assertEquals(aggregator.getFridayExpenses().getExpenses().size(), 2);
		assertNull(aggregator.getSaturdayExpenses());
		assertEquals(new BigDecimal("44"), aggregator.getWeekTotalAmount());
		assertEquals(new BigDecimal("6.29"), aggregator.getPerDayAverageAmount());
	}
}
