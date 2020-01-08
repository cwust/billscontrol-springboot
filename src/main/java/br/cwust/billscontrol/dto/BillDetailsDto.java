package br.cwust.billscontrol.dto;

import java.math.BigDecimal;

public class BillDetailsDto {
	private Long id;
	private String name;
	private BigDecimal value;
	private String dueDate;
	private String paidDate;
	private BigDecimal paidValue;
	private CategoryDto category;
	private String recurrenceType;
	private Integer recurrencePeriod;
	private String additionalInfo;

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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public BigDecimal getPaidValue() {
		return paidValue;
	}

	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	public Integer getRecurrencePeriod() {
		return recurrencePeriod;
	}

	public void setRecurrencePeriod(Integer recurrencePeriod) {
		this.recurrencePeriod = recurrencePeriod;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String toString() {
		return "BillDetailsDto [id=" + id + ", name=" + name + ", value=" + value + ", dueDate=" + dueDate
				+ ", paidDate=" + paidDate + ", paidValue=" + paidValue + ", category=" + category + ", recurrenceType="
				+ recurrenceType + ", recurrencePeriod=" + recurrencePeriod + ", additionalInfo=" + additionalInfo
				+ "]";
	}

}
