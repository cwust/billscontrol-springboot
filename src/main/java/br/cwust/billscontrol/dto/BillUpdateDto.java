package br.cwust.billscontrol.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.cwust.billscontrol.model.Category;

public class BillUpdateDto {
	private Long id;
	private String name;
	private String additionalInfo;
	private LocalDate dueDate;
	private LocalDate paidDate;
	private BigDecimal value;
	private BigDecimal paidValue;
	private Category category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDate paidDate) {
		this.paidDate = paidDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getPaidValue() {
		return paidValue;
	}

	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
