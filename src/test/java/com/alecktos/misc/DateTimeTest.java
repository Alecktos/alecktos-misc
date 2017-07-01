package com.alecktos.misc;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeTest {

	@Test
	public void testIsInTimeInterval() {
		assertTrue(DateTime.createFromDateTimeString("01/01/2016 12:35:00").isTimeInInterval("12:00:00", "13:00:00"));
		assertFalse(DateTime.createFromDateTimeString("01/01/2016 13:35:00").isTimeInInterval("12:00:00", "13:00:00"));
		assertFalse(DateTime.createFromDateTimeString("01/01/2016 00:35:00").isTimeInInterval("12:00:00", "13:00:00"));
	}

	@Test
	public void testIsInInterval() {
		DateTime dateTimeToTest = DateTime.createFromDateTimeString("01/01/2016 12:35:00");
		boolean isInInterval = dateTimeToTest.isInInterval(DateTime.createFromDateTimeString("12/31/2015 12:35:00"), DateTime.createFromDateTimeString("01/02/2016 12:35:00"));
		assertTrue(isInInterval);

		dateTimeToTest = DateTime.createFromDateTimeString("01/01/2016 01:00:00");
		isInInterval = dateTimeToTest.isInInterval(DateTime.createFromDateTimeString("01/01/2016 01:01:00"), DateTime.createFromDateTimeString("01/02/2016 00:00:00"));
		assertFalse(isInInterval);
	}

	@Test
	public void testGoBackInTime() {
		DateTime dateTime = DateTime.createFromDateTimeString("01/01/2016 14:00:00");
		assertEquals("13:30:00", dateTime.createDateTimeWithTimeRewind(30).toTimeString());
	}

	@Test
	public void testNumberOfDays() {
		final int numberOfDays = DateTime.createFromDateTimeString("01/01/2016 00:00:00").daysBetween("01/02/2016 00:00:00");
		assertEquals(1, numberOfDays);
	}

	@Test
	public void testSQLFormat() {
		final DateTime dateTime = DateTime.createFromDateTimeString("01/05/2016 14:00:00");
		assertEquals("2016-01-05 14:00:00", dateTime.toSqlFormat());
	}
}
