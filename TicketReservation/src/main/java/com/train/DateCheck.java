package com.train;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

class DateCheck {
	public static boolean dateCheck(String date1) throws ParseException {
		System.out.println(date1);
		int dat1 = Integer.parseInt(date1.substring(8, 10));
		int month1 = Integer.parseInt(date1.substring(5, 7));
		int year1 = Integer.parseInt(date1.substring(0, 4));
		String given = dat1 + "-" + month1 + "-" + year1;
		System.out.println(given);
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		Date givenDate = sdf2.parse(given);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String dates = String.valueOf(formatter.format(date));

		int dat = Integer.parseInt(dates.substring(0, 2));
		int month = Integer.parseInt(dates.substring(3, 5));
		int year = Integer.parseInt(dates.substring(6, 10));

		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now2 = LocalDateTime.now();
		int[] arr = { 31, 29, 31, 30, 31, 31, 30, 31, 30, 31, 30, 31 };
		if (year % 4 != 0 || (year % 100 == 0 && year % 400 != 0)) {
			String dateString = odd(month, dat, year, arr);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date2 = sdf.parse(dateString);
			return check(givenDate, date2);
		} else {
			arr[1] = 28;

			String dateString = odd(month, dat, year, arr);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date2 = sdf.parse(dateString);
			return check(givenDate, date2);
		}
	}

	public static boolean check(Date given, Date finish) throws ParseException {

		Date yesterday = getMeYesterday();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		System.out.println("yesterday :" + formatter.format(yesterday));
		System.out.println("Booking day:  " + formatter.format(given));
		System.out.println("Expiration day " + formatter.format(finish));

		System.out.println("yesterday" + yesterday);
		if (given.after(yesterday) && given.before(finish)) {

			return true;
		} else {

			return false;
		}
	}

	public static Date getMeYesterday() {

		return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
	}

	public static String odd(int month, int dat, int year, int[] arr) {

		if (month == 2) {
			if (dat > 23) {
				dat = 7 - (arr[month - 1] % dat);
				if (month == 12) {
					year += 1;
					month = 1;
				} else {
					month += 1;
				}
			} else {
				dat = dat + 6;
			}
		} else if (month == 4 || month == 7 || month == 9 || month == 11) {

			if (dat > 24) {
				dat = 7 - (31 % dat);

				if (month == 12) {
					year += 1;
					month = 1;
				} else {
					month += 1;
				}
			} else {
				dat = dat + 6;
			}
		} else {

			if (dat > 25) {
				dat = 7 - (31 % dat);

				if (month == 12) {
					year += 1;
					month = 1;
				} else {
					month += 1;
				}
			} else {
				dat = dat + 6;
			}

		}
		String datString = "";
		String monString = "";
		if (dat < 10) {
			datString = "0" + dat;
		} else {
			datString = String.valueOf(dat);
		}
		if (month < 10) {
			monString = "0" + month;
		} else {
			monString = "" + month;
		}
		return datString + "-" + monString + "-" + year;
	}

	public static boolean timeCheck(Time time) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		Time time2 = Time.valueOf(dtf.format(now));
		System.out.println("newtime" + time2);
		if (time2.after(time)) {
			System.out.println("false");
			return false;
		}
		System.out.println("true");
		return true;

	}

}
