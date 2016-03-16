package co.quchu.quchu.net;

import co.quchu.quchu.base.Constants;

/**
 * netApi
 */
public class NetApi {
    // public static boolean isDebug = false;
    // public static String DEBUG_HOST = "http://203.195.139.22:8080/appservices";
    public static String RELEASE_HOST = "http://www.quchu.co/appservices";
    public static String HOST_UAT = "http://uat.quchu.co:8080/appservices";
    public static String HOST_SIT = "http://sit.quchu.co:8080/appservices";
//    public static String HOST_SIT = "http://192.168.1.134:8080/appservices";

    //  public static String DEBUG_HOST = "http://119.29.108.45:8080/appservices";
    //    public static final String HOST = "http://www.paimeilv.com/appservices";
    //public static final String HOST = location_HOST;
    public static final String HOST = Constants.ISDEBUG == 0 ? RELEASE_HOST : Constants.ISDEBUG == 1 ? HOST_UAT : HOST_SIT;
    /****
     * Get start
     ****/
    public static final String GetCircleList = HOST + "/place/GetCircleList";
    public static final String GetCardList = HOST + "/personal/getCardList"; //获取我的明信片
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
    public static final String getMessageList = HOST + "/personal/getMessageList"; //消息中心

    public static final String getRootTags = HOST + "/place/getRootTags"; //获取趣处分类列表

    // public static final String getPlaceList = HOST + "/place/getPlaceList";//获取趣处 推荐列表
    public static final String getPlaceList = HOST + "/place/getPlaceList?cityId=%d&tagsEn=%s&latitude=%s&longitude=%s&pageno=%d";//获取趣处 推荐列表按照分类
    //public static final String getDefaultPlaceList = HOST + "/place/getPlaceList?cityId=%d&latitude=%s&longitude=%s&pageno=%d";//获取趣处 推荐列表
    public static final String getDefaultPlaceList = HOST + "/place/getPlaceList?tagsEn=%s&cityId=%d&latitude=%s&longitude=%s&pageno=%d";// 1.1 根据tag获取趣处 列表
    public static final String GetCityList = HOST + "/place/getCityList";  //获取城市列表  或验证城市
    public static final String Seach = HOST + "/search?value=%s&pageno=%d";  //搜索 value=搜索内容  gageno=分页页码
    public static final String getDetail = HOST + "/place/GetPlace?pId=%d";  //获取趣处详情信息 pid=趣处id
    public static final String getUserOutPlace = HOST + "/operate/userOutPlace?pId=%d";  //趣处详情 去过
    public static final String delUserOutPlace = HOST + "/operate/delUserOutPlace?pId=%d";  //趣处详情 撤销去过
    public static final String FavTypeImg = "image";
    public static final String FavTypeArt = "article";
    public static final String FavTypePlace = "place";
    public static final String FavTypeCard = "card";
    public static final String userFavorite = HOST + "/sns/favorite?formId=%d&type=%s";  //收藏 formId=423&type=image  formId=id  type=（image:图片,article:资讯,place:趣处,card: 明信片）
    public static final String userDelFavorite = HOST + "/sns/delfavorite?formId=%d&type=%s";  //收藏 formId=423&type=image  formId=id  type=（image:图片,article:资讯,place:趣处,card: 明信片）
    public static final String getFavorite = HOST + "/personal/getFavorite";  //获取我的收藏
    public static final String getProposalPlaceList = HOST + "/personal/getProposalPlaceList?pageno=%d";  //获取我的发现
    public static final String getFavoriteList = HOST + "/personal/getFavoriteList?pageno=%d&type=%s";  //获取我收藏的趣处/明信片

    public static final String getPlaceCardList = HOST + "/place/getCardList?pageno=%d&pId=%s";  //趣处的明信片列表
    public static final String getQiniuToken = HOST + "/operate/getQiniuToenk";  //获取七牛token

    public static final String getCardDetail = HOST + "/place/getCard?cId=%d";  //获取明信片详情
    public static final String delPostCard = HOST + "/operate/delCard?cId=%d";  //删除明信片

    public static final String delPraise = HOST + "/sns/delpraise?formId=%d&type=%s";  //取消点赞
    public static final String doPraise = HOST + "/sns/praise?formId=%d&type=%s";  //点赞 ·
    public static final String delPostCardImage = HOST + "/place/delCardImg?imgId=%s";  //删除明信片照片

    public static final String getPlaceUserCard = HOST + "/place/getPlaceUserCard?pId=%d";  //获取趣处中我留下的明信片
    public static final String sharePlace = HOST + "/share/place?pId=%d";  //分享趣处
    public static final String sharePostCard = HOST + "/share/card?cId=%d";  //分享明信片


    public static final String getUserGene = HOST + "/personal/getUserGene";  //获取用户基因
    public static final String userBehavior = HOST + "/push/userBehavior";  //用户数据采集

    public static final String followFriends = HOST + "/sns/follow?followId=%d";  //关注他人
    public static final String delFollowFriends = HOST + "/sns/delFollow?followId=%d";  //取消关注
    public static final String getMyUserInfo = HOST + "/personal/getUser";  //获取自己用户信息
    public static final String getUserInfo = HOST + "/personal/getUserInfo?userId=%d";  //获取自己用户信息
    public static final String getUserCardList = HOST + "/place/getUserCardList?userId=%d&pageno=%d";  //获取某个用户的明信片
    public static final String getUsercenterFavoriteList = HOST + "/place/getFavoriteList?userId=%d&pageno=%d";  //获取某个用户收藏的趣处列表
    public static final String getFollow = HOST + "/place/getFollow?userId=%d&pageno=%d&head=no&type=%s";  //获取某个用户收藏的趣处列表


    public static final String getCategoryTags = HOST + "/place/getCategoryTags";  //获取tag 列表

    /****  Get end  ****/

    /******************
     * POST
     *******************/
    public static final String IsUnique = HOST + "/mregister/isUnique?username=%s&type=username"; //验证用户唯一性   type=username 验证手机号码  type=fullname  验证用户昵称 是否可用
    public static final String Regiester = HOST + "/mregister?username=%s&password=%s&captcha=%s&regType=tel&equip=%s&fullname=%s"; //用户注册  username=PhoneNo  captcha =验证码 equip=uuid  fullname=NickName
    public static final String Mlogin = HOST + "/login/android?j_username=%s&j_password=%s&equip=%s"; //用户登录  username=PhoneNo  captcha =验证码 equip=uuid
    public static final String ResertPsw = HOST + "/mregister/resertPsw?resType=tel&tel=%s&newpsw=%s&captcha=%s"; //重置密码  username=PhoneNo  captcha =验证码 newpsw=密码
    public static final String FeedBack = HOST + "/sns/feedback?value=%s"; //意见反馈
    public static final String saveOrUpdateCard = HOST + "/operate/saveOrUpdateCard"; //意见反馈
    public static final String updateUser = HOST + "/personal/updateUser"; //修改用户设置
    public static final String VisitorRegiester = HOST + "/mregister?visitors=1&equip=%s";
    public static final String findPosition = HOST + "/operate/proposalPlace?place.pId=%s&place.pname=%s&place.paddress=%s&place.profile=%s&place.pimage=%s";//发现趣处


    /****************** POST *******************/
}
