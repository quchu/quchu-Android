package co.quchu.quchu.base;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;

/**
 * AppLocationListener
 * User: Chenhs
 * Date: 2016-01-20
 */
public class AppLocationListener implements BDLocationListener {

  public static String currentCity;
  private static List<LocationListener> listeners;

  public static void addLocationListener(LocationListener amapLocation) {
    if (listeners == null) {
      listeners = new ArrayList<>();
    }
    listeners.add(amapLocation);
  }

  public static void removeListener(LocationListener listener) {
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

  @Override
  public void onReceiveLocation(BDLocation amapLocation) {
    if (amapLocation != null) {
      //定位成功回调信息，设置相关消息
//      amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//      amapLocation.getLatitude();//获取纬度
//      amapLocation.getLongitude();//获取经度
      SPUtils.setLatitude(amapLocation.getLatitude());
      SPUtils.setLongitude(amapLocation.getLongitude());
//      amapLocation.getAccuracy();//获取精度信息
//      amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//      amapLocation.getCountry();//国家信息
//      amapLocation.getProvince();//省信息
//      amapLocation.getCity();//城市信息
//      amapLocation.getDistrict();//城区信息
//      amapLocation.getStreet();//街道信息
//      amapLocation.getStreetNum();//街道门牌号信息
//      amapLocation.getCityCode();//城市编码
//      amapLocation.getAdCode();//地区编码
//      LogUtils.json("城市信息=" + amapLocation.getCity() + "///省信息=" + amapLocation.getProvince());
      currentCity = amapLocation.getCity();

      SPUtils.putValueToSPMap(AppContext.mContext, AppKey.LOCATION_CITY, amapLocation.getCity());
      SPUtils.putValueToSPMap(AppContext.mContext, AppKey.LOCATION_PROVINCE, amapLocation.getProvince());

      AppContext.stopLocation();

      if (listeners != null) {
        for (LocationListener item : listeners) {
          item.location(amapLocation);
        }
      }
    }
  }

  public interface LocationListener {
    void location(BDLocation amapLocation);
  }
}
