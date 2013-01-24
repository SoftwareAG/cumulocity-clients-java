package com.cumulocity.me.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class DateUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    protected DateUtils() {
    }

    public static Date parse(String dateString) {
        try {
            String[] dateArray = StringUtils.split(dateString, "T");
            String[] date = StringUtils.split(dateArray[0], "-");
            String[] timeTab = null;
            String timeZoneId = null;
            
            if (dateArray[1].indexOf("-") > 0) {
                timeTab = StringUtils.split(dateArray[1], "-");
                timeZoneId = "Etc/GMT-" + StringUtils.replaceAll(StringUtils.split(timeTab[1], ":")[0], "0", "");
            } else if (dateArray[1].indexOf("+") > 0) {
                timeTab = StringUtils.split(dateArray[1], "+");
                timeZoneId = "Etc/GMT+" + StringUtils.replaceAll(StringUtils.split(timeTab[1], ":")[0], "0", "");
            } else if (dateArray[1].indexOf("Z") > 0) {
                timeTab = StringUtils.split(dateArray[1], "Z");
                timeZoneId = "GMT";
            }
            
            TimeZone timezone = TimeZone.getTimeZone(timeZoneId);
            Calendar cal = Calendar.getInstance(timezone);
            
            String[] time = StringUtils.split(timeTab[0], ":");
            String[] seconds = StringUtils.split(time[2], ".");
            seconds[1] = StringUtils.replaceAll(seconds[1], "Z", "");

            cal.set(Calendar.YEAR, Integer.parseInt(date[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            cal.set(Calendar.SECOND, Integer.parseInt(seconds[0]));
            cal.set(Calendar.MILLISECOND, Integer.parseInt(seconds[1]));

            return cal.getTime();
        } catch (Exception ex) {
            throw new RuntimeException(dateString + ": " + ex.getMessage());
        }
    }

    public static String format(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(date);

        StringBuffer buffer = new StringBuffer();
        buffer.append(cal.get(Calendar.YEAR)).append("-").append(getStringOfIntValue(cal.get(Calendar.MONTH) + 1)).append("-")
                .append(getStringOfIntValue(cal.get(Calendar.DAY_OF_MONTH))).append("T")
                .append(getStringOfIntValue(cal.get(Calendar.HOUR_OF_DAY))).append(":")
                .append(getStringOfIntValue(cal.get(Calendar.MINUTE))).append(":")
                .append(getStringOfIntValue(cal.get(Calendar.SECOND)))
                .append(".").append(cal.get(Calendar.MILLISECOND))
                .append("Z");

        return buffer.toString();
    }

    private static String getStringOfIntValue(int value) {
        StringBuffer buffer = new StringBuffer();
        if (value < 10) {
            buffer.append("0");
        }
        buffer.append(value);
        return buffer.toString();
    }
}
