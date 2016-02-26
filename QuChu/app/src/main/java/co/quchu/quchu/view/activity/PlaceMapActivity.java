package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.umeng.analytics.MobclickAgent;

import java.net.URISyntaxException;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.NavigateSelectedDialogFg;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by Administrator on 2016/1/24.
 * <p/>
 * 34:94:5B:1E:43:1A:A3:0A:FE:BB:D8:59:B1:2E:73:73:10:16:E8:D1
 * <p/>
 * 28:EB:11:F4:F3:47:22:CC:A2:08:DE:8E:75:B5:3D:DD:97:8F:2C:C4正式环境
 * <p/>
 * 地图 点击可以导航界面
 */
public class PlaceMapActivity extends BaseActivity implements View.OnClickListener, LocationSource, AMap.OnMapLoadedListener,
        AMapLocationListener {
    MapView mapView;
    RelativeLayout about_us_title_back_rl;
    private OnLocationChangedListener mListener;
    private AMap aMap;
    private AMapLocationClient mlocationClient;

    double lat = 0, lont = 0, gdlon = 0, gdlat = 0;
    String placeTitle, placeAddressStr = "";
    LatLng placeAddress;
    LatLng myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);
        mapView = (MapView) findViewById(R.id.place_map_mv);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        TextView title_right_navigate_tv = (TextView) findViewById(R.id.title_right_navigate_tv);
        about_us_title_back_rl = (RelativeLayout) findViewById(R.id.about_us_title_back_rl);
        about_us_title_back_rl.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        initData();
        title_right_navigate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateSelectedDialogFg navigateDialogFg = NavigateSelectedDialogFg.newInstance();
                navigateDialogFg.setNavigateClickListener(new NavigateSelectedDialogFg.NavigateClickListener() {
                    @Override
                    public void choiceGd() {
                        jump2Amap();
                    }

                    @Override
                    public void choiceBd() {
                        jump2BaiduMap();
                    }

                    @Override
                    public void choiceTx() {
                        jump2TencentMap();
                    }
                });
                navigateDialogFg.show(getFragmentManager(), "navigate");
            }
        });
    }

    private void initData() {
        if (StringUtils.isDouble(getIntent().getStringExtra("lat")))
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        if (StringUtils.isDouble(getIntent().getStringExtra("lon")))
            lont = Double.parseDouble(getIntent().getStringExtra("lon"));
        if (StringUtils.isDouble(getIntent().getStringExtra("gdlon")))
            gdlon = Double.parseDouble(getIntent().getStringExtra("gdlon"));
        if (StringUtils.isDouble(getIntent().getStringExtra("gdlat")))
            gdlat = Double.parseDouble(getIntent().getStringExtra("gdlat"));

        placeTitle = getIntent().getStringExtra("title");
        placeAddressStr = getIntent().getStringExtra("placeAddress");
        placeAddress = new LatLng(gdlat, gdlon);
        //  myAddress = new LatLng(SPUtils.getLatitude(), SPUtils.getLongitude());

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(placeAddress);
        markerOption.title(placeTitle).snippet(placeAddressStr);
        markerOption.perspective(true);
        markerOption.draggable(true);
        markerOption.visible(true);
        markerOption.setFlat(true);
//        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_1));
        aMap.addMarker(markerOption);
    }

    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        finish();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                SPUtils.setLatitude(amapLocation.getLatitude());
                SPUtils.setLongitude(amapLocation.getLongitude());
                if (myAddress == null) {
                    myAddress = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(placeAddress).include(myAddress).build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                }
            } else if (amapLocation.getErrorCode() == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) {
                LogUtils.e("定位失败,权限被拒绝");
                aMap.setOnMapLoadedListener(this);
                aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            }
        }
    }

    private void setUpMap() {

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //   jump2BaiduMap();
                // jump2TencentMap();
                // jump2Amap();
                return false;
            }
        });
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            mLocationOption.setInterval(15 * 1000);
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        MobclickAgent.onPageStart("PlaceMapActivity");
        MobclickAgent.onResume(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        MobclickAgent.onPageEnd("PlaceMapActivity");
        MobclickAgent.onPause(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(placeAddress).build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    private void jump2BaiduMap() {
        Intent intent;
        if (AppUtil.isAppInstall("com.baidu.BaiduMap")) {
            try {
                intent = Intent.getIntent("intent://map/direction?origin=latlng:" + lat + "," + lont + "|name:我的位置&destination=" + placeTitle + "&mode=walking®ion=&src=厦门趣处网络科技有限公司|趣处#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请检查是否已安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    private void jump2TencentMap() {
        Intent tencentMap;
        if (AppUtil.isAppInstall("com.tencent.map")) {
            try {
                tencentMap = Intent.getIntent("qqmap://map/routeplan?type=walk&from=我的位置&fromcoord=" + SPUtils.getLatitude() + "," + SPUtils.getLongitude() + "&to=" + placeTitle + "&tocoord=" + gdlat + "," + gdlon);
                startActivity(tencentMap);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            //  Toast.makeText(this, "请检查是否已安装腾讯地图", Toast.LENGTH_SHORT).show();
            tencentMap = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apis.map.qq.com/uri/v1/routeplan?type=bus&from=我的位置&fromcoord=" + SPUtils.getLatitude() + "," + SPUtils.getLongitude() + "&to=" + placeTitle + "&tocoord=" + gdlat + "," + gdlon + "&policy=1&referer=趣处"));
            tencentMap.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            startActivity(tencentMap);
        }
    }

    private void jump2Amap() {
        if (AppUtil.isAppInstall("com.autonavi.minimap")) {
            try {
                Intent amapIntent = Intent.getIntent("androidamap://route?sourceApplication=趣处&slat=" + SPUtils.getLatitude() + "&slon=" + SPUtils.getLongitude() + "&sname=我的位置&dlat=" + gdlat + "&dlon=" + gdlon + "&dname=" + placeTitle + "&dev=0&m=0&t=4");
                amapIntent.addCategory("android.intent.category.DEFAULT");
                amapIntent.setAction("android.intent.action.VIEW");
                amapIntent.setPackage("com.autonavi.minimap");
                startActivity(amapIntent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请检查是否已安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }
}