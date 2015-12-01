package co.quchu.quchu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SPUtils
 * User: Chenhs
 * Date: 2015-10-19
 * SharedPreferencesUtils 本地数据保存工具类
 */
public class SPUtils {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor edit;

    /**
     * 存储布尔值
     *
     * @param mContext
     * @param key
     * @param value
     */
    public static void putBooleanToSPMap(Context mContext, String key, boolean value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 获取布尔值
     *
     * @param mContext
     * @param key
     * @return
     */
    public static Boolean getBooleanFromSPMap(Context mContext, String key) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

    public static Boolean getBooleanFromSPMap(Context mContext, String key, boolean def) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, def);
        return value;
    }

    /**
     * 存储String
     *
     * @param mContext
     * @param key
     * @param value
     */
    public static void putValueToSPMap(Context mContext, String key, String value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 存储多个String
     *
     * @param mContext
     */
    public static void putValueToSPMap(Context mContext, Map<String, String> map) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        String value;
        for (String key : map.keySet()) {
            value = map.get(key);
            edit.putString(key, value);
        }
        edit.commit();
    }

    /**
     * 获取String
     *
     * @param mContext
     * @param key
     * @return value
     */
    public static String getValueFromSPMap(Context mContext, String key) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            String value = preferences.getString(key, "");
            return value;
        } else {
            return null;
        }
    }

    public static double getDoubleFromSPMap(Context mContext, String key) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        double value;
        value = (double) preferences.getFloat(key, 0f);
        return value;
    }

    /**
     * 获取String
     *
     * @param mContext
     * @param key
     * @param defaults 无值时取defaults
     * @return
     */

    public static String getValueFromSPMap(Context mContext, String key, String defaults) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            String value = preferences.getString(key, defaults);
            return value;
        } else {
            return null;
        }
    }

    /**
     * 保存int
     *
     * @param mContext
     */
    public static void putIntToSPMap(Context mContext, String key, int value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * 取 int
     *
     * @param mContext
     * @param key
     * @param defaults
     * @return
     */
    public static int getIntFromSPMap(Context mContext, String key, int defaults) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            int value = preferences.getInt(key, defaults);
            return value;
        } else {
            return 0;
        }
    }

    /**
     * 清除全部
     *
     * @param mContext
     */
    public static void clearSPMap(Context mContext) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 指定key清除
     *
     * @param mContext
     * @param key
     */
    public static void clearSpMap(Context mContext, String key) {
        putValueToSPMap(mContext, key, "");
    }

    public static String getUserToken(Context context) {
        return getValueFromSPMap(context, AppKey.USERTOKEN, "");
    }

    public static void setUserToken(Context context, String userToken) {
        putValueToSPMap(context, AppKey.USERTOKEN, userToken);
    }

    public static void setUserInfo(Context context, String userToken) {
        putValueToSPMap(context, AppKey.USERINFO, userToken);
    }

    public static String getUserInfo(Context context) {
        return getValueFromSPMap(context, AppKey.USERINFO, "");
    }

    public static void cliearUserinfo(Context mContext) {
        putValueToSPMap(mContext, AppKey.USERINFO, "");
        putValueToSPMap(mContext, AppKey.USERTOKEN, "");
    }
}
