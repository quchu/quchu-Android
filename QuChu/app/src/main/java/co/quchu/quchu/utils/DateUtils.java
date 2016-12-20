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

  //  public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

  public static String DATE_FORMAT_YYYY_MM_DD = "yyyy.MM.dd";

  public static String DATA_FORMAT_MM_DD_YYYY = "MM.dd.yyyy";
  public static String DATA_FORMAT_MM_DD_YYYY_CLEAR = "yyyy - MM - dd";

  public static String DATA_FORMAT_YYYY_MM_DDHH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static String DATA_FORMAT_HH_MM = "HH:mm";

  public static String getDateToString(String format, long time) {
    Date d = new Date(time);
    SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINESE);
    sf.setTimeZone(TimeZone.getDefault());
    return sf.format(d);
  }

  public static String getCurrentTime(String format) {
    Date d = new Date();
    SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINESE);
    return sf.format(d);
  }

  public static String getDateToString(String format, String time) {
    Date d = new Date(getTimeStamp(time));
    SimpleDateFormat sf = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
    sf.setTimeZone(TimeZone.getDefault());
    return sf.format(d);
  }

  public static long getTimeStamp(String timeStr) {
    SimpleDateFormat sdf =
        new SimpleDateFormat(DATA_FORMAT_YYYY_MM_DDHH_MM_SS, Locale.SIMPLIFIED_CHINESE);
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

  /**
   * 时间格式转换
   * <p>默认格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param time
   * @return
   */
  public static String dateTimeFormat(String time) {
    SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
    long timestamp = string2Timestamp(time);
    if (timestamp == -1) {
      return time;
    }
    Date date = new Date(timestamp);
    return format.format(date);
  }

  /**
   * 时间格式转换
   * <p>格式为用户自定义</p>
   *
   * @param time
   * @param string
   * @return
   */
  public static String dateTimeFormat(String time, String string) {
    SimpleDateFormat format = new SimpleDateFormat(string);
    long timestamp = string2Timestamp(time);
    if (timestamp == -1) {
      return time;
    }
    Date date = new Date(timestamp);
    return format.format(date);
  }

  /**
   * 将时间戳转为时间字符串
   * <p>默认格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param millis 毫秒时间戳
   * @return 时间字符串
   */
  public static String millis2String(long millis) {
    return millis2String(millis, DEFAULT_FORMAT);
  }

  /**
   * 将时间戳转为时间字符串
   * <p>格式为用户自定义</p>
   *
   * @param millis 毫秒时间戳
   * @param string 时间格式
   * @return 时间字符串
   */
  public static String millis2String(long millis, String string) {
    SimpleDateFormat format = new SimpleDateFormat(string, Locale.getDefault());
    return format.format(new Date(millis));
  }

  /**
   * 将时间字符串转为时间戳
   * <p>默认格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param time 时间字符串
   * @return 毫秒时间戳
   */
  public static long string2Timestamp(String time) {
    return string2Timestamp(time, DEFAULT_FORMAT);
  }

  /**
   * 将时间字符串转为时间戳
   * <p>格式为用户自定义</p>
   *
   * @param time   时间字符串
   * @param string 时间格式
   * @return 毫秒时间戳
   */
  public static long string2Timestamp(String time, String string) {
    SimpleDateFormat format = new SimpleDateFormat(string, Locale.getDefault());
    try {
      return format.parse(time).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return -1;
  }
}
