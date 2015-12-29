package co.quchu.quchu.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.WindowManager;

/**
 * AppUtil
 * User: Chenhs
 * Date: 2015-11-17
 */
public class AppUtil {

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    public static void printDeviceInf() {
        StringBuilder sb = new StringBuilder();
        sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
        sb.append("BOARD ").append(android.os.Build.BOARD).append("\n");
        sb.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER).append("\n");
        sb.append("BRAND ").append(android.os.Build.BRAND).append("\n");
        sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
        sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
        sb.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
        sb.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
        sb.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT).append("\n");
        sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
        sb.append("HOST ").append(android.os.Build.HOST).append("\n");
        sb.append("ID ").append(android.os.Build.ID).append("\n");
        sb.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER).append("\n");
        sb.append("MODEL ").append(android.os.Build.MODEL).append("\n");
        sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
        sb.append("RADIO ").append(android.os.Build.RADIO).append("\n");
        sb.append("SERIAL ").append(android.os.Build.SERIAL).append("\n");
        sb.append("TAGS ").append(android.os.Build.TAGS).append("\n");
        sb.append("TIME ").append(android.os.Build.TIME).append("\n");
        sb.append("TYPE ").append(android.os.Build.TYPE).append("\n");
        sb.append("USER ").append(android.os.Build.USER).append("\n");
        LogUtils.json(sb.toString());
    }
}
