package co.quchu.quchu.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

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
        if (Constants.ISPRINTLOG) {
//            Logger.init();
//            Logger.e(msg);
        }
    }

    public static void i(String msg) {
        if (Constants.ISPRINTLOG) {
//            Logger.init();
//            Logger.i(msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (Constants.ISPRINTLOG) {
//            Logger.init();
//            Logger.i(msg);
        }
    }

    public static void v(String msg) {
        if (Constants.ISPRINTLOG) {
            Logger.init();
            Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if (Constants.ISPRINTLOG) {
//            Logger.init();
//            Logger.d(msg);
        }
    }

    public static void w(String msg) {
        if (Constants.ISPRINTLOG) {
//            Logger.init();
//            Logger.w(msg);
        }
    }

    /**
     * 打印json数据
     * @param msg
     */
    public static void json(String msg){
        if (Constants.ISPRINTLOG) {
           Log.e(tag + "Json=", msg);
        }
    }

    public static void netLog(Object msg){
        if (Constants.ISPRINTLOG) {
            Log.e(tag+"Net=",msg.toString());
        }
    }


    /*
    *Error:Execution failed for task ':app:dexDebug'.
> com.android.ide.common.process.ProcessException: org.gradle.process.internal.ExecException: Process 'command 'C:\Program Files\Java\jdk1.8.0_60\bin\java.exe'' finished with non-zero exit value 2
    * */
}
