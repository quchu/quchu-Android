package co.quchu.quchu.utils;

/**
 * Created by admin on 2016/3/16.
 */

/**
 * EventBus 通知Key凭据
 */
public class EventFlags {

    public static final int EVENT_APPLICATION_CHECK_UPDATE = 0x101;    //检查更新

    public static final int EVENT_LOGIN_ACTIVITY_SHOW_RETURN = 0x1001;    //登录界面显示返回
    public static final int EVENT_LOGIN_ACTIVITY_HIDE_RETURN = 0x1002;    //登录界面隐藏返回
    public static final int EVENT_FOOTPRINT_UPDATED = 0x0001;//修改脚印
    public static final int EVENT_POST_CARD_DELETED = 0x0002;
    public static final int EVENT_QUCHU_RATING_UPDATE = 0x0003;
    public static final int EVENT_POST_CARD_ADDED = 0x0004;
    public static final int EVENT_USER_LOGIN_SUCCESS = 0x10001;
    public static final int EVENT_USER_LOGOUT = 0x10002;
    public static final int EVENT_USER_INFO_UPDATE = 0x10003;
    public static final int EVENT_NEW_CITY_SELECTED = 0x11001;

    public static final int EVENT_GOTO_HOME_PAGE = 0x200001;
    public static final int EVENT_FINISH_MAP = 0x200002;
    public static final int EVENT_CANCLE_FAVORITE_QUCHU = 0x300001;

    public static final int EVENT_SWITCH_2_LOGIN = 0x400001;
    public static final int EVENT_LOCATION_UPDATED = 0x110001;


    public static final int EVENT_SCENE_FAVORITE = 0x50001;
    public static final int EVENT_SCENE_CANCEL_FAVORITE = 0x50002;


    public static final int EVENT_DEVICE_NETWORK_AVAILABLE = 0x600001;
    public static final int EVENT_DEVICE_NETWORK_CONNECTED_OR_CONNECTING = 0x600003;
    public static final int EVENT_DEVICE_NETWORK_UNAVAILABLE = 0x600002;

    public static final int EVENT_LOCATION_CHANGED = 0x700001;

}
