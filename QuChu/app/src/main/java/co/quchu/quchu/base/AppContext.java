package co.quchu.quchu.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.UserBehaviorPresentor;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

public class AppContext extends Application {
    public static Context mContext;
    public static UserInfoModel user;//用户信息

    // 屏幕宽度
    public static float Width = 0;
    // 屏幕高度
    public static float Height = 0;
    public static RecommendModel selectedPlace; //推荐分类 数据源
    public static boolean dCardListNeedUpdate = false;

    public static String token = "";
    public static long mLastLocatingTimeStamp = -1;


    private RefWatcher refWatcher;
    public static PackageInfo packageInfo;


    private BroadcastReceiver mPowerKeyReceiver = null;

    private void registBroadcastReceiver() {
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mPowerKeyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();

                if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
                    UserBehaviorPresentor.insertBehavior(getApplicationContext(), 0, "screenOff", "", System.currentTimeMillis());
                } else if (strAction.equals(Intent.ACTION_SCREEN_ON)) {
                    UserBehaviorPresentor.insertBehavior(getApplicationContext(), 0, "screenOn", "", System.currentTimeMillis());
                }
            }
        };

        getApplicationContext().registerReceiver(mPowerKeyReceiver, theFilter);
    }

    public static RefWatcher getRefWatcher(Context context) {
        AppContext application = (AppContext) context.getApplicationContext();
        return application.refWatcher;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registBroadcastReceiver();
        refWatcher = LeakCanary.install(this);
        mContext = getApplicationContext();
        token = SPUtils.getUserToken(getApplicationContext());
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String processName = getProcessName(this, android.os.Process.myPid());
        if(processName.endsWith("pushservice")) {
            PushManager.getInstance().initialize(this.getApplicationContext());
        }


            //禁用页面自动统计
        MobclickAgent.openActivityDurationTrack(false);
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setBitmapsConfig(Bitmap.Config.RGB_565)
//                .setWebpSupportEnabled(true)
                .build();
        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
        if (!StringUtils.isEmpty(SPUtils.getUserInfo(this))) {
            LogUtils.json(SPUtils.getUserInfo(this));
            user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);

        }
        initWidths();


        //TODO revert sensitive to 100
        if (UserBehaviorPresentor.getDataSize(getApplicationContext())>=1){
            UserBehaviorPresentor.postBehaviors(getApplicationContext(), UserBehaviorPresentor.getBehaviors(getApplicationContext()), new CommonListener() {
                @Override
                public void successListener(Object response) {
                    UserBehaviorPresentor.insertBehavior(getApplicationContext(), 0, "startup", "", System.currentTimeMillis());
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    UserBehaviorPresentor.insertBehavior(getApplicationContext(), 0, "startup", "", System.currentTimeMillis());
                }
            });
        }

    }


    public void initWidths() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        //   System.exit(0);
    }

    //声明AMapLocationClient类对象
    private static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private static AMapLocationListener mLocationListener = null;
    //声明mLocationOption对象
    private static AMapLocationClientOption mLocationOption = null;

    public static void initLocation() {
        //初始化定位
        if (!AppUtil.isOpen(mContext))
            AppUtil.openGPS(mContext);
        mLocationClient = new AMapLocationClient(mContext);
        mLocationListener = new AppLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                super.onLocationChanged(amapLocation);
                mLastLocatingTimeStamp = System.currentTimeMillis();
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        if (SPUtils.getLongitude() == 0 && SPUtils.getLatitude() == 0) {
            mLocationOption.setInterval(5 * 1000);
        } else {
            mLocationOption.setInterval(3600 * 1000);
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    public static void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        mLocationListener = null;
    }

}
