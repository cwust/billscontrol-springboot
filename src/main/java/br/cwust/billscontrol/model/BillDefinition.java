package br.cwust.billscontrol.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.cwust.billscontrol.enums.RecurrenceType;

@Entity
@Table(name = "bill_definition")
public class BillDefinition {
	private Long id;
	private User user;
	private String name;
	private BigDecimal defaultValue;
	private LocalDate startDate;
	private LocalDate endDate;
	private Category category;
	private RecurrenceType recurrenceType;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name="name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="default_value", nullable = false)
	public BigDecimal getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(BigDecimal defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name="start_date", nullable = true)
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Column(name="end_date", nullable = true)
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="recurrence_type", nullable = false)
	public RecurrenceType getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(RecurrenceType recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	@Override
	public String toString() {
		return "BillDefinition [id=" + id + ", user=" + user + ", name=" + name + ", defaultValue=" + defaultValue
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", category=" + category + ", recurrenceType="
				+ recurrenceType + "]";
	}
}
