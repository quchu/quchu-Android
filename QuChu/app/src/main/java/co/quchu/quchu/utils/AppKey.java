package co.quchu.quchu.utils;

/**
 * AppKey
 * User: Chenhs
 * Date: 2015-10-19
 * SharedPreferencesUtils Key汇总
 */
public class AppKey {
    /**
     * sharedPreference 文件名
     */
    public static final String APPINFO = "QuChu";
    /**
     * 设备uuid
     */
    public static final String UUID = "device_id";
    /**
     * 令牌
     */
    public static final String USERTOKEN = "user_token";

    /**
     * 用户信息
     */
    public static final String USERINFO = "user_info";

    /**
     * 用户选中分类
     */
    public static final String USERSELECTEDCLASSIFY = "UserSelectedClassify";
    /**
     * 用户选中分类中文名
     */
    public static final String USERSELECTEDCLASSIFY_CHS = "UserSelectedClassify_Chs";
    /**
     * 当前用户选中的城市id
     * 或默认的
     */
    public static final String CITYID = "cityId";
    /**
     * 定位后获得的城市名称
     */
    public static final String CITYNAME = "cityName";
    /**
     * 定位后获得的 纬度
     */
    public static final String LAT = "latitude";
    /**
     * 定位后获得的 经度
     */
    public static final String LON = "longitude";
    /**
     * 搜索历史
     */
    public static final String SEARCHHISTORY = "search_history";
    /**
     * 是否需要创建桌面快捷方式
     */
    public static final String IS_NEED_ICON = "is_need_icon";
    /**
     * 我的趣星球界面是否需要弹出引导页
     */
    public static final String IS_PLANET_GUIDE = "is_planet_guide";
    /**
     * 我的明信片界面是否需要弹出引导页
     */
    public static final String IS_POSTCARD_GUIDE = "is_postcard_guide";
    /**
     * 我的明信片照片界面是否需要弹出引导页
     */
    public static final String IS_POSTCARD_IMAGES_GUIDE = "is_postcard_images_guide";
}
