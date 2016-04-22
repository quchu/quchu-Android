package co.quchu.quchu.net;

import co.quchu.quchu.BuildConfig;

/**
 * netApi
 */
public interface NetApi {
    String RELEASE_HOST = "http://www.quchu.co/app-main-service";
    String HOST_UAT = "http://uat.quchu.co/app-main-service";

    String HOST_SIT = "http://sit.quchu.co/app-main-service";

    String HOST = BuildConfig.API_SERVER == 0 ? RELEASE_HOST : BuildConfig.API_SERVER == 1 ? HOST_UAT : HOST_SIT;
    /****
     * Get start
     ****/
    String GetCircleList = HOST + "/place/GetCircleList";
    String GetCardList = HOST + "/personal/getCardList"; //获取我的明信片
    /**
     * URL:{host}/appservices/personal/getAlbum?accesstoke=075a849f43383e6f18daba73c229532f3b671a0d&pageno=1&type=favorite&orderby=new
     * type 数据获取类型(image(照片)/favorite(收藏)) 为空则为image
     * orderby 排序类型(hot/new) 为空则为hot
     */
    String GetImageAlbum = HOST + "/personal/getAlbum?accesstoken=%s&type=image&orderby=%s&pageno=%d"; //相册接口
    String GetFavoriteAlbum = HOST + "/personal/getAlbum?accesstoken=%s&type=favorite&orderby=%s&pageno=%d"; //相册接口
    String AlbumTypeHot = "hot";
    String AlbumTypeNew = "new";
    String GetCaptcha = HOST + "/mregister/getCaptcha?username=%s&method=%s"; //获取验证码 register=注册  reset=重置密码
    String WeiboLogin = HOST + "/oauth/checkWeibo?token=%s&openId=%s&equip=%s&type=login"; //微博注册/登录 token=微博返回的token openid= 微博返回的uid  equip=设备uuid
    String WeiboBind = HOST + "/oauth/checkWeibo"; //账号绑定微博   accesstoken=服务器返回的用户token
    String WechatLogin = HOST + "/oauth/checkWeixin?token=%s&openId=%s&equip=%s&type=login"; //微信注册登录   accesstoken=服务器返回的用户token
    String WechatBind = HOST + "/oauth/checkWeixin"; //微信绑定   accesstoken=服务器返回的用户token

    String UserStar = HOST + "/personal/getUserStar"; //我的趣星球
    String getMessageList = HOST + "/personal/getMessageList"; //消息中心

    String getRootTags = HOST + "/place/getRootTags?cityId=%d"; //获取趣处分类列表

    //  String getPlaceList = HOST + "/place/getPlaceList";//获取趣处 推荐列表
    String getPlaceList = HOST + "/place/getPlaceList?cityId=%d&tagsEn=%s&latitude=%s&longitude=%s&pageno=%d";//获取趣处 推荐列表按照分类
    // String getDefaultPlaceList = HOST + "/place/getPlaceList?cityId=%d&latitude=%s&longitude=%s&pageno=%d";//获取趣处 推荐列表
    String getDefaultPlaceList = HOST + "/place/getPlaceList?tagsEn=%s&cityId=%d&latitude=%s&longitude=%s&pageno=%d";// 1.1 根据tag获取趣处 列表
    String GetCityList = HOST + "/place/getCityList";  //获取城市列表  或验证城市
    String Seach = HOST + "/search?value=%s&pageno=%d";  //搜索 value=搜索内容  gageno=分页页码
    String getDetail = HOST + "/place/getPlace?pId=%d";  //获取趣处详情信息 pid=趣处id
    String getUserOutPlace = HOST + "/operate/userOutPlace?pId=%d";  //趣处详情 去过
    String delUserOutPlace = HOST + "/operate/delUserOutPlace?pId=%d";  //趣处详情 撤销去过
    String FavTypeImg = "image";
    String FavTypeArt = "article";
    String FavTypePlace = "place";
    String FavTypeCard = "card";
    String userFavorite = HOST + "/sns/favorite?formId=%d&type=%s";  //收藏 formId=423&type=image  formId=id  type=（image:图片,article:资讯,place:趣处,card: 明信片）
    String userDelFavorite = HOST + "/sns/delfavorite?formId=%d&type=%s";  //收藏 formId=423&type=image  formId=id  type=（image:图片,article:资讯,place:趣处,card: 明信片）
    String getFavorite = HOST + "/personal/getFavorite";  //获取我的收藏
    String getProposalPlaceList = HOST + "/personal/getProposalPlaceList?pageno=%d";  //获取我的发现
    String getFavoriteList = HOST + "/personal/getFavoriteList?pageno=%d&type=%s";  //获取我收藏的趣处/明信片

    String getPlaceCardList = HOST + "/place/getCardList?pageno=%d&pId=%s";  //趣处的明信片列表
    String getQiniuToken = HOST + "/operate/getQiniuToenk";  //获取七牛token

    String getCardDetail = HOST + "/place/getCard?cId=%d";  //获取明信片详情
    String delPostCard = HOST + "/operate/delCard?cId=%d";  //删除明信片

    String delPraise = HOST + "/sns/delpraise?formId=%d&type=%s";  //取消点赞
    String doPraise = HOST + "/sns/praise?formId=%d&type=%s";  //点赞 ·
    String delPostCardImage = HOST + "/place/delCardImg?imgId=%s";  //删除明信片照片

    String getPlaceUserCard = HOST + "/place/getPlaceUserCard?pId=%d";  //获取趣处中我留下的明信片
    String sharePlace = HOST + "/share/place?pId=%d";  //分享趣处
    String sharePostCard = HOST + "/share/card?cId=%d";  //分享明信片


    String getUserGene = HOST + "/personal/getUserGene";  //获取用户基因
    String userBehavior = HOST + "/push/userBehavior";  //用户数据采集

    String followFriends = HOST + "/sns/follow?followId=%d";  //关注他人
    String delFollowFriends = HOST + "/sns/delFollow?followId=%d";  //取消关注
    String getMyUserInfo = HOST + "/personal/getUser";  //获取自己用户信息
    String getUserInfo = HOST + "/personal/getUserInfo?userId=%d";  //获取自己用户信息
    String getUserCardList = HOST + "/place/getUserCardList";  //获取某个用户的明信片
    String getUsercenterFavoriteList = HOST + "/place/getFavoriteList?userId=%d&pageno=%d";  //获取某个用户收藏的趣处列表
    String getFollow = HOST + "/place/getFollow?userId=%d&pageno=%d&head=no&type=%s";  //获取某个用户收藏的趣处列表

    String getCurrentUserFollowers = HOST + "/personal/getFollow?head=%s&type=%s&pageno=%d";  //获取当前用户的关注列表


    String getCategoryTags = HOST + "/place/getCategoryTags?cityId=%d";  //获取tag 列表
    String getNearby = HOST + "/place/nearPlaces?cityId=%d&latitude=%s&longitude=%s&pagesNo=%d&recommendPlaceIds=%s&categoryTagIds=%s&isFirst=%d&placeId=%d";
    String getMapNearby = HOST + "/place/nearbyMapPlaces?name=%s&cityId=%d&latitude=%s&longitude=%s";
    String getFootprint = HOST + "/place/getCardList?pId=%d&pageno=%d";
    String getSearchResult = HOST + "/place/getSelectPlaces?cityId=%d&name=%d";
    String getVisitedUsers = HOST + "/place/userOut?placeId=%d";
    String getVisitorAnalysis = HOST + "/place/tagsCount?placeId=%d";

    /****  Get end  ****/

    /******************
     * POST
     *******************/
    String IsUnique = HOST + "/mregister/isUnique?username=%s&type=username"; //验证用户唯一性   type=username 验证手机号码  type=fullname  验证用户昵称 是否可用
    String Regiester = HOST + "/mregister?username=%s&password=%s&captcha=%s&regType=tel&equip=%s&fullname=%s"; //用户注册  username=PhoneNo  captcha =验证码 equip=uuid  fullname=NickName
    String Mlogin = HOST + "/login/android?j_username=%s&j_password=%s&equip=%s"; //用户登录  username=PhoneNo  captcha =验证码 equip=uuid
    String ResertPsw = HOST + "/mregister/resertPsw?resType=tel&tel=%s&newpsw=%s&captcha=%s"; //重置密码  username=PhoneNo  captcha =验证码 newpsw=密码
    String FeedBack = HOST + "/sns/feedback?value=%s"; //意见反馈
    String saveOrUpdateCard = HOST + "/operate/saveOrUpdateCard"; //意见反馈
    String updateUser = HOST + "/personal/updateUser"; //修改用户设置
    ///TODO
    String visitorRegiester = HOST + "/mregister?visitors=1&equip=%s";
    String checkIfForceUpdate = HOST + "/public/getIsUpdate";
    String findPosition = HOST + "/operate/proposalPlace?place.pId=%s&place.pname=%s&place.paddress=%s&place.profile=%s";//发现趣处

    String accoundMerger = HOST + "/oauth/userMerge";//账号合并
    String bindPhoneNumber = HOST + "/personal/bindPhoneNumber";//绑定手机号
    String unBindThrid = HOST + "/oauth/delbind";
    String getVersionInfo = HOST + "/public/getVersion";//查看版本信息
    String getVisitedInfo = HOST +"/operate/outView?placeId=%d";//获得评价内容
    String updateRatingInfo = HOST + "/operate/userOutPlace?pId=%d&tagIds=%s&score=%d";//更新评价
    String getFilterTags = HOST + "/public/getCategoryTags";//获得筛选标签
    /****************** POST *******************/
}
