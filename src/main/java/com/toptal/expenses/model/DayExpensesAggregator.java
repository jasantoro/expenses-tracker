package com.toptal.expenses.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;

/**
 * Calculates the week days expenses
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
public class DayExpensesAggregator implements Serializable {

	private LocalDate date;
	private Collection<Expense> expenses = new ArrayList<>();
	private BigDecimal totalAmount = BigDecimal.ZERO;

	public DayExpensesAggregator(LocalDate date) {
		this.date=date;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
		this.totalAmount = this.totalAmount.add(expense.getAmount());
	}

	public Collection<Expense> getExpenses() {
		return expenses;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
