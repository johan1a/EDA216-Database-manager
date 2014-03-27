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

	public static boolean isDate(String string) {
		return string.matches("\\d{4}-\\d{2}-\\d{2}");
	}
}
