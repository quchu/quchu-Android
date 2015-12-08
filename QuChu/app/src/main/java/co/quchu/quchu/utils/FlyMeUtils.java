package co.quchu.quchu.utils;

import android.os.Build;

import java.lang.reflect.Method;

/**
 * FlyMeUtils
 * User: Chenhs
 * Date: 2015-12-08
 */
public class FlyMeUtils {

    /**
     * 判断当前系统是否为 魅族flyme
     *
     * @return true=flyme
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
