package co.quchu.quchu.utils;

import android.util.Log;

import co.quchu.quchu.BuildConfig;

/**
 * LogUtils
 * User: Chenhs
 * Date: 2015-10-19
 * 日志工具类
 */
public class LogUtils {
    private static String tag = "QuChu";

    public static void e(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印json数据
     *
     * @param msg
     */
    public static void json(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.e(tag + "Json=", msg);
        }
    }

    public static void netLog(Object msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Log.e(tag + "Net=", msg.toString());
        }
    }

    public static void jsonLong(String s, String msg) {
        int chunkCount = msg.length() / 4000;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = 4000 * (i + 1);
            if (max >= msg.length()) {
                Log.d(tag, s + "NetService== (" + i + "/" + chunkCount + "): " + msg.substring(4000 * i));
            } else {
                Log.d(tag, s + "NetService== (" + i + "/" + chunkCount + "): " + msg.substring(4000 * i, max));
            }
        }
    }
}
