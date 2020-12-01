package il.co.guzon.datetimepicker;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getHTMLEnter() {
        return "<BR>";
    }

    public static String getHTMLText_green(String text) {
        return "<font color=\"#009400\">" + text + "</font>";
    }

    public static String getHTMLText_red(String text) {
        return "<font color=\"#ba0c0c\">" + text + "</font>";
    }

    public static String getHTMLText_black(String text) {
        return "<font color=\"black\">" + text + "</font>";
    }

    public static String getHTMLText_blue(String text) {
        return "<font color=\"blue\">" + text + "</font>";
    }

    public static String getHTMLText_bold(String text) {
        return "<B>" + text + "</B>";
    }
}
