package com.mobigen.framework.service.time;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;


@Service
public class TimeService {

	public String getServerTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		return date.format(formatter);
	}

	public long getTimeOffset(String clientDatetime) {
		LocalDateTime serverDate = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime clientDate = LocalDateTime.parse(clientDatetime, formatter);

        return getTime(serverDate) - getTime(clientDate);
	}

	private long getTime(LocalDateTime lt) {
		return lt.get(ChronoField.MILLI_OF_SECOND);
	}

}
