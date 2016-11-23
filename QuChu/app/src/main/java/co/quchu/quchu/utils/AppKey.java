package co.quchu.quchu.utils;

/**
 * AppKey
 * User: Chenhs
 * Date: 2015-10-19
 * SharedPreferencesUtils Key汇总
 */
public interface AppKey {
  /**
   * sharedPreference 文件名
   */
  String APPINFO = "QuChu";
  /**
   * 设备uuid
   */
  String UUID = "device_id";
  /**
   * 令牌
   */
  String USERTOKEN = "user_token";

  /**
   * 用户信息
   */
  String USERINFO = "user_info";

  /**
   * 用户选中分类
   */
  String USERSELECTEDCLASSIFY = "UserSelectedClassify";
  /**
   * 用户选中分类中文名
   */
  String USERSELECTEDCLASSIFY_CHS = "UserSelectedClassify_Chs";
  /**
   * 当前用户选中的城市id
   * 或默认的
   */
  String CITYID = "cityId";
  /**
   * 定位后获得的城市名称
   */
  String CITYNAME = "cityName";
  /**
   * 定位后获得的 纬度
   */
  String LAT = "latitude";
  /**
   * 定位后获得的 经度
   */
  String LON = "longitude";
  /**
   * 定位后获得的 城市
   */
  String LOCATION_CITY = "location_city";
  /**
   * 定位后获得的 省信息
   */
  String LOCATION_PROVINCE = "location_Province";
  /**
   * 搜索历史
   */
  String SEARCHHISTORY = "search_history";
  /**
   * 是否需要创建桌面快捷方式
   */
  String IS_NEED_ICON = "is_need_icon";
  /**
   * 我的趣星球界面是否需要弹出引导页
   */
  String IS_PLANET_GUIDE = "is_planet_guide";
  /**
   * 落地页动画
   */
  String IS_LANDING_ANIMATION = "is_landing_animation";
  /**
   * 我的明信片界面是否需要弹出引导页
   */
  String IS_POSTCARD_GUIDE = "is_postcard_guide";

  /**
   * 推荐页引导
   */
  String SHOW_AI_CONVERSATION_GUIDE = "SHOW_AI_CONVERSATION_GUIDE";

  /**
   * 首页向导
   */
  String DISPLAY_GUILD = "DISPLAY_GUILD";

  /**
   * 我的明信片照片界面是否需要弹出引导页
   */
  String IS_POSTCARD_IMAGES_GUIDE = "is_postcard_images_guide";
  /**
   * 趣处明信片列表是否需要刷新
   */
  String IS_POSTCARD_LIST_NEED_REFRESH = "is_postcard_list_need_refresh";
  /**
   * 菜单页是否需要刷新
   */
  String IS_MENU_NEED_REFRESH = "is_menu_need_refresh";

  /**
   * 是否强制更新
   */
  String SPF_KEY_FORCE_UPDATE = "SPF_KEY_FORCE_UPDATE";

  /**
   * 强制更新说明
   */
  String SPF_KEY_FORCE_UPDATE_REASON = "SPF_KEY_FORCE_UPDATE_REASON";
  /**
   * 强制更新地址
   */
  String SPF_KEY_FORCE_UPDATE_URL = "SPF_KEY_FORCE_UPDATE_URL";
  /**
   * 强制更新前版本
   */
  String SPF_KEY_FORCE_UPDATE_VERSION_NAME = "SPF_KEY_FORCE_UPDATE_VERSION_NAME";

  /**
   * 当前登陆的类型 微信 手机 游客 微博
   */
  String LOGIN_TYPE = "login_type";
  /**
   * 滑动删除提示
   */
  String SPF_KEY_SWIPE_DELETE_PROMPT_FIND = "swipeDeletePrompt";
  String SPF_KEY_SWIPE_DELETE_PROMPT_FAVORITE_QUCHU = "swipeDeletePrompt_quchu";
  String SPF_KEY_SWIPE_DELETE_PROMPT_FAVORITE_ESSAY = "swipeDeletePrompt_essay";

  /**
   * 当前用户是否开启搭伙功能
   * 或默认的
   */
  String DAHUO_SWITCH = "dahuo_switch";

  /**
   * 推荐文章
   */
  String NEWS_SWITCH = "news_switch";

  /**
   * 推荐趣处
   */
  String QUCHU_SWITCH = "quchu_switch";

  /**
   * 推荐趣星人
   */
  String QUCHU_USER_SWITCH = "quchu_user_switch";

  /**
   * mark
   */
  String USER_MARK = "user_mark";

  /**
   * 最近一条反馈
   */
  String FEEDBACK_TITLE = "feedback_title";

  String FEEDBACK_CONTENT = "feedback_content";

  /**
   * 融云token
   */
  String RONGYUN_TOKEN = "rongyun_token";

  /**
   * 融云私聊目标id
   */
  String TARGET_ID = "target_id";

  /**
   * 融云私聊title
   */
  String CHAT_TITLE = "chat_title";

  /**
   * 个推未读消息
   */
  String PUSH_MESSAGE = "push_message";
}
