package co.quchu.quchu.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.quchu.quchu.base.AppLocationListener;
import com.android.volley.VolleyError;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.NavigateSelectedDialogFg;
import co.quchu.quchu.model.NearbyMapModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
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


public class PlaceMapActivity extends BaseBehaviorActivity implements View.OnClickListener, AppLocationListener.LocationListener {



    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_navigation);
    }

    private BDLocationListener mListener;
    private BaiduMap aMap;
    private LocationClient mlocationClient;
    double lat = 0, lont = 0, gdlon = 0, gdlat = 0;
    private List<NearbyMapModel> mDataSet = new ArrayList<>();
    private AMapNearbyVPAdapter mAdapter;
    private String placeTitle, placeAddressStr = "";
    private LatLng placeAddress;
    private LatLng myAddress;
    private MapView mapView;
    private ViewPager mVPNearby;
    private BitmapDescriptor mMapPin,mMapPinBlue,mOverlayMyLocation;
    private NearbyMapModel mCurrentModel;
    private int mLastMarker = 0;



    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {

        ArrayMap<String,Object> data = new ArrayMap<>();
        data.put("pid",getIntent().getIntExtra("pid",-1));
        return data;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 110;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);
        mapView = (MapView) findViewById(R.id.place_map_mv);
        mVPNearby = (ViewPager) findViewById(R.id.vpNearby);
        View vReturn = findViewById(R.id.ivReturn);
        vReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mMapPin = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_yellow);
        mMapPinBlue = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_blue);
        mOverlayMyLocation = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_me);
        mapView.onCreate(getApplicationContext(),savedInstanceState);// 此方法必须重写
        ImageView currentPosition = (ImageView) findViewById(R.id.current_position);
        ImageView myPosition = (ImageView) findViewById(R.id.my_position);
        currentPosition.setOnClickListener(this);
        myPosition.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mapView.showZoomControls(false);
        initData();

        ZGEvent("趣处名称",placeTitle,"进入地图");

        //getEnhancedToolbar().getTitleTv().setText(R.string.nearby_quchu);

        mAdapter = new AMapNearbyVPAdapter(mDataSet, new AMapNearbyVPAdapter.OnMapItemClickListener() {
            @Override
            public void onItemClick(int position) {


                ArrayMap<String,Object> params = new ArrayMap<>();
                params.put("趣处名称",mDataSet.get(position).getName());
                params.put("入口名称",getPageNameCN());
                ZGEvent(params,"进入趣处详情页");

                Intent intent = new Intent(PlaceMapActivity.this,QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM,QuchuDetailsActivity.FROM_TYPE_MAP);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID,mDataSet.get(position).getPid());
                startActivity(intent);
            }
        });
        mVPNearby.setAdapter(mAdapter);
        mVPNearby.setClipToPadding(false);
        mVPNearby.setPadding(40, 0, 40, 20);
        mVPNearby.setPageMargin(20);
        mVPNearby.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                UMEvent("maplist_c");
                if (mDataSet.get(position).getLatitude()==null||mDataSet.get(position).getLongitude()==null){
                    return;
                }
                LatLng latLng = new LatLng(Double.valueOf(mDataSet.get(position).getLatitude()), Double.valueOf(mDataSet.get(position).getLongitude()));

                MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                aMap.animateMapStatus(mMapStatusUpdate);
                mVPNearby.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mMarks.get(position).showInfoWindow();
                        popUpWindow(position);
                        mMarks.get(position+1).setIcon(mMapPinBlue);
                        mMarks.get(position+1).setToTop();
                        if (mLastMarker<mMarks.size()&&mLastMarker>=0){
                            mMarks.get(mLastMarker).setIcon(mMapPin);
                        }
                        mLastMarker = position+1;
                    }
                }, 250l);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        NearbyPresenter.getMapNearbyData(this, SPUtils.getCityId(), "", lat, lont, new CommonListener<List<NearbyMapModel>>() {
            @Override
            public void successListener(List<NearbyMapModel> response) {
                mDataSet.addAll(response);
                for (int i = 0; i < mDataSet.size(); i++) {
                    if (mDataSet.get(i).getPid()==mCurrentModel.getPid()){
                        mDataSet.remove(i);
                    }
                }
                mAdapter.notifyDataSetChanged();
                initMarks();
                DialogUtil.dismissProgess();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgess();

            }
        });


    }

    private List<Marker> mMarks = new ArrayList<>();

    public void popNavigation(final String latt, final String lontt,final String gdlatt,final String gdlontt){
        NavigateSelectedDialogFg navigateDialogFg = NavigateSelectedDialogFg.newInstance();
        navigateDialogFg.setNavigateClickListener(new NavigateSelectedDialogFg.NavigateClickListener() {
            @Override
            public void choiceGd() {
                jump2Amap(latt,lontt,gdlatt,gdlontt);
            }

            @Override
            public void choiceBd() {
                jump2BaiduMap(latt,lontt,gdlatt,gdlontt);
            }

            @Override
            public void choiceTx() {
                jump2TencentMap(latt,lontt,gdlatt,gdlontt);
            }
        });
        navigateDialogFg.show(getFragmentManager(), "navigate");
    }
    private void initMarks() {
        for (int i = 0; i < mDataSet.size(); i++) {

            LatLng latLng = new LatLng(0,0);
            if (null!=mDataSet.get(i).getLatitude() && null!= mDataSet.get(i).getLongitude()){
                latLng = new LatLng(Double.valueOf(mDataSet.get(i).getLatitude()), Double.valueOf(mDataSet.get(i).getLongitude()));
            }

            Bundle bundle = new Bundle();
            bundle.putInt("obj",i);
            OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .title(mDataSet.get(i).getAddress())
                .anchor(.5f,.5f)
                .perspective(true)
                .draggable(false)
                .period(50)
                .extraInfo(bundle)
                .icon(mMapPin);
            //在地图上添加Marker，并显示
            Marker marker = (Marker) aMap.addOverlay(option);


            if (i==0){
                popUpWindow(0);
                marker.setIcon(mMapPinBlue);
                marker.setToTop();
                mLastMarker = i;
            }

            mMarks.add(marker);
        }

        OverlayOptions optionMyLoc = new MarkerOptions()
            .position(new LatLng(SPUtils.getLatitude(),SPUtils.getLongitude()))
            .anchor(.5f,.5f)
            .perspective(true)
            .draggable(false)
            .period(50)
            .icon(mOverlayMyLocation);
        aMap.addOverlay(optionMyLoc);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void initData() {

        //aMap.moveCamera(CameraUpdateFactory.zoomTo(.5f));
        if (StringUtils.isDouble(getIntent().getStringExtra("lat")))
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        if (StringUtils.isDouble(getIntent().getStringExtra("lon")))
            lont = Double.parseDouble(getIntent().getStringExtra("lon"));
        if (StringUtils.isDouble(getIntent().getStringExtra("gdlon")))
            gdlon = Double.parseDouble(getIntent().getStringExtra("gdlon"));
        if (StringUtils.isDouble(getIntent().getStringExtra("gdlat")))
            gdlat = Double.parseDouble(getIntent().getStringExtra("gdlat"));

        mCurrentModel = (NearbyMapModel) getIntent().getSerializableExtra("entity");

        placeTitle = getIntent().getStringExtra("title");
        placeAddressStr = getIntent().getStringExtra("placeAddress");
        placeAddress = new LatLng(lat, lont);
        //  myAddress = new LatLng(SPUtils.getLatitude(), SPUtils.getLongitude());



        OverlayOptions option = new MarkerOptions()
            .position(placeAddress)
            .title(placeAddressStr)
            .perspective(true)
            .draggable(false)
            .flat(true)
            .visible(true)
            .icon(mMapPin);

        Marker marker = (Marker) aMap.addOverlay(option);

        MapStatus mapStatus = new MapStatus.Builder().target(placeAddress).zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);


        //MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
        //        fromResource(R.mipmap.ic_map_pin_me));
        //
        //aMap.setMyLocationStyle(myLocationStyle);
        aMap.animateMapStatus(mMapStatusUpdate);
        //marker.showInfoWindow();
        Bundle bundle = new Bundle();
        bundle.putInt("obj",0);
        marker.setExtraInfo(bundle);
        mMarks.add(marker);

        mDataSet.add(mCurrentModel);
        popUpWindow(0);
    }

    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.current_position:
                if(mVPNearby.getChildCount()>0){
                    if (mVPNearby.getCurrentItem()==0){
                        checkPermission();
                        mVPNearby.setCurrentItem(0);
                        MapStatus mapStatus = new MapStatus.Builder().target(new LatLng(lat,lont)).zoom(aMap.getMapStatus().zoom).build();
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                        aMap.animateMapStatus(mMapStatusUpdate);
                    }else{
                        mVPNearby.setCurrentItem(0);
                    }
                }

                break;
            case R.id.my_position:
                //如果没有定位权限,提醒
                checkPermission();
                //有权限
                MapStatus mapStatusCurrent = new MapStatus.Builder().target(new LatLng(SPUtils.getLatitude(),SPUtils.getLongitude())).zoom(aMap.getMapStatus().zoom).build();
                MapStatusUpdate mMapStatusUpdateCurrent = MapStatusUpdateFactory.newMapStatus(mapStatusCurrent);
                aMap.animateMapStatus(mMapStatusUpdateCurrent);

                break;
            default:
                finish();
        }

    }

    private void checkPermission() {
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
    }

    private int mLocationUpdateCounter = 0;



    private void popUpWindow(final int index){
        if(mMarks.size()<=index || mDataSet.size()<=index){
            return;
        }

        LatLng latLng = new LatLng(Double.valueOf(mDataSet.get(index).getLatitude()), Double.valueOf(mDataSet.get(index).getLongitude()));

        View infoWindow = getLayoutInflater().inflate(R.layout.cp_amap_infowindow, null);
        TextView textView = (TextView) infoWindow.findViewById(R.id.tvAddress);

        if (null != mMarks.get(index).getExtraInfo()) {
            NearbyMapModel nearbyMapModel = mDataSet.get(index);
            textView.setText(nearbyMapModel.getAddress());
        }

        infoWindow.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                NearbyMapModel nearbyMapModel = mDataSet.get(index);
                popNavigation(nearbyMapModel.getLatitude(),nearbyMapModel.getLongitude(),nearbyMapModel.getGdLatitude(),nearbyMapModel.getGdLongitude());
            }
        });

        InfoWindow mInfoWindow = new InfoWindow(infoWindow, latLng, -56);
        aMap.showInfoWindow(mInfoWindow);
    }

    private void setUpMap() {

//        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.setLocationSource(this);// 设置定位监听
////        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (null!=marker.getExtraInfo()){
                    int index = (int) marker.getExtraInfo().get("obj");
                    popUpWindow(index);
                    mVPNearby.setCurrentItem(index);
                }
                return false;
            }
        });


    }

    //@Override
    //public void activate(OnLocationChangedListener onLocationChangedListener) {
    //    mListener = onLocationChangedListener;
    //    if (mlocationClient == null) {
    //        mlocationClient = new LocationClient(this);
    //        LocationClientOption mLocationOption = new LocationClientOption();
    //        //设置定位监听
    //        mlocationClient.registerLocationListener(this);
    //        //设置为高精度定位模式
    //        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
    //        //设置定位参数
    //        mlocationClient.setLocOption(mLocationOption);
    //        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
    //        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
    //        mLocationOption.setScanSpan(5 * 1000);
    //        // 在定位结束后，在合适的生命周期调用onDestroy()方法
    //        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    //        mlocationClient.start();
    //    }
    //}
    //
    //@Override
    //public void deactivate() {
    //    mListener = null;
    //    if (mlocationClient != null) {
    //        mlocationClient.unRegisterLocationListener(this);
    //        mlocationClient.stop();
    //    }
    //    mlocationClient = null;
    //}

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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
            mlocationClient.stop();
        }
    }


    private void jump2BaiduMap(String latt,String lontt,String gdLatt,String gdLont) {
        Intent intent;
        if (AppUtil.isAppInstall(PlaceMapActivity.this,"com.baidu.BaiduMap")) {
            try {
                intent = Intent.getIntent("intent://map/direction?origin=latlng:" + latt + "," + lontt + "|name:我的位置&destination=" + placeTitle + "&mode=walking®ion=&src=厦门趣处网络科技有限公司|趣处#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请检查是否已安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    private void jump2TencentMap(String latt,String lontt,String gdLatt,String gdLont) {
        Intent tencentMap;
        if (AppUtil.isAppInstall(PlaceMapActivity.this,"com.tencent.map")) {
            try {
                tencentMap = Intent.getIntent("qqmap://map/routeplan?type=walk&from=我的位置&fromcoord=" + SPUtils.getLatitude() + "," + SPUtils.getLongitude() + "&to=" + placeTitle + "&tocoord=" + gdLatt + "," + gdLont);
                startActivity(tencentMap);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            //  Toast.makeText(this, "请检查是否已安装腾讯地图", Toast.LENGTH_SHORT).show();
            tencentMap = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apis.map.qq.com/uri/v1/routeplan?type=bus&from=我的位置&fromcoord=" + SPUtils.getLatitude() + "," + SPUtils.getLongitude() + "&to=" + placeTitle + "&tocoord=" + gdLatt + "," + gdLont + "&policy=1&referer=趣处"));
            tencentMap.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            startActivity(tencentMap);
        }
    }

    private void jump2Amap(String latt,String lontt,String gdLatt,String gdLont) {
        if (AppUtil.isAppInstall(PlaceMapActivity.this,"com.autonavi.minimap")) {
            try {
                Intent amapIntent = Intent.getIntent("androidamap://route?sourceApplication=趣处&slat=" + SPUtils.getLatitude() + "&slon=" + SPUtils.getLongitude() + "&sname=我的位置&dlat=" + gdLatt + "&dlon=" + gdLont + "&dname=" + placeTitle + "&dev=0&m=0&t=4");
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (event.getFlag()== EventFlags.EVENT_GOTO_HOME_PAGE) {
            finish();
        }else if(event.getFlag()== EventFlags.EVENT_FINISH_MAP){
            finish();
        }
    }


    //@Override
    //public View getInfoContents(Marker marker) {
    //    return null;
    //}

    private boolean mFirstLocate = true;

    @Override public void location(BDLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            SPUtils.setLatitude(amapLocation.getLatitude());
            SPUtils.setLongitude(amapLocation.getLongitude());
            //            LatLngBounds bounds = new LatLngBounds.Builder().include(placeAddress).include(myAddress).build();
            //            if (amapLocation.getErrorCode() == 0) {
            mListener.onReceiveLocation(amapLocation);// 显示系统小蓝点

            if (mLocationUpdateCounter<=1 && mFirstLocate){
                MapStatus mapStatus = new MapStatus.Builder().target(placeAddress).zoom(50).build();
                aMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            }
            mLocationUpdateCounter +=1;
            mFirstLocate = false;

        }
    }
}