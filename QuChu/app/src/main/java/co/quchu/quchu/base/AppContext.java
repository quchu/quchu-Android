package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.utils.SPUtils;


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
//        OkHttpClient okHttpClient = new OkHttpClient();
//        OkHttpDownloader downloader = new OkHttpDownloader(okHttpClient);
//         picasso = new Picasso.Builder(this).downloader(downloader).build();
        SPUtils.setUserToken(mContext, "2f354b95eff0457ec179d52e9c3aea8b49306bd7");
        user = new Gson().fromJson(SPUtils.getUserInfo(mContext), UserInfoModel.class);
    }




}
