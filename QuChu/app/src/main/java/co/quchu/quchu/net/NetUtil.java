package co.quchu.quchu.net;

import android.content.Context;
import android.net.ConnectivityManager;


public class NetUtil {

    public static final int NETTYPE_WIFI = 0x0001;
    public static final int NETTYPE_CMWAP = 0x0002;
    public static final int NETTYPE_CMNET = 0x0003;

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context mContext) {
        final ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return null != connMgr.getActiveNetworkInfo();

//        final android.net.NetworkInfo wifi = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        final android.net.NetworkInfo mobile = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        return wifi.isAvailable() || mobile.isAvailable();
    }
}
