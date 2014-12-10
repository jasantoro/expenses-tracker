package com.toptal.expenses.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Calculates the week days expenses
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
public class ExpensesAggregator implements Serializable {

	private DateTime startDate;
	private DateTime endDate;

	private DayExpensesAggregator sundayExpenses;
	private DayExpensesAggregator mondayExpenses;
	private DayExpensesAggregator tuesdayExpenses;
	private DayExpensesAggregator wednesdayExpenses;
	private DayExpensesAggregator thursdayExpenses;
	private DayExpensesAggregator fridayExpenses;
	private DayExpensesAggregator saturdayExpenses;

	private BigDecimal weekTotalAmount = BigDecimal.ZERO;
	private BigDecimal perDayAverageAmount;

	public ExpensesAggregator(DateTime startDate, DateTime endDate, Collection<Expense> expenses) {
		this.startDate = startDate;
		this.endDate = endDate;
		if (CollectionUtils.isNotEmpty(expenses)) {
			this.calculate(expenses);
		}
	}

	private void calculate(Collection<Expense> expenses) {
		for (Expense expense : expenses) {
			DayExpensesAggregator dayExpenses = this.getExpensesForDay(expense.getDate());
			dayExpenses.addExpense(expense);
			this.weekTotalAmount = this.weekTotalAmount.add(expense.getAmount());
		}
		this.perDayAverageAmount = this.weekTotalAmount.divide(new BigDecimal("7"), 2, RoundingMode.CEILING);
	}

	private DayExpensesAggregator getExpensesForDay(DateTime dateTime) {
		int dayOfWeek = dateTime.getDayOfWeek();
		switch (dayOfWeek) {
		case 1:
			if (this.mondayExpenses == null) {
				this.mondayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.mondayExpenses;
		case 2:
			if (this.tuesdayExpenses == null) {
				this.tuesdayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.tuesdayExpenses;
		case 3:
			if (this.wednesdayExpenses == null) {
				this.wednesdayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.wednesdayExpenses;
		case 4:
			if (this.thursdayExpenses == null) {
				this.thursdayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.thursdayExpenses;
		case 5:
			if (this.fridayExpenses == null) {
				this.fridayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.fridayExpenses;
		case 6:
			if (this.saturdayExpenses == null) {
				this.saturdayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.saturdayExpenses;
		case 7:
			if (this.sundayExpenses == null) {
				this.sundayExpenses = new DayExpensesAggregator(dateTime.toLocalDate());
			}
			return this.sundayExpenses;
		}
		throw new RuntimeException("Invalid day of week");
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DayExpensesAggregator getFridayExpenses() {
		return fridayExpenses;
	}

	public DayExpensesAggregator getMondayExpenses() {
		return mondayExpenses;
	}

	public BigDecimal getPerDayAverageAmount() {
		return perDayAverageAmount;
	}

	public DayExpensesAggregator getSaturdayExpenses() {
		return saturdayExpenses;
	}

	public DayExpensesAggregator getSundayExpenses() {
		return sundayExpenses;
	}

	public DayExpensesAggregator getThursdayExpenses() {
		return thursdayExpenses;
	}

	public DayExpensesAggregator getTuesdayExpenses() {
		return tuesdayExpenses;
	}

	public DayExpensesAggregator getWednesdayExpenses() {
		return wednesdayExpenses;
	}

	public BigDecimal getWeekTotalAmount() {
		return weekTotalAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
