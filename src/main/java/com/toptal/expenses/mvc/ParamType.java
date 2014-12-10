package com.toptal.expenses.mvc;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.toptal.expenses.persistence.ExpensePersistenceService.ExpenseProperty;

/**
 * Helper to read the params in the correct type
 * 
 * @author jorge.santoro
 */
public interface ParamType<T> {

	public static final ParamType<String> STRING = new ParamType<String>() {
		@Override
		public String transform(String value) {
			return value;
		}
	};
	public static final ParamType<Long> LONG = new ParamType<Long>() {
		@Override
		public Long transform(String value) {
			return Long.valueOf(value);
		}
	};
	public static final ParamType<BigDecimal> BIG_DECIMAL = new ParamType<BigDecimal>() {
		@Override
		public BigDecimal transform(String value) {
			return new BigDecimal(value);
		}
	};
	public static final ParamType<DateTime> DATE_TIME = new ParamType<DateTime>() {
		@Override
		public DateTime transform(String value) {
			try {
				return ISODateTimeFormat.dateHourMinuteSecond().parseDateTime(value);
			} catch (IllegalArgumentException exception) {
				return ISODateTimeFormat.dateHourMinute().parseDateTime(value);
			}
		}
	};
	public static final ParamType<DateTime> DATE = new ParamType<DateTime>() {
		@Override
		public DateTime transform(String value) {
			return ISODateTimeFormat.date().parseDateTime(value);
		}
	};
	public static final ParamType<Boolean> BOOLEAN = new ParamType<Boolean>() {
		@Override
		public Boolean transform(String value) {
			return value != null;
		}
	};
	public static final EnumParamType<ExpenseProperty> EXPENSE_PROPERTY = new EnumParamType<>(ExpenseProperty.class);

	public T transform(String value);

	default boolean isValid(String value) {
		try {
			this.transform(value);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	class EnumParamType<E extends Enum<E>> implements ParamType<E> {
		private Class<E> clazz;

		public EnumParamType(Class<E> clazz) {
			this.clazz = clazz;
		}

		@Override
		public E transform(String value) {
			return Enum.valueOf(clazz, value);
		}
	}

}
