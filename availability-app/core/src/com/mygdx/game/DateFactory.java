package com.mygdx.game;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateFactory {

    public static Date offsetDate(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(java.util.Calendar.DATE, i);
        date = calendar.getTime();
        return date;
    }

    public static void setCalendarMinimum(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    }

    public static String getDayStr(Date date) {
        DateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String getDayOfMonthStr(Date date) {
        DateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String getMonthStr(Date date) {
        DateFormat format = new SimpleDateFormat("MMMM");
        return format.format(date);
    }

    public static String getMonthStr(Calendar calendar) {
        return new SimpleDateFormat("MMM").format(calendar.getTime());
    }

    public static String getYearStr(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static String dateToString(Date date) {
        DateFormat format = new SimpleDateFormat("EEE, MMM dd");
        return format.format(date);
    }

    public static Date StringToDate(String s) throws ParseException {
        Date date = new SimpleDateFormat("EEE, MMM dd").parse(s);
        return date;
    }

    public static String dateToSixDigString(Date date) {
        DateFormat format = new SimpleDateFormat("MM/dd/yy");
        return format.format(date);
    }

    public static Date sixDigStringToDate(String s) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yy").parse(s);
        return date;
    }

    public static int getMonthLength(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getDateRangeLength(Vector2f<Date> dateRange) {
        long diff = dateRange.end.getTime() - dateRange.start.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
    }

    public static int getDateRangeLength(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
    }

    public static int getDaysToEndOfMonth(Calendar calendar) {
        int dayInMonth = Calendar.DAY_OF_MONTH;
        int daysLeftInMonth = calendar.getActualMaximum(dayInMonth) - calendar.get(dayInMonth);
        return daysLeftInMonth + 1;
    }

}
