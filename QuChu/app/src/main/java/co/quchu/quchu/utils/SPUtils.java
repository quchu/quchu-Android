package co.quchu.quchu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import co.quchu.quchu.base.AppContext;

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
     */
    public static void putBooleanToSPMap(Context mContext, String key, boolean value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }


    /**
     * 获取布尔值
     */
    public static Boolean getBooleanFromSPMap(Context mContext, String key) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static Boolean getBooleanFromSPMap(Context mContext, String key, boolean def) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, def);
    }

    /**
     * 存储String
     */
    public static void putValueToSPMap(Context mContext, String key, String value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * 存储多个String
     */
    public static void putValueToSPMap(Context mContext, Map<String, String> map) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        String value;
        for (String key : map.keySet()) {
            value = map.get(key);
            edit.putString(key, value);
        }
        edit.apply();
    }

    /**
     * 获取String
     */
    public static String getValueFromSPMap(Context mContext, String key) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            return preferences.getString(key, "");
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
     */

    public static String getValueFromSPMap(Context mContext, String key, String defaults) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            return preferences.getString(key, defaults);
        } else {
            return null;
        }
    }

    /**
     * 保存int
     */
    public static void putIntToSPMap(Context mContext, String key, int value) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 取 int
     */
    public static int getIntFromSPMap(Context mContext, String key, int defaults) {
        if (null != mContext) {
            preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
            return preferences.getInt(key, defaults);
        } else {
            return 0;
        }
    }

    /**
     * 清除全部
     */
    public static void clearSPMap(Context mContext) {
        preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
        edit = preferences.edit();
        edit.clear();
        edit.apply();
    }

    /**
     * 指定key清除
     */
    public static void clearSpMap(Context mContext, String key) {
        putValueToSPMap(mContext, key, "");
    }

    public static String getUserToken(Context context) {
        return getValueFromSPMap(context, AppKey.USERTOKEN, "");
    }


    public static boolean animationShown(Context context) {
        if (!getBooleanFromSPMap(context, AppKey.IS_LANDING_ANIMATION,false)){
            return true;
        }else{
            return false;
        }

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

    public static void clearUserinfo(Context mContext) {
        putValueToSPMap(mContext, AppKey.USERINFO, "");
        putValueToSPMap(mContext, AppKey.USERTOKEN, "");
    }

    public static void setCityId(int cityId) {
        putValueToSPMap(AppContext.mContext, AppKey.CITYID, String.valueOf(cityId));
    }

    public static int getCityId() {
        return Integer.parseInt(getValueFromSPMap(AppContext.mContext, AppKey.CITYID, "5"));
    }

    public static void setCityName(String cityId) {
        putValueToSPMap(AppContext.mContext, AppKey.CITYNAME, cityId);
    }

    public static String getCityName() {
        return getValueFromSPMap(AppContext.mContext, AppKey.CITYNAME, " ");
    }

    public static void setLongitude(double cityId) {
        putValueToSPMap(AppContext.mContext, AppKey.LON, String.valueOf(cityId));
    }

    public static double getLongitude() {
        return Double.parseDouble(getValueFromSPMap(AppContext.mContext, AppKey.LON, "0"));
    }

    public static void setLatitude(double cityId) {
        putValueToSPMap(AppContext.mContext, AppKey.LAT, String.valueOf(cityId));
    }

    public static double getLatitude() {
        return Double.parseDouble(getValueFromSPMap(AppContext.mContext, AppKey.LAT, "0"));
    }


    /**
     * 初始化 引导页 标志
     */
    public static void initGuideIndex() {
        putBooleanToSPMap(AppContext.mContext, AppKey.IS_POSTCARD_IMAGES_GUIDE, true);
        putBooleanToSPMap(AppContext.mContext, AppKey.IS_POSTCARD_GUIDE, true);
        putBooleanToSPMap(AppContext.mContext, AppKey.IS_PLANET_GUIDE, true);

    }
}



