package br.cwust.billscontrol.date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimePeriod {
	private LocalDate start;
	private LocalDate end;

	public static TimePeriod forMonth(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		return new TimePeriod(date, date.plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS));
	}

	public static TimePeriod forMonth(LocalDate date) {
		return TimePeriod.forMonth(date.getYear(), date.getMonthValue());
	}
	
	
	public TimePeriod(LocalDate start, LocalDate end) {
		if (start == null) {
			throw new IllegalArgumentException("TimePeriod must hava a start date");
		}
		this.start = start;
		this.end = end;
	}

	public boolean periodsOverlap(TimePeriod other) {
		return (other.end == null || !this.start.isAfter(other.end))
				&& (this.end == null || !other.start.isAfter(this.end));
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

}
