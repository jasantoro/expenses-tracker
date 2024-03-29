package com.toptal.expenses.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;
import org.joda.time.DateTime;

/**
 * Persistent representation of a {@link DateTime}
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
public class PersistentDateTime implements EnhancedUserType, Serializable {

	public static final PersistentDateTime INSTANCE = new PersistentDateTime();

	private static final int[] SQL_TYPES = new int[] { Types.TIMESTAMP, };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return DateTime.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}
		DateTime dtx = (DateTime) x;
		DateTime dty = (DateTime) y;

		return dtx.equals(dty);
	}

	@Override
	public int hashCode(Object object) throws HibernateException {
		return object.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object timestamp = StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names, session, owner);
		if (timestamp == null) {
			return null;
		}
		return new DateTime(timestamp);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index, session);
		} else {
			StandardBasicTypes.TIMESTAMP.nullSafeSet(st, ((DateTime) value).toDate(), index, session);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object value) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public String objectToSQLString(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toXMLString(Object object) {
		return object.toString();
	}

	@Override
	public Object fromXMLString(String string) {
		return new DateTime(string);
	}
}
