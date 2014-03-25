package util;

import org.joda.time.DateTime;

public class Util {

	/*
	 * Checks if the given date is in the correct format. Returns today's date
	 * if not.
	 */
	@SuppressWarnings("deprecation")
	public static String checkDate(String date) {
		DateTime dt = new DateTime();
		String result = date;
		if (!isDate(date)) {
			result = dt.toYearMonthDay().toString();
		}
		return result;
	}

	/*
	 * Checks if the given time is in the correct format. Returns the current
	 * time if not.
	 */
	public static String checkTime(String time) {
		DateTime dt = new DateTime();
		String result = time;
		if (!isTime(time)) {
			result = dt.getHourOfDay() + ":" + dt.getMinuteOfHour();
		}
		return result;
	}

	public static boolean isDate(String string) {
		return string.matches("\\d{4}-\\d{2}-\\d{2}");
	}

	public static boolean isTime(String string) { // TODO dubbelkolla regex
		return string.matches("(?:[01][0-9]|[2][0-3]):?[0-5]\\d");
	}
}
