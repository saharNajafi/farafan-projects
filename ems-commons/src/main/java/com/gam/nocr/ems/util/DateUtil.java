package com.gam.nocr.ems.util;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.faces.model.SelectItem;
import java.util.*;

/**
 * Created by safiary on 10/7/17.
 */
public class DateUtil {
    private static String[] months = (Configuration.getProperty("messages.hijri.months")).split(",");

    private static String DELIMITER = "/";

    public static List<SelectItem> lunarbirthDay(Date citizenBirthDate) {

        // An arry of Hijri month names

        List<SelectItem> items = new ArrayList<SelectItem>();

        List<String> hijriStringList = new ArrayList<String>(5);
        citizenBirthDate = getDateAtMidnight(citizenBirthDate);

        // Calculate the Hijri value from 2 days before citizen birth date to 2
        // days after it
        for (int i = -2; i <= 2; i++) {
            Date tempDate = differDay(citizenBirthDate, i);
            try {
                hijriStringList.add(gampooya.tools.date.DateUtil.convert(tempDate, gampooya.tools.date.DateUtil.HIJRI));
            } catch (ArrayIndexOutOfBoundsException e) {
                // In case of any error, add an empty string to the calculated
                // values list
                hijriStringList.add("");
            }
        }

        int firstHijriYearCount = 0;

        // If first day of a Hijri year exists in calculated values, make sure
        // its previous value ends in 29
        for (String hijriString : hijriStringList) {
            if (hijriString.endsWith("/01/01")) {
                firstHijriYearCount++;
            }
        }

        if (firstHijriYearCount > 1) {
            String temp = hijriStringList.get(0);
            hijriStringList.set(1, hijriStringList.get(0));

            temp = temp.substring(0, 8) + "29";
            hijriStringList.set(0, temp);
        }

        // Fill the combo with calculated values
        int loopCount = 0;
        for (String hijriString : hijriStringList) {

            String y = hijriString.split(DELIMITER)[0];
            int m = Integer.valueOf(hijriString.split(DELIMITER)[1]);
            String d = hijriString.split(DELIMITER)[2];

            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(LangUtil.getFarsiNumber(d) + '\t'
                    + months[m - 1] + '\t'
                    + LangUtil.getFarsiNumber(y));
            selectItem.setValue(hijriString);
            items.add(selectItem);

            // If 29th of a month is in list and there is no 30th value after,
            // add the 30th day next to it
            if (hijriString.endsWith("/29")
                    && ((hijriStringList.size() != loopCount + 1) && (hijriStringList
                    .get(loopCount + 1).endsWith("/30") == false))) {
                String year = hijriString.split(DELIMITER)[0];
                int month = Integer.valueOf(hijriString.split(DELIMITER)[1]);

                SelectItem theLastDayOfMonth = new SelectItem();
                theLastDayOfMonth.setLabel(LangUtil.getFarsiNumber("30")
                        + '\t'
                        + months[month - 1]
                        + '\t'
                        + LangUtil.getFarsiNumber(year));
                theLastDayOfMonth.setValue(year + DELIMITER + month + "/30");
                items.add(theLastDayOfMonth);
            }

            loopCount++;

        }

        return items;
    }


    /**
     * Given a {@link Date} value and returns a new result that its time related fields are filled with zero
     * (e.g. 2014/12/2T00:00:00:000)
     *
     * @param date the date value to fill its time related fields with zero
     * @return a new date that its time related fields are filled with zero (e.g. 2014/12/2T00:00:00:000)
     */
    public static Date getDateAtMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Given a {@link Date} value, adds number of days to it (specified as 'value' parameter) and returns a
     * new {@link Date}
     *
     * @param date  The date object to add some days to it
     * @param value Number of days to be added to the date
     * @return Newly calculated date
     */
    public static Date differDay(Date date, Integer value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

    public static String formatAsDate(String date) {
        if (StringUtils.isNotEmpty(date) && date.trim().length() == 8) {
            return date.substring(0, 4) + DELIMITER + date.substring(4, 6) + DELIMITER + date.substring(6, 8);
        }
        return date;
    }

    public static String getSolarDateString(int solarDate) {
        String solarDateStr = String.valueOf(solarDate);
        return solarDateStr.substring(0, 4) + DELIMITER + solarDateStr.substring(4, 6) + DELIMITER + solarDateStr.substring(6, 8);
    }

    public static String getHijriDateAsString(String date) {
        String y = date.split(DELIMITER)[0];
        int m = Integer.valueOf(date.split(DELIMITER)[1]);
        String d = date.split(DELIMITER)[2];
        return LangUtil.getFarsiNumber(d) + '\t'
                + months[m - 1] + '\t'
                + LangUtil.getFarsiNumber(y);
    }

    public static Date incrementDateUtil(Date curentDate, Integer increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curentDate);
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }
}
