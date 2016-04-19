package co.quchu.quchu.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.NavigateSelectedDialogFg;
import co.quchu.quchu.model.NearbyMapModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.AMapNearbyVPAdapter;

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
    private OnLocationChangedListener mListener;
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    double lat = 0, lont = 0, gdlon = 0, gdlat = 0;
    private List<NearbyMapModel> mDataSet = new ArrayList<>();
    private AMapNearbyVPAdapter mAdapter;
    private String placeTitle, placeAddressStr = "";
    private LatLng placeAddress;
    private LatLng myAddress;
    private MapView mapView;
    private ViewPager mVPNearby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);
        mapView = (MapView) findViewById(R.id.place_map_mv);
        mVPNearby = (ViewPager) findViewById(R.id.vpNearby);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        ImageView currentPosition = (ImageView) findViewById(R.id.current_position);
        currentPosition.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        initData();

        getEnhancedToolbar().getRightTv().setText(R.string.navigation);
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
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

        mAdapter = new AMapNearbyVPAdapter(mDataSet, new AMapNearbyVPAdapter.OnMapItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
        mVPNearby.setAdapter(mAdapter);
        mVPNearby.setClipToPadding(false);
        mVPNearby.setPadding(40,0,40,20);
        mVPNearby.setPageMargin(20);
        mVPNearby.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                LatLng latLng = new LatLng(Double.valueOf(mDataSet.get(position).getLatitude()),Double.valueOf(mDataSet.get(position).getLongitude()));
                CameraUpdate s = CameraUpdateFactory.changeLatLng(latLng);

                aMap.animateCamera(s);
                mVPNearby.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMarks.get(position).showInfoWindow();
                    }
                },250l);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        DialogUtil.showProgess(this,R.string.loading_dialog_text);
        NearbyPresenter.getMapNearbyData(this, SPUtils.getCityId(), "", SPUtils.getLatitude(), SPUtils.getLongitude(), new CommonListener<List<NearbyMapModel>>() {
            @Override
            public void successListener(List<NearbyMapModel> response) {
                mDataSet.addAll(response);
                mAdapter.notifyDataSetChanged();
                initMarks();
                DialogUtil.dismissProgess();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgess();

            }
        });

//        for (int i = 0; i < 5; i++) {
//
//            List<TagsModel> tags = new ArrayList<TagsModel>();
//            for (int j = 0; j < 5; j++) {
//                TagsModel model = new TagsModel();
//                switch (j){
//                    case 0:
//                        model.setZh("清新");
//                        break;
//                    case 1:
//                        model.setZh("书店");
//                        break;
//                    case 2:
//                        model.setZh("逛街");
//                        break;
//                    case 3:
//                        model.setZh("文艺");
//                        break;
//                    case 4:
//                        model.setZh("神秘");
//                        break;
//                }
//                tags.add(model);
//            }
//            NearbyMapModel nearbyMapModel = new NearbyMapModel();
//            nearbyMapModel.setCover("http://img3.imgtn.bdimg.com/it/u=1604706481,3962528280&fm=21&gp=0.jpg");
//            nearbyMapModel.setName("Name"+i);
//            nearbyMapModel.setAddress("厦门,中山路/轮渡,32 How"+i);
//            nearbyMapModel.setLongitude((118.09427777263+(i/1000d))+"");
//            nearbyMapModel.setLatitude((24.466288171628+(i/1000d))+"");
//            nearbyMapModel.setTags(tags);
//            mDataSet.add(nearbyMapModel);
//            DialogUtil.dismissProgess();
//        }
//        mAdapter.notifyDataSetChanged();


    }

    private List<Marker> mMarks = new ArrayList<>();
    private void initMarks() {
        mMarks.clear();
        for (int i = 0; i < mDataSet.size(); i++) {
            float distance =AMapUtils.calculateLineDistance(new LatLng(gdlat, gdlon),
                    new LatLng(Double.valueOf(mDataSet.get(i).getLatitude()), Double.valueOf(mDataSet.get(i).getLongitude())));
            String strDistance = "距离当前趣处："+new DecimalFormat("#.##").format(((distance / 1000) / 100f) * 100) + "km";
            LatLng latLng = new LatLng(Double.valueOf(mDataSet.get(i).getLatitude()),Double.valueOf(mDataSet.get(i).getLongitude()));
            mMarks.add(aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLng).title(mDataSet.get(i).getName())
                    .snippet(strDistance)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_target))
                    .perspective(true).draggable(false).period(50)));
        }
    }

    @Override
    protected int activitySetup() {
        return 0;
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
//        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.avatar_1));
        aMap.addMarker(markerOption);
    }

    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.current_position:
                //如果没有定位权限,提醒
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_DENIED) {
                        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.dialog_two_button);
                        b.setTitle("趣处没有权限获取您的位置!");
                        b.setMessage("请在设置/应用管理/趣处/权限管理允许获取位置权限");
                        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        b.setCancelable(false);
                        b.show();
                        return;
                    }
                }
                //有权限
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(SPUtils.getLatitude(), SPUtils.getLongitude()), mapView.getMap().getCameraPosition().zoom, 0, 0));

                mapView.getMap().animateCamera(update);

                break;
            default:
                finish();
        }

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


        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LogUtils.e("点击事件");
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
            mlocationClient.unRegisterLocationListener(this);
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
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(placeAddress).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                placeAddress, 14, 0, 0));

        aMap.animateCamera(update);

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