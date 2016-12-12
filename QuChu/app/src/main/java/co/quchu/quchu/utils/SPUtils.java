package co.quchu.quchu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.UserInfoModel;

/**
 * SPUtils
 * User: Chenhs
 * Date: 2015-10-19
 * SharedPreferencesUtils 本地数据保存工具类
 */
public class SPUtils {
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor edit;

  public static final String LOGIN_TYPE_WEIXIN = "weixin";
  public static final String LOGIN_TYPE_WEIBO = "weibo";
  public static final String LOGIN_TYPE_PHONE = "phone";

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
    edit.commit();
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

  public static boolean getForceUpdateIfNecessary(Context context) {
    PackageInfo pInfo = null;
    try {
      pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    return preferences.getBoolean(AppKey.SPF_KEY_FORCE_UPDATE + pInfo.versionName, false);
  }

  public static void setForceUpdateIfNecessary(Context context, boolean forceUpdate) {
    PackageInfo pInfo = null;
    try {
      pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    preferences.edit().putBoolean(AppKey.SPF_KEY_FORCE_UPDATE + pInfo.versionName, forceUpdate)
        .commit();
  }

  public static String getForceUpdateReason(Context context) {
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    return preferences.getString(AppKey.SPF_KEY_FORCE_UPDATE_REASON, "");
  }

  public static void setForceUpdateReason(Context context, String reason) {
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    preferences.edit().putString(AppKey.SPF_KEY_FORCE_UPDATE_REASON, reason).commit();
  }

  public static String getForceUpdateUrl(Context context) {
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    return preferences.getString(AppKey.SPF_KEY_FORCE_UPDATE_URL, "");
  }

  public static void setForceUpdateUrl(Context context, String reason) {
    preferences = context.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    preferences.edit().putString(AppKey.SPF_KEY_FORCE_UPDATE_URL, reason).commit();
  }

  /**
   * 获取String
   */

  public static String getValueFromSPMap(Context mContext, String key, String defaults) {
    preferences = mContext.getSharedPreferences(AppKey.APPINFO, Context.MODE_PRIVATE);
    return preferences.getString(key, defaults);
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

  public static void setUserToken(Context context, String userToken) {
    putValueToSPMap(context, AppKey.USERTOKEN, userToken);
  }

  public static void setUserInfo(Context context, String userToken) {
    putValueToSPMap(context, AppKey.USERINFO, userToken);
    AppContext.user = new Gson().fromJson(userToken, UserInfoModel.class);
  }

  public static String getUserInfo(Context context) {
    return getValueFromSPMap(context, AppKey.USERINFO, "");
  }

  public static void clearUserinfo(Context mContext) {
    putValueToSPMap(mContext, AppKey.USERTOKEN, "");
  }

  public static void setCityId(int cityId) {
    putValueToSPMap(AppContext.mContext, AppKey.CITYID, String.valueOf(cityId));
  }

  public static int getCityId() {
    return Integer.parseInt(getValueFromSPMap(AppContext.mContext, AppKey.CITYID, "1"));
  }

  public static void setCityName(String cityId) {
    putValueToSPMap(AppContext.mContext, AppKey.CITYNAME, cityId);
  }

  public static String getCityName() {
    return getValueFromSPMap(AppContext.mContext, AppKey.CITYNAME, "厦门");
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

  public static boolean getConversationGuide() {
    boolean show = getBooleanFromSPMap(AppContext.mContext, AppKey.SHOW_AI_CONVERSATION_GUIDE, false);
    putBooleanToSPMap(AppContext.mContext, AppKey.SHOW_AI_CONVERSATION_GUIDE, true);
    return show;
  }


  public static boolean getShowGuild() {
    boolean show = getBooleanFromSPMap(AppContext.mContext, AppKey.DISPLAY_GUILD, false);
    putBooleanToSPMap(AppContext.mContext, AppKey.DISPLAY_GUILD, true);
    return show;
  }

  //    /**
  //     * 初始化 引导页 标志
  //     */
  //    public static void initGuideIndex() {
  //        putBooleanToSPMap(AppContext.mContext, AppKey.IS_POSTCARD_IMAGES_GUIDE, true);
  //        putBooleanToSPMap(AppContext.mContext, AppKey.IS_POSTCARD_GUIDE, true);
  //        putBooleanToSPMap(AppContext.mContext, AppKey.IS_PLANET_GUIDE, true);
  //
  //    }

  public static String getLoginType() {
    return getValueFromSPMap(AppContext.mContext, AppKey.LOGIN_TYPE);
  }

  public static void putLoginType(String type) {
    putValueToSPMap(AppContext.mContext, AppKey.LOGIN_TYPE, type);
  }

  /**
   * 搭伙开关
   */
  public static void setDahuoSwitch(boolean checked) {
    putBooleanToSPMap(AppContext.mContext, AppKey.DAHUO_SWITCH, checked);
  }

  public static boolean getDahuoSwitch() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.DAHUO_SWITCH, true);
  }

  /**
   * 搭伙开关
   */
  public static void setEnableSound(boolean checked) {
    putBooleanToSPMap(AppContext.mContext, AppKey.ENABLE_SOUND, checked);
  }

  public static boolean isEnableSound() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.ENABLE_SOUND, true);
  }

  /**
   * 推荐文章
   */
  public static void setNewsSwitch(boolean checked) {
    putBooleanToSPMap(AppContext.mContext, AppKey.NEWS_SWITCH, checked);
  }

  public static boolean getNewsSwitch() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.NEWS_SWITCH, true);
  }

  /**
   * 推荐趣处
   */
  public static void setQuchuSwitch(boolean checked) {
    putBooleanToSPMap(AppContext.mContext, AppKey.QUCHU_SWITCH, checked);
  }

  public static boolean getQuchuSwitch() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.QUCHU_SWITCH, true);
  }

  /**
   * 推荐趣星人
   */
  public static void setQuchuUserSwitch(boolean checked) {
    putBooleanToSPMap(AppContext.mContext, AppKey.QUCHU_USER_SWITCH, checked);
  }

  public static boolean getQuchuUserSwitch() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.QUCHU_USER_SWITCH, true);
  }

  /**
   * user mark
   */
  public static void setUserMark(String mark) {
    putValueToSPMap(AppContext.mContext, AppKey.USER_MARK, mark);
  }

  public static String getUserMark() {
    return getValueFromSPMap(AppContext.mContext, AppKey.USER_MARK, null);
  }

  /**
   * 保存最近一条反馈
   */
  public static void setFeedback(String title, String content) {
    putValueToSPMap(AppContext.mContext, AppKey.FEEDBACK_TITLE, title);
    putValueToSPMap(AppContext.mContext, AppKey.FEEDBACK_CONTENT, content);
  }

  public static String getFeedback() {
    String title = getValueFromSPMap(AppContext.mContext, AppKey.FEEDBACK_TITLE, "");
    String content = getValueFromSPMap(AppContext.mContext, AppKey.FEEDBACK_CONTENT, "");
    return title + "/" + content;
  }

  /**
   * 保存融云token
   */
  public static void setRongYunToken(String rongYunToken) {
    putValueToSPMap(AppContext.mContext, AppKey.RONGYUN_TOKEN, rongYunToken);
  }

  public static String getRongYunToken() {
    return getValueFromSPMap(AppContext.mContext, AppKey.RONGYUN_TOKEN, "");
  }

  /**
   * 保存融云私聊的目标id
   */
  public static void setRongYunTargetId(String targetId) {
    putValueToSPMap(AppContext.mContext, AppKey.TARGET_ID, targetId);
  }

  public static String getRongYunTargetId() {
    return getValueFromSPMap(AppContext.mContext, AppKey.TARGET_ID, "");
  }

  /**
   * 保存融云私聊标题
   */
  public static void setRongYunTitle(String title) {
    putValueToSPMap(AppContext.mContext, AppKey.CHAT_TITLE, title);
  }

  public static String getRongYunTitle() {
    return getValueFromSPMap(AppContext.mContext, AppKey.CHAT_TITLE, "");
  }

  /**
   * 保存是否有个推消息
   */
  public static void setHasPushMsg(boolean hasMsg) {
    putBooleanToSPMap(AppContext.mContext, AppKey.PUSH_MESSAGE, hasMsg);
  }

  public static boolean getHasPushMsg() {
    return getBooleanFromSPMap(AppContext.mContext, AppKey.PUSH_MESSAGE, false);
  }
}



