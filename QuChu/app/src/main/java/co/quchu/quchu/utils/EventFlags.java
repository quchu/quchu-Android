package co.quchu.quchu.utils;

/**
 * Created by admin on 2016/3/16.
 */

/**
 * EventBus 通知Key凭据
 */
public class EventFlags {
    public static final int EVENT_FOOTPRINT_UPDATED = 0x0001;//修改脚印
    public static final int EVENT_POST_CARD_DELETED = 0x0002;
    public static final int EVENT_QUCHU_RATING_UPDATE = 0x0003;
    public static final int EVENT_POST_CARD_ADDED = 0x0004;
    public static final int EVENT_USER_LOGIN_SUCCESS = 0x10001;
    public static final int EVENT_NEW_CITY_SELECTED = 0x11001;

    public static final int EVENT_FINISH_THIS = 0x200001;
    public static final int EVENT_FINISH_MAP = 0x200002;
    public static final int EVENT_CANCLE_FAVORITE_QUCHU = 0x300001;

    public static final int EVENT_SWITCH_2_LOGIN = 0x400001;
}
