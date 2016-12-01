package co.quchu.quchu.refactor.retrofit;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.refactor.QuChuApiService;
import co.quchu.quchu.refactor.rxjava.BaseSubscriber;
import co.quchu.quchu.refactor.rxjava.HttpResponseFunc;
import co.quchu.quchu.refactor.rxjava.HttpResultFunc;
import co.quchu.quchu.utils.SPUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Retrofit 配置
 * <p>
 * Created by mwb on 16/11/25.
 */
public class AppClient {

  private final Retrofit mRetrofit;
  private final QuChuApiService mService;
  private static final AppClient mAppClient = new AppClient();

  /**
   * 正式环境
   */
  private static final String HOST_RELEASE = "http://www.quchu.co/app-main-service/";
  /**
   * 集成测试环境
   */
  private static final String HOST_UAT = "http://uat.quchu.co/app-main-service/";
  /**
   * 开发环境
   */
  private static final String HOST_SIT = "http://sit.quchu.co/app-main-service/";

  private AppClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    if (BuildConfig.DEBUG) {
      //添加日志打印
      builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    //添加 header
    Map<String, String> map = new HashMap<>();
    map.put("Charset", "UTF-8");
    map.put("Content-Type", "application/json;charset=UTF-8");
    map.put("quchu-token", SPUtils.getUserToken(AppContext.mContext));
    map.put("quchuVersion", "Android_V" + AppContext.packageInfo.versionName);
    builder.addInterceptor(new BaseInterceptor(map));

    builder.connectTimeout(15, TimeUnit.SECONDS);//连接超时时间
    builder.retryOnConnectionFailure(true);//连接失败重试
    OkHttpClient okHttpClient = builder.build();

    //变量名使用驼峰式，e.g: user_name --> userName
    Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    String API_HOST = BuildConfig.API_SERVER == 0 ? HOST_RELEASE
        : BuildConfig.API_SERVER == 1 ? HOST_UAT : HOST_SIT;

    mRetrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(API_HOST)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    mService = mRetrofit.create(QuChuApiService.class);
  }

  public static AppClient getInstance() {
    return mAppClient;
  }

  public QuChuApiService getService() {
    return mService;
  }

  public <T> T create(final Class<T> service) {
    return mRetrofit.create(service);
  }

  public <T> Subscription packageObservable(Observable observable, BaseSubscriber<T> subscriber) {
    return observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(new HttpResultFunc<T>())
        .onErrorResumeNext(new HttpResponseFunc<T>())
        .subscribe(subscriber);
  }
}
