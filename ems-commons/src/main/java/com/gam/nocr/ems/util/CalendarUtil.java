package com.gam.nocr.ems.util;


import com.ghasemkiani.util.icu.PersianCalendar;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarUtil {

    private static final int[] STYLES;
    public static final TimeZone TIME_ZONE;
    private static String DELIMITER = "/";

    static {
        STYLES = new int[]{DateFormat.DEFAULT, DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL};
//        TIME_ZONE = TimeZone.getTimeZone(Configuration.getProperty("timezone"));
        TIME_ZONE = TimeZone.getTimeZone("Asia/Tehran");
    }

    public static String getDate(Locale locale) {
        return getDate(new Date(), locale);
    }

    public static String getDate(Date date) {
        return getDate(date, null);
    }

    public static Date getDate(String date) {
        return getDate(date, null);
    }

    public static Date getDate(String date, Locale locale) {

        if (date == null)
            return null;

        try {
            if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
                String[] splitDate = date.split("/");
                SimplePersianCalendar spc = new SimplePersianCalendar();
                spc.setDateFields(Integer.parseInt(splitDate[0]), (Integer.parseInt(splitDate[1]) - 1), Integer.parseInt(splitDate[2]));
                return new Date(spc.getTimeInMillis());
            } else {
                String[] splitDate = date.split("/");
                GregorianCalendar gc = new GregorianCalendar();
                gc.set(Integer.parseInt(splitDate[2]), (Integer.parseInt(splitDate[1]) - 1), Integer.parseInt(splitDate[0]));
                return new Date(gc.getTimeInMillis());
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getDate(Date date, Locale locale) {
        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;

        if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
            calendar = new PersianCalendar(locale);
            calendar.setTime(date);
            sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[1], STYLES[1], locale);
            sdf.applyPattern("yyyy/MM/dd");

            return sdf.format(calendar.getTime());
        } else {
            sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return sdf.format(date);
        }
    }

    public static Integer getPersianDateWithoutSlash(Date date, Locale locale) {
        String todayDate = getDate(date, locale).replaceFirst("/", "#");
        String year = todayDate.substring(0, todayDate.indexOf("#"));
        String month = todayDate.substring(todayDate.indexOf("#") + 1, todayDate.indexOf("/"));
        String day = todayDate.substring(todayDate.indexOf("/") + 1).split(" ")[0];
        if (month.length() < 2) month = "0" + month;
        if (day.length() < 2) day = "0" + day;
        return Integer.valueOf(year + month + day);
    }

    public static String getDateForModify(Date date, Locale locale) {
        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;

        if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
            calendar = new PersianCalendar(locale);
            calendar.setTime(date);
            sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[1], STYLES[1], locale);
            sdf.applyPattern("yyyy/MM/dd HH:mm:ss");

            return sdf.format(calendar.getTime());
        } else {
            sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return sdf.format(date);
        }
    }

    public static String convertGregorianToPersian(Timestamp date) {
        java.text.SimpleDateFormat dt1 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String formatDate = dt1.format(date);
        return convertGregorianToPersian(formatDate);
    }

    public static String convertGregorianToPersian(String date) {
        int year, month, day;
        DateFields t;
        String[] splitDate = date.split("/");
        String value = "";
        try {
            try {
                year = Integer.parseInt(splitDate[2]);
            } catch (NumberFormatException nfe) {
                year = 0;
            }
            try {
                month = Integer.parseInt(splitDate[1]) - 1;
            } catch (NumberFormatException nfe) {
                month = 0;
            }
            try {
                day = Integer.parseInt(splitDate[0]);
            } catch (NumberFormatException nfe) {
                day = 0;
            }

            SimplePersianCalendar c = new SimplePersianCalendar();
            c.set(c.YEAR, year);
            c.set(c.MONTH, month);
            c.set(c.DAY_OF_MONTH, day);
            t = c.getDateFields();

            String shamsiYear = Long.toString(t.getYear());
            String shamsiMonth = Long.toString(t.getMonth() + 1);
            String shamsiDay = Long.toString(t.getDay());
            shamsiMonth = shamsiMonth.length() == 1 ? "0" + shamsiMonth : shamsiMonth;
            shamsiDay = shamsiDay.length() == 1 ? "0" + shamsiDay : shamsiDay;
            value = shamsiYear + DELIMITER + shamsiMonth + DELIMITER + shamsiDay;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String convertPersianToGregorian(String date) {
        int year, month, day;
        DateFields t;
        String[] splitDate = date.split("/");
        String value = "";
        try {
            try {
                year = Integer.parseInt(splitDate[0]);
            } catch (NumberFormatException nfe) {
                year = 0;
            }
            try {
                month = Integer.parseInt(splitDate[1]) - 1;
            } catch (NumberFormatException nfe) {
                month = 0;
            }
            try {
                day = Integer.parseInt(splitDate[2]);
            } catch (NumberFormatException nfe) {
                day = 0;
            }

            SimplePersianCalendar c = new SimplePersianCalendar();
            c.setDateFields(year, month, day);
            String miladiYear = Long.toString(c.get(c.ERA) == c.AD ? c.get(c.YEAR) : -(c.get(c.YEAR) - 1));
            String miladiMonth = Long.toString(c.get(c.MONTH) + 1);
            String miladiDay = Long.toString(c.get(c.DAY_OF_MONTH));
            value = miladiYear + DELIMITER + miladiMonth + DELIMITER + miladiDay;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getPersianDateWithSlash(Locale locale) {
        String todayDate = getDate(locale).replaceFirst("/", "#");

        String year = todayDate.substring(0, todayDate.indexOf("#"));
        String month = todayDate.substring(todayDate.indexOf("#") + 1, todayDate.indexOf("/"));
        String day = todayDate.substring(todayDate.indexOf("/") + 1).split(" ")[0];

        if (month.length() < 2) month = "0" + month;
        if (day.length() < 2) day = "0" + day;

        return LangUtil.getEnglishNumber(year + "/" + month + "/" + day);
    }

    public static String getPersianDateWithoutSlash(Locale locale) {
        String todayDate = getDate(locale).replaceFirst("/", "#");

        String year = todayDate.substring(0, todayDate.indexOf("#"));
        String month = todayDate.substring(todayDate.indexOf("#") + 1, todayDate.indexOf("/"));
        String day = todayDate.substring(todayDate.indexOf("/") + 1).split(" ")[0];

        if (month.length() < 2) month = "0" + month;
        if (day.length() < 2) day = "0" + day;

        return LangUtil.getEnglishNumber(year + month + day);
    }

    public static Integer getPersianDateIndexInYearWithoutSlash(String date) {

        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        if (Integer.valueOf(month) > 6) {
            return Integer.valueOf(((Integer.valueOf(month) - 7) * 30) + 186 + (Integer.valueOf(day)));
        } else {
            return Integer.valueOf(((Integer.valueOf(month) - 1) * 31) + (Integer.valueOf(day)));
        }
    }

    public static Integer getPersianDateIndexInYear(String date) {
        String indexDate = date.replaceFirst("/", "#");

        String year = indexDate.substring(0, indexDate.indexOf("#"));
        String month = indexDate.substring(indexDate.indexOf("#") + 1, indexDate.indexOf("/"));
        String day = indexDate.substring(indexDate.indexOf("/") + 1);
        if (Integer.valueOf(month) > 6) {
            return Integer.valueOf(((Integer.valueOf(month) - 7) * 30) + 186 + (Integer.valueOf(day)));
        } else {
            return Integer.valueOf(((Integer.valueOf(month) - 1) * 31) + (Integer.valueOf(day)));
        }
    }

    public static String getPersianDateIndexInYear(Locale locale) {
        String todayDate = getDate(locale).replaceFirst("/", "#");

        String year = todayDate.substring(0, todayDate.indexOf("#"));
        String month = todayDate.substring(todayDate.indexOf("#") + 1, todayDate.indexOf("/"));
        String day = todayDate.substring(todayDate.indexOf("/") + 1, todayDate.indexOf(" "));

        if (Integer.valueOf(month) > 6) {
            return String.valueOf(((Integer.valueOf(month) - 7) * 30) + 186 + (Integer.valueOf(day)));
        } else {
            return String.valueOf(((Integer.valueOf(month) - 1) * 31) + (Integer.valueOf(day)));
        }
    }

    public static String addMonth(String date, int month) {
        String currentdate = "";
        try {

            date = date.replaceAll("/", "");

            int date_year = Integer.valueOf(date.substring(0, 4));
            int date_month = Integer.valueOf(date.substring(4, 6));
            int date_day = Integer.valueOf(date.substring(6, 8));

            PersianCalendar calendar = new PersianCalendar(new Locale("fa"));
            calendar.set(date_year, date_month, date_day);

            calendar.add(Calendar.MONTH, month);

            String now_year = String.valueOf(calendar.get(Calendar.YEAR));
            String now_month = String.valueOf(calendar.get(Calendar.MONTH));
            String now_day = String.valueOf(calendar.get(Calendar.DATE));

            if (now_month.length() != 2) {
                now_month = "0" + now_month;
            }

            if (now_month.equalsIgnoreCase("00")) {
                now_month = "12";
                now_year = String.valueOf(Integer.valueOf(now_year) - 1);
            }

            if (now_day.length() != 2) {
                now_day = "0" + now_day;
            }

            currentdate = now_year + "/" + now_month + "/" + now_day;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentdate.trim();
    }

    public static String getDateWithoutSlash(Date date, Locale locale, String pattern) {

        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;

        if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
            calendar = new PersianCalendar(locale);
            calendar.setTime(date);
            sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[0], STYLES[0], locale);
            sdf.applyPattern("YYYYMMdd");

            return sdf.format(calendar.getTime());
        } else {
            sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }

    public static String getTime(Date date, Locale locale) {

        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;


        if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
            calendar = new PersianCalendar(locale);
            calendar.setTime(date);

            sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[1], STYLES[1], locale);
            sdf.applyPattern("HH:mm:ss");

            return LangUtil.getEnglishNumber(sdf.format(calendar.getTime()));
        } else {
            sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(date);
        }

    }

    public static String getPersianDateTime(Date date) {

        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;
        calendar = new PersianCalendar(LangUtil.LOCALE_FARSI);
        calendar.setTime(date);

        sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[1], STYLES[1], LangUtil.LOCALE_FARSI);
        sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
        return LangUtil.getEnglishNumber(sdf.format(calendar.getTime()));

    }


    public static void main(String[] args) {
        System.out.println(CalendarUtil.getPersianDateTime(new Date()));
    }

    public static String getTimeWithoutDot(Date date, Locale locale) {

        if (date == null)
            return null;

        SimpleDateFormat sdf;
        Calendar calendar;


        if (locale != null && locale.equals(LangUtil.LOCALE_FARSI)) {
            calendar = new PersianCalendar(locale);
            calendar.setTime(date);

            sdf = (SimpleDateFormat) calendar.getDateTimeFormat(STYLES[1], STYLES[1], locale);
            sdf.applyPattern("HHmmss");

            return LangUtil.getEnglishNumber(sdf.format(calendar.getTime()));
        } else {
            sdf = new SimpleDateFormat("HHmmss");
            return sdf.format(date);
        }

    }

    public static int compareDate(Date today, String selectedDateStr) {

        if (selectedDateStr != null && !StringUtils.isEmpty(selectedDateStr)) {
            Date selectedDate = getDate(selectedDateStr, LangUtil.LOCALE_FARSI);

            Calendar calendar;
            calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Calendar selectedCalendar;
            selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(selectedDate);
            selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
            selectedCalendar.set(Calendar.MINUTE, 0);
            selectedCalendar.set(Calendar.SECOND, 0);
            selectedCalendar.set(Calendar.MILLISECOND, 0);

            return selectedCalendar.compareTo(calendar);
        }
        return 0;

    }
}