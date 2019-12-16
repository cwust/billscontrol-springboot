package br.cwust.billscontrol.date;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {
	public Date dateNow() {
		return new Date();
	}
	
	public Date addSeconds(Date date, long secondsToAdd) {
		return Date.from(date.toInstant().plusSeconds(secondsToAdd));
	}
	
	public LocalDate parseLocalDate(String dateStr) {
		if (dateStr == null) {
			return null;
		} else {
			return LocalDate.parse(dateStr);
		}
	}
	
}
