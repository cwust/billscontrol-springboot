package br.cwust.billscontrol.date;

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
}
