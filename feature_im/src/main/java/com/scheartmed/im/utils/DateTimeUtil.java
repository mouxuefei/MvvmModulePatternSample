package com.scheartmed.im.utils;

import android.content.Context;


import com.core.basemvvm.BaseApplication;
import com.scheartmed.im.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeUtil {

    private final static long minute = 60 * 1000;
    private final static long hour = 60 * minute;
    private final static long day = 24 * hour;
    private final static long week = 7 * day;
    private final static long month = 31 * day;
    private final static long year = 12 * month;

    /**
     * @return format text
     */
    public static String getTimeFormatText(long formTime) {
        long newDate = formTime;
        String pattern = "yyyy年";
        String monthPattern = "MM月dd日";
        String dayPattern = "yyyy-MM-dd";
        String hourPattern = "HH:mm";
        SimpleDateFormat sfYear = new SimpleDateFormat(pattern);
        SimpleDateFormat sfMonth = new SimpleDateFormat(monthPattern);
        SimpleDateFormat sfDay = new SimpleDateFormat(dayPattern);
        SimpleDateFormat sfHour = new SimpleDateFormat(hourPattern);
        String timeText = sfYear.format(new Date(newDate));
        if (timeText.equals(sfYear.format(new Date()))) {
            timeText = sfMonth.format(new Date(newDate));
        }
        if (sfDay.format(new Date(newDate)).equals(sfDay.format(new Date()))) {
            timeText = sfHour.format(new Date(newDate));
        }
        return timeText;
    }

    public static String formatSeconds(long seconds) {
        Context context = BaseApplication.Companion.instance();
        String timeStr = seconds + context.getString(R.string.str_date_second_short);
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + context.getString(R.string.str_date_minute_short) + second + context.getString(R.string.str_date_second_short);
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + context.getString(R.string.str_date_hour_short) + min + context.getString(R.string.str_date_minute_short) + second + context.getString(R.string.str_date_second_short);
                if (hour % 24 == 0) {
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + context.getString(R.string.str_date_day_short);
                } else if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + context.getString(R.string.str_date_day_short) + hour + context.getString(R.string.str_date_hour_short) + min + context.getString(R.string.str_date_minute_short) + second + context.getString(R.string.str_date_second_short);
                }
            }
        }
        return timeStr;
    }

    public static String formatSecondsTo00(int timeSeconds) {
        int second = timeSeconds % 60;
        int minuteTemp = timeSeconds / 60;
        if (minuteTemp > 0) {
            int minute = minuteTemp % 60;
            int hour = minuteTemp / 60;
            if (hour > 0) {
                return (hour >= 10 ? (hour + "") : ("0" + hour)) + ":" + (minute >= 10 ? (minute + "") : ("0" + minute))
                        + ":" + (second >= 10 ? (second + "") : ("0" + second));
            } else {
                return (minute >= 10 ? (minute + "") : ("0" + minute)) + ":"
                        + (second >= 10 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:" + (second >= 10 ? (second + "") : ("0" + second));
        }
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 判断时间是否大于5分钟
     */
    public static boolean checkOver5Minutes(
            long lastShowTimeStamp,
            long thisTimeStamp
    ) {
        long deviation = thisTimeStamp - lastShowTimeStamp;
        long deviationMinutes = Math.abs(deviation) / 60;
        return deviationMinutes >= 5;
    }
}
