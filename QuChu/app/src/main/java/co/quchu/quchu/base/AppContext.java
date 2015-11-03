package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


/**
 *
 */
public class AppContext extends Application {
    public static Context mContext;
public static Picasso picasso;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpDownloader downloader = new OkHttpDownloader(okHttpClient);
         picasso = new Picasso.Builder(this).downloader(downloader).build();
    }



}
