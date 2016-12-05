package co.quchu.quchu.baselist.Base;

import android.app.Application;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 16/11/30.
 */

public class AppContext extends Application {

  private static OkHttpClient mOkHttp;
  private static Retrofit mRetrofit;
  /**
   * 开发环境
   */
  private static final String HOST_SIT = "http://sit.quchu.co/app-main-service/";
  //public static SharedPreferences mSharedPreferencesCompat;
  //
  //public SharedPreferences getSPC(Context context){
  //  if (null==mSharedPreferencesCompat){
  //    mSharedPreferencesCompat = context.getSharedPreferences(getPackageName(),MODE_PRIVATE);
  //  }
  //  return mSharedPreferencesCompat;
  //}

  private static OkHttpClient getHttpClient(){
    if (null==mOkHttp){

      Map<String, String> map = new HashMap<>();
      map.put("Charset", "UTF-8");
      map.put("Content-Type", "application/json;charset=UTF-8");
      map.put("quchu-token", "b61d7d2ca0671ab87447c1fcec5a090fad484038");
      map.put("quchuVersion", "Android_V" + "2.0.0");


      mOkHttp = new OkHttpClient.Builder()
          .connectTimeout(3000l, TimeUnit.MILLISECONDS)
          .readTimeout(5000, TimeUnit.MILLISECONDS)
          .writeTimeout(3000, TimeUnit.MILLISECONDS)
          .retryOnConnectionFailure(true)
          .addInterceptor(new BaseInterceptor(map))
          .build();
    }
    return mOkHttp;
  }

  public static Retrofit getRetrofit(){
    if (null==mRetrofit){
      mRetrofit = new Retrofit.Builder()
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl(HOST_SIT)
          .client(getHttpClient())
          .build();
    }
    return mRetrofit;
  }


  @Override public void onCreate() {
    super.onCreate();

  }
}
