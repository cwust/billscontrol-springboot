package br.cwust.billscontrol.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bill_instance")
public class BillInstance {
	private Long id;
	private BillDefinition billDefinition;
	private Integer recurrencePeriod;
	private String additionalInfo;
	private LocalDate dueDate;
	private LocalDate paidDate;
	private BigDecimal value;
	private BigDecimal paidValue;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	public BillDefinition getBillDefinition() {
		return billDefinition;
	}

	public void setBillDefinition(BillDefinition billDefinition) {
		this.billDefinition = billDefinition;
	}

	@Column(name="recurrence_period", nullable = true)
	public Integer getRecurrencePeriod() {
		return recurrencePeriod;
	}

	public void setRecurrencePeriod(Integer recurrencePeriod) {
		this.recurrencePeriod = recurrencePeriod;
	}

	@Column(name="additional_info", nullable = true)
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Column(name="due_date", nullable = true)
	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name="paid_date", nullable = true)
	public LocalDate getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDate paidDate) {
		this.paidDate = paidDate;
	}

	@Column(name="value", nullable = true)
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Column(name="paid_value", nullable = true)
	public BigDecimal getPaidValue() {
		return paidValue;
	}

	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}

	@Override
	public String toString() {
		return "BillInstance [id=" + id + ", billDefinition=" + billDefinition + ", recurrencePeriod="
				+ recurrencePeriod + ", additionalInfo=" + additionalInfo + ", dueDate=" + dueDate + ", paidDate="
				+ paidDate + ", value=" + value + ", paidValue=" + paidValue + "]";
	}

}
