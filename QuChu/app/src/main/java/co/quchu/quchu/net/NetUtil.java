package co.quchu.quchu.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import co.quchu.quchu.utils.StringUtils;


public class NetUtil {
	
	public static final int NETTYPE_WIFI = 0x0001;
	public static final int NETTYPE_CMWAP = 0x0002;
	public static final int NETTYPE_CMNET = 0x0003;
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public static boolean isNetworkConnected(Context mContext) {
		final ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return wifi.isConnected() || mobile.isConnected();
	}
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public static int getNetworkType(Context mContext) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
}
