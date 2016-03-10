package co.quchu.quchu.base;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;

/**
 * AppLocationListener
 * User: Chenhs
 * Date: 2016-01-20
 */
public class AppLocationListener implements AMapLocationListener {
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
              /*  amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度*/
                SPUtils.setLatitude( amapLocation.getLatitude());
                SPUtils.setLongitude( amapLocation.getLongitude());
           /*     amapLocation.getAccuracy();//获取精度信息
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                LogUtils.json("城市信息=" + amapLocation.getCity() + "///省信息=" + amapLocation.getProvince());*/
                SPUtils.putValueToSPMap(AppContext.mContext, AppKey.LOCATION_CITY, amapLocation.getCity());
                SPUtils.putValueToSPMap(AppContext.mContext, AppKey.LOCATION_PROVINCE, amapLocation.getProvince());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}