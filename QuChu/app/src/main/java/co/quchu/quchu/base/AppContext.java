package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

import co.quchu.quchu.model.PlacePostCardModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;


/**
 *
 */
public class AppContext extends Application {
    public static Context mContext;
    public static UserInfoModel user;//用户信息

    public static PlacePostCardModel ppcModel;//趣处明信片信息 用户返回后刷新
    // 屏幕宽度
    public static float Width = 0;
    // 屏幕高度
    public static float Height = 0;
    public static ArrayList<Object> gatherList;
    // public static ArrayList<RecommendModel> dCardList; //推荐分类 数据源
    public static RecommendModel selectedPlace; //推荐分类 数据源
    public static boolean dCardListNeedUpdate = false;
    public static int dCardListRemoveIndex = -1;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LogUtils.json("userinfo=" + SPUtils.getUserInfo(mContext));
        LogUtils.json("userToken=" + SPUtils.getUserToken(mContext));
        Fresco.initialize(mContext);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .build();
        if (!StringUtils.isEmpty(SPUtils.getUserInfo(this))) {
            if (user == null) {
                LogUtils.json(SPUtils.getUserInfo(this));
                user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
            }
        }
        gatherList = new ArrayList<>();
        initImageLoader();
        initWidths();
    }


    public void initWidths() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
                .memoryCacheSize(50 * 1024 * 1024) //设置内存缓存的大小
                .diskCacheFileCount(200)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        //    SPUtils.initGuideIndex();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        System.exit(0);
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
        mLocationListener = new AppLocationListener();
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
        mLocationOption.setInterval(80 * 1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public static void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }
}
