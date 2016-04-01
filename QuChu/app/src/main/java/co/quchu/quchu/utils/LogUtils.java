package co.quchu.quchu.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.base.Constants;

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
            Logger.init();
            Logger.e(msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Logger.init();
            Logger.i(msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Logger.init();
            Logger.i(msg);
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Logger.init();
            Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Logger.init();
            Logger.d(msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DISPLAY_LOG) {
            Logger.init();
            Logger.w(msg);
        }
    }

    /**
     * 打印json数据
     * @param msg
     */
    public static void json(String msg){
        if (BuildConfig.DISPLAY_LOG) {
           Log.e(tag + "Json=", msg);
        }
    }

    public static void netLog(Object msg){
        if (BuildConfig.DISPLAY_LOG) {
            Log.e(tag+"Net=",msg.toString());
        }
    }

    public static void jsonLong(String s, String msg) {
        int chunkCount = msg.length() / 4000;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = 4000 * (i + 1);
            if (max >= msg.length()) {
                Log.d(tag,s+"NetService== (" + i + "/" + chunkCount + "): " + msg.substring(4000 * i));
            } else {
                Log.d(tag,s+"NetService== (" + i + "/" + chunkCount + "): " + msg.substring(4000 * i, max));
            }
        }
    }


    /*
    *Error:Execution failed for task ':app:dexDebug'.
> com.android.ide.common.process.ProcessException: org.gradle.process.internal.ExecException: Process 'command 'C:\Program Files\Java\jdk1.8.0_60\bin\java.exe'' finished with non-zero exit value 2
    * */
}
