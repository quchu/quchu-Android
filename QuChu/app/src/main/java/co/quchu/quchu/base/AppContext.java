package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

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
    public static ArrayList<RecommendModel> dCardList; //推荐分类 数据源
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
                gatherList = new ArrayList<>();
            }
        }
        initImageLoader();
        initWidths();
       /* DateUtils.getUTCTime();
        LogUtils.json(new Gson().toJson(new GatherModel()));*/
        //  dCardList = new ArrayList<>();
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

}
