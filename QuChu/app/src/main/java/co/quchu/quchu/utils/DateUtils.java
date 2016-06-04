package co.quchu.quchu.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static String DATA_FORMAT_HH_MM = "HH:mm";


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
        long s = (System.currentTimeMillis() - getTimeStamp(timeStr));
        if (s < 600000) {//十分钟以内
            return cont.getString(R.string.mypost_timer_now);
        }
//        if (s < 3600) {
//            int m = s / 60;
//            return m + cont.getString(R.string.mypost_timer_minutes);
//        }
        s = getTimeStamp(timeStr);
        if (s > getStartTime() && s - getStartTime() < 86400000) {//当天消息
//            int h = s / 3600;
            return getDateToString(DATA_FORMAT_HH_MM, timeStr);
        }
        if (s < getStartTime() && getStartTime() - s < 86400000) {
//            int d = s / 86400;
            return "昨天";
        }
//        if (s < 86400 * 365) {
//            int m = s / (86400 * 30);
//            return m + cont.getString(R.string.mypost_timer_months);
//        }
//        int y = s / (86400 * 365);
        return getDateToString(DATA_FORMAT_MM_DD_YYYY, timeStr);
    }

    /**
     * 当日0点的时间
     *
     * @return
     */
    public static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
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
