package com.toptal.expenses.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An user's expense
 * 
 * @author jorge.santoro
 */
@SuppressWarnings("serial")
@Entity
public class Expense implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(targetEntity = User.class)
	@JsonIgnore
	private User owner;
	@Type(type = "com.toptal.expenses.persistence.PersistentDateTime")
	private DateTime date;
	@Column(length = 140)
	private String description;
	private BigDecimal amount;
	@Column(length = 1000)
	private String comment;

	@SuppressWarnings("unused")
	private Expense() {
	}

	public Expense(User owner, DateTime date, String description, BigDecimal amount, String comment) {
		this.owner = owner;
		this.date = date;
		this.description = description;
		this.amount = amount;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
