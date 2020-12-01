package il.co.guzon.datetimepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.reflect.Array.getInt;

public class DateObject {
    public enum e_datePart{year,
        month,
        day,
        hour,
        minute}

    E_DateTimeType type;
    public Integer year;
    public Integer month;
    public Integer day;
    public Integer hour;
    public Integer minute;

    public DateObject(E_DateTimeType type, Date date) {
        this.type = type;

        if (date != null) {
            year = DateUtils.getYear(date);
            month = DateUtils.getMonth(date);
            day = DateUtils.getDay(date);
            hour = DateUtils.getHour(date);
            minute = DateUtils.getMinute(date);
        }
    }

    public Date toDate() {
        if (!hasData()) {
            return null;
        }

        String d = String.format("%02d",getValueInt( minute,0)) +
                   String.format("%02d", getValueInt( hour,0))+
                   String.format("%02d",getValueInt(day,1)) +
                   String.format("%02d", getValueInt( month,1))+
                   String.format("%04d", getValueInt( year,1900));

        SimpleDateFormat format = new SimpleDateFormat("mmHHddMMyyyy");

        try {
            return format.parse(d);
        } catch (Exception ex) {
        }

        return null;
    }

    public String toString(e_datePart part) {
        switch (part) {
            case year:
                return year == null ? "" : String.format("%04d", year);
            case month:
                return month == null ? "" : String.format("%02d", month);
            case day:
                return day == null ? "" : String.format("%02d", day);
            case hour:
                return hour == null ? "" : String.format("%02d", hour);
            case minute:
                return minute == null ? "" : String.format("%02d", minute);
        }

        return "";
    }

    public String toString(E_DateFormat dateFormat) {
         switch (type){
             case DateTime:
                 return  String.format("%02d",getValueInt( minute,0)) + ":"+
                         String.format("%02d", getValueInt( hour,0))+" " +
                         String.format("%02d",getValueInt(dateFormat==E_DateFormat.DayMonthYear? day:month,1)) + "/"+
                         String.format("%02d", getValueInt( dateFormat==E_DateFormat.DayMonthYear? month:day,1))+"/" +
                         String.format("%04d", getValueInt( year,1900));
             case DateOnly:
                 return String.format("%02d",getValueInt(dateFormat==E_DateFormat.DayMonthYear? day:month,1)) + "/"+
                         String.format("%02d", getValueInt( dateFormat==E_DateFormat.DayMonthYear? month:day,1))+"/" +
                                 String.format("%04d", getValueInt( year,1900));
             case TimeOnly:
                 return String.format("%02d",getValueInt( minute,0)) + ":"+
                         String.format("%02d", getValueInt( hour,0));
         }

         return "";
    }

    public String toHtmlString(E_DateFormat dateFormat) {
        StringBuilder builder=new StringBuilder();
        if(type!= E_DateTimeType.DateOnly){
            if(minute==null){
                builder.append("00:");
            }else{
                builder.append(DateUtils.getHTMLText_black(String.format("%02d",minute) + ":"));
            }
            if(hour==null){
                builder.append("00");
            }else{
                builder.append(DateUtils.getHTMLText_black(String.format("%02d",hour)));
            }
        }

        if(type == E_DateTimeType.DateTime){
            builder.append(" ");
        }

        if(type!= E_DateTimeType.TimeOnly){
            Integer d1=dateFormat ==E_DateFormat.DayMonthYear? day : month;
            Integer d2=dateFormat ==E_DateFormat.DayMonthYear? month : day;

            if(d1==null){
                builder.append("00/");
            }else{
                builder.append(DateUtils.getHTMLText_black(String.format("%02d",d1) + "/"));
            }
            if(d2==null){
                builder.append("00/");
            }else{
                builder.append(DateUtils.getHTMLText_black(String.format("%02d",d2) + "/"));
            }
            if(year==null){
                builder.append("0000");
            }else{
                builder.append(DateUtils.getHTMLText_black(String.format("%04d",year)));
            }
        }

        return builder.toString();
    }

    private int getValueInt(Integer value, int defaultValue) {
        if(value==null){
            return defaultValue;
        }

        return value;
    }

    public boolean hasData() {
        switch (type) {
            case DateTime: {
                return year != null &&
                        month != null &&
                        day != null &&
                        hour != null &&
                        minute != null;
            }
            case DateOnly: {
                return year != null &&
                        month != null &&
                        day != null;
            }
            case TimeOnly: {
                return hour != null &&
                        minute != null;
            }
        }

        return false;
    }
}
