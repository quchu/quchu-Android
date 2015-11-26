package co.quchu.quchu.net;

import co.quchu.quchu.base.Constants;

/**
 * netApi
 */
public class NetApi {
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
    /****  Get end  ****/

    /******************POST*******************/
  /*  public static final String IsUnique = HOST + "/sns/feedback";*/
    public static final String IsUnique = HOST + "/mregister/isUnique?username=%s&type=fullname";

    /****************** POST *******************/
}
