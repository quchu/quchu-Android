package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.gson.Gson;

import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;


/**
 *
 */
public class AppContext extends Application {
    public static Context mContext;
    public static UserInfoModel user;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Fresco.initialize(mContext);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .build();
        if (!StringUtils.isEmpty(SPUtils.getUserInfo(this))) {
            if (user == null){
                LogUtils.json(SPUtils.getUserInfo(this));
                user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
            }
        }
    }


}
