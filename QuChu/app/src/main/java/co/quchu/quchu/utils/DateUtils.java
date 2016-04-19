package co.quchu.quchu.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.ParsePosition;
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
    public static String Analysis_DATA_FORMAT = "yyyyMMDDHHmmss";

    /**
     * 将Date类型转换为日期字符串
     *
     * @param type 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    public static String formatDate(String type) {
        try {
            Date date = getNowDate();
            SimpleDateFormat df = new SimpleDateFormat(type, Locale.CHINA);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        return formatter.parse(dateString, pos);
    }
/*时间戳转换成字符窜*/


    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sf.format(d);
    }

    public static String getDateToString(String format, long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINA);
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sf.format(d);
    }


    public static String getUTCTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CHINA);
        fmt.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)));
        return fmt.format(new Date());
    }

    public static long getTimeStamp(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
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
