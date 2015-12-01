package co.quchu.quchu.net;

import co.quchu.quchu.base.Constants;

/**
 * netApi
 */
public class NetApi {
   // public static String DEBUG_HOST = "http://203.195.139.22:8080/appservices";
    public static String location_HOST = "http://192.168.1.134:8080/appservices";
      public static String DEBUG_HOST = "http://119.29.108.45:8080/appservices";
    public static String DEBUG_TOKEN = "8c6c7322163dc815c3f45c39c9f63dbb16460465";
    //    public static final String HOST = "http://www.paimeilv.com/appservices";
    public static final String HOST = Constants.ISDEBUG ? DEBUG_HOST : DEBUG_HOST;
    /****
     * Get start
     ****/
    public static final String GetCityList = HOST + "/place/getPlaceList";
    public static final String GetCircleList = HOST + "/place/GetCircleList";
    public static final String GetCardList = HOST + "/personal/getCardList?accesstoken=%s";
    /**
     * URL:{host}/appservices/personal/getAlbum?accesstoke=075a849f43383e6f18daba73c229532f3b671a0d&pageno=1&type=favorite&orderby=new
     * type 数据获取类型(image(照片)/favorite(收藏)) 为空则为image
     * orderby 排序类型(hot/new) 为空则为hot
     */
    public static final String GetImageAlbum = HOST + "/personal/getAlbum?accesstoken=%s&type=image&orderby=%s&pageno=%d"; //相册接口
    public static final String GetFavoriteAlbum = HOST + "/personal/getAlbum?accesstoken=%s&type=favorite&orderby=%s&pageno=%d"; //相册接口
    public static final String AlbumTypeHot = "hot";
    public static final String AlbumTypeNew = "new";
    public static final String GetCaptcha = HOST + "/mregister/getCaptcha?username=%s&method=%s"; //获取验证码 register=注册  reset=重置密码
    public static final String WeiboLogin = HOST + "/oauth/checkWeibo?token=%s&openId=%s&equip=%s&type=login"; //微博注册/登录 token=微博返回的token openid= 微博返回的uid  equip=设备uuid
    public static final String WeiboBind = HOST + "/oauth/checkWeibo?token=%s&openId=%s&equip=%s&type=bind&accesstoken=%s"; //账号绑定微博   accesstoken=服务器返回的用户token
    public static final String WechatLogin = HOST + "/oauth/checkWeixin?token=%s&openId=%s&equip=%s&type=login"; //微信注册登录   accesstoken=服务器返回的用户token
    public static final String WechatBind = HOST + "/oauth/checkWeixin?token=%s&openId=%s&equip=%s&type=bind&accesstoken=%s"; //微信绑定   accesstoken=服务器返回的用户token


    public static final String UserStar = HOST + "/personal/getUserStar"; //我的趣星球


    /****  Get end  ****/

    /******************
     * POST
     *******************/
    public static final String IsUnique = HOST + "/mregister/isUnique?username=%s&type=username"; //验证用户唯一性   type=username 验证手机号码  type=fullname  验证用户昵称 是否可用
    public static final String Regiester = HOST + "/mregister?username=%s&password=%s&captcha=%s&regType=tel&equip=%s&fullname=%s"; //用户注册  username=PhoneNo  captcha =验证码 equip=uuid  fullname=NickName
    public static final String Mlogin = HOST + "/login/android?j_username=%s&j_password=%s&equip=%s"; //用户登录  username=PhoneNo  captcha =验证码 equip=uuid
    public static final String ResertPsw = HOST + "/mregister/resertPsw?resType=tel&tel=%s&newpsw=%s&captcha=%s"; //重置密码  username=PhoneNo  captcha =验证码 newpsw=密码

    /****************** POST *******************/
}
