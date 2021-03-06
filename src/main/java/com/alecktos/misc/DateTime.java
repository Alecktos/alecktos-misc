package com.alecktos.misc;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {

	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private String dateTimeString;

	private DateTime() {
		//Empty
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof DateTime))return false;

		return other.toString().equals(this.toString());
	}

	public static DateTime createFromNow() {
		DateTime dateTime = new DateTime();
		dateTime.dateTimeString = dateTime.getUTCDateTimeFromTimeStamp(dateTime.getTimestamp());
		return dateTime;
	}

	public static DateTime createFromTimeStamp(Long timeStamp) {
		DateTime dateTime = new DateTime();
		dateTime.dateTimeString = dateTime.getUTCDateTimeFromTimeStamp(timeStamp);
		return dateTime;
	}

	public static DateTime createFromDateTimeString(String dateTimeString) {
		DateTime dateTime = new DateTime();
		dateTime.dateTimeString = dateTimeString;
		return dateTime;
	}

	public static DateTime createFromSQLFromat(String sqlformatString) {
		final long timestamp = parseDateTime("yyyy-MM-dd HH:mm:ss", sqlformatString);
		return DateTime.createFromTimeStamp(timestamp);
	}

	private Long getTimestamp() {
		return System.currentTimeMillis();
	}

	private String getUTCDateTimeFromTimeStamp(Long timeStamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(timeStamp);
	}

	public Long toTimeStamp() {
		return parseDateTime(DATE_TIME_FORMAT, dateTimeString);
	}

	private static long parseDateTime(String format, String stringToFormat) {
		DateFormat formatter;
		formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date;

		try {
			date = formatter.parse(stringToFormat);
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse datetime string");
		}

		return date.getTime();
	}

	@Override
	public String toString() {
		return dateTimeString;
	}

	/**
	 * @return String Only date part of datetime
	 */
	public String toDateString() {
		return dateTimeString.split(" ")[0];
	}

	/**
	 *
	 * @return String only the time part from the datetime string
	 */
	public String toTimeString() {
		return dateTimeString.split(" ")[1];
	}

	public String toSqlFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		final Date date;
		try {
			date = formatter.parse(dateTimeString);
		} catch (ParseException e) {
			throw new RuntimeException("Failed converting dateTime to string");
		}

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public boolean isDateAfter(DateTime otherDateTime) {
		Calendar thisDay = getCalendarFromDateTime();
		Calendar otherDay = otherDateTime.getCalendarFromDateTime();
		return otherDay.before(thisDay);
	}

	public Calendar getCalendarFromDateTime() {
		Date time1 = null;
		try {
			time1 = new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time1);
		resetTimeOnCalendar(calendar);
		return calendar;
	}

	private void resetTimeOnCalendar(Calendar calendar) {
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public DateTime createDateTimeWithTimeForward(int minutes) {
		return createDateTimeWithTimeRewind(minutes * -1);
	}

	public DateTime createDateTimeWithTimeRewind(int minutes) {
		Date dateTime;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		try {
			dateTime = simpleDateFormat.parse(dateTimeString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.MINUTE, minutes * -1);
		return createFromDateTimeString(simpleDateFormat.format(calendar.getTime()));
	}

	public boolean isTimeInInterval(String timeFrom, String timeTo) {
		return isInInterval(timeFrom, timeTo, toTimeString(), TIME_FORMAT);
	}

	public boolean isInInterval(DateTime dateTimeFrom, DateTime dateTimeTo) {
		return isInInterval( dateTimeFrom.toString(), dateTimeTo.toString(), toString(), DATE_TIME_FORMAT);
	}

	private static boolean isInInterval(String dateTimeFrom, String dateTimeTo, String dateTimeToTest, String dateTimeFormat) {

		try {
			Date time1 = new SimpleDateFormat(dateTimeFormat).parse(dateTimeFrom);

			Date time2 = new SimpleDateFormat(dateTimeFormat).parse(dateTimeTo);

			Date d = new SimpleDateFormat(dateTimeFormat).parse(dateTimeToTest);

			return d.after(time1) && d.before(time2);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	//http://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-in-java
	public int daysBetween(final String dateTimeDay2){
		Calendar day1 = DateTime.createFromDateTimeString(dateTimeString).getCalendarFromDateTime();
		Calendar day2 = DateTime.createFromDateTimeString(dateTimeDay2).getCalendarFromDateTime();

		Calendar dayOne = (Calendar) day1.clone(),
				dayTwo = (Calendar) day2.clone();

		if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
			return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
		} else {
			if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
				//swap them
				Calendar temp = dayOne;
				dayOne = dayTwo;
				dayTwo = temp;
			}
			int extraDays = 0;

			int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

			while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
				dayOne.add(Calendar.YEAR, -1);
				// getActualMaximum() important for leap years
				extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
			}

			return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
		}
	}

}
