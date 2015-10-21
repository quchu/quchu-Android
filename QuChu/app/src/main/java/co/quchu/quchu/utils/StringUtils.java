package co.quchu.quchu.utils;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import co.quchu.quchu.base.AppContext;

/**
 * StringUtils
 * User: Chenhs
 * Date: 2015-10-19
 * 字符串工具类
 */
public class StringUtils {

    public static boolean isFile(String path) {

        return new File(path).isFile();
    }

    public static String getRealPath() {
        String sdDir = null;
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Rehu/";//获取跟目录
            File src = new File(sdDir);
            if (!src.exists()) {
                src.mkdirs();
            }
            return sdDir;
        } else {
            File src = new File("/sdcard/Rehu/");
            if (!src.exists()) {
                src.mkdirs();
            }
            return src.getAbsolutePath();
        }
    }

    public static String getRealPath(String fileName) {
        return getRealPath() + fileName;
    }

    /**
     * json 本地保存
     *
     * @param response
     */
    public static void SaveLoca(String response) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file_name = null;
            try {
                file_name = new File(getRealPath() + "Rehu.txt");
                if (!file_name.exists()) {
                    File dir = new File(file_name.getParent());
                    dir.mkdirs();
                    file_name.createNewFile();
                }
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
            try {
                final OutputStream os = new FileOutputStream(file_name);
                os.write(response.getBytes());
                os.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     *获取设备uuid
     * @return
     */
    public static String getMyUUID() {
        String MyUUID = SPUtils.getValueFromSPMap(AppContext.mContext,
                AppKey.UUID);
        if (StringUtils.isEmpty(MyUUID)) {
            TelephonyManager tm = (TelephonyManager) AppContext.mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, tmPhone, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    AppContext.mContext.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            SPUtils.putValueToSPMap(AppContext.mContext, AppKey.UUID, uniqueId);
            return uniqueId;
        } else {
            return MyUUID;
        }
    }

    /**
     * 判断字符串是否为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
