package co.quchu.quchu.net;

import co.quchu.quchu.base.Constants;

/**
 * netApi
 */
public class NetApi {
    public static String DEBUG_HOST = "http://203.195.139.22:8080/appservices";
    public static String DEBUG_TOKEN = "8c6c7322163dc815c3f45c39c9f63dbb16460465";
    //    public static final String HOST = "http://www.paimeilv.com/appservices";
    public static final String HOST = Constants.ISDEBUG? DEBUG_HOST:DEBUG_HOST;
    /****
     * Get start
     ****/
    public static final String GetCityList = HOST + "/place/getPlaceList";
    public static final String GetCircleList = HOST + "/place/GetCircleList";
    /****  Get end  ****/
}
