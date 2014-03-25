package temp;

import org.joda.time.DateTime;

public class Test {

	public static void main(String[] args) {
		DateTime dt = new DateTime();
		System.out.println(dt.toYearMonthDay());
		System.out.println(dt.getHourOfDay() + ":" + dt.getMinuteOfHour());

	}
}
