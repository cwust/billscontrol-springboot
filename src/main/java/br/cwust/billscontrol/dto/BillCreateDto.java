package br.cwust.billscontrol.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.cwust.billscontrol.model.BillDefinition;

public class BillCreateDto {
	private String name;
	private BigDecimal value;
	private String startDate;
	private String endDate;
	private String recurrenceType;
	private CategoryDto category;

	@NotEmpty(message = "{bill.name.notempty}")
	@Length(message = "{bill.name.length}", max = BillDefinition.NAME_LENGTH)
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

	@NotEmpty(message = "{bill.startDate.notempty}")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@NotEmpty(message = "{bill.recurrenceType.notempty}")
	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

}
