package co.quchu.quchu.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import co.quchu.quchu.R;

/**
 * DateUtils
 * User: Chenhs
 * Date: 2015-10-19
 * 日期工具类
 */
public class DateUtils {
    public static String DATA_FORMAT_MM_DD_YYYY = "MM-dd-yyyy";
    public static String DATA_FORMAT_YYYY_MM_DDHH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    public static String getDateToString(String format, long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINESE);
        sf.setTimeZone(TimeZone.getDefault());
        return sf.format(d);
    }

    public static String getDateToString(String format, String time) {
        Date d = new Date(getTimeStamp(time));
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
        sf.setTimeZone(TimeZone.getDefault());
        return sf.format(d);
    }

    public static long getTimeStamp(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT_YYYY_MM_DDHH_MM_SS, Locale.SIMPLIFIED_CHINESE);
        Date d;
        long timeStamp = 0L;
        try {
            d = sdf.parse(timeStr);
            timeStamp = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static String getTimeRange(String timeStr, Context cont) {
        int s = (int) (System.currentTimeMillis() - getTimeStamp(timeStr)) / 1000;
        if (s < 60) {
            return cont.getString(R.string.mypost_timer_now);
        }
        if (s < 3600) {
            int m = s / 60;
            return m + cont.getString(R.string.mypost_timer_minutes);
        }
        if (s < 86400) {
            int h = s / 3600;
            return h + cont.getString(R.string.mypost_timer_hours);
        }
        if (s < 86400 * 30) {
            int d = s / 86400;
            return d + cont.getString(R.string.mypost_timer_days);
        }
        if (s < 86400 * 365) {
            int m = s / (86400 * 30);
            return m + cont.getString(R.string.mypost_timer_months);
        }
        int y = s / (86400 * 365);
        return y + cont.getString(R.string.mypost_timer_years);
    }


    public static int getHour(String time) {
        try {
            return Integer.parseInt(time.substring(11, 13));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getMin(String time) {
        try {
            return Integer.parseInt(time.substring(14, 16));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
