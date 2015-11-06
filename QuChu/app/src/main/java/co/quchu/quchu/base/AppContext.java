package co.quchu.quchu.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
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
        Fresco.initialize(mContext);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        OkHttpDownloader downloader = new OkHttpDownloader(okHttpClient);
//         picasso = new Picasso.Builder(this).downloader(downloader).build();
    }



}
