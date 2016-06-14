package co.quchu.quchu.utils;

import android.os.Environment;

import co.quchu.quchu.base.AppContext;

/**
 * FileUtils
 * User: Chenhs
 * Date: 2015-12-04
 */
public class FileUtils {
    public static String SDPATH = AppContext.mContext.getCacheDir().getAbsolutePath()
            + "/Quchu";

    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
