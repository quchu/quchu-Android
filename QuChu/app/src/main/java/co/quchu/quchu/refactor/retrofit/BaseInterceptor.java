package co.quchu.quchu.refactor.retrofit;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截器
 * <p>
 * Created by mwb on 16/9/25.
 */
public class BaseInterceptor implements Interceptor {

  private String TAG = "BaseInterceptor";

  private Map<String, String> mMap;

  public BaseInterceptor(Map<String, String> map) {
    if (map == null) {
      map = new HashMap<>();
    }
    this.mMap = map;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
//    Request.Builder builder = chain.request().newBuilder();
//    if (mMap != null && mMap.size() > 0) {
//      Set<String> keys = mMap.keySet();
//      for (String headerKey : keys) {
//        builder.addHeader(headerKey, mMap.get(headerKey)).build();
//      }
//    }
//    return chain.proceed(builder.build());

    Map<String, String> cacheMap = new HashMap<>();

    for (String key : mMap.keySet()) {
      if (!TextUtils.isEmpty(mMap.get(key))) {
        cacheMap.put(key, mMap.get(key));
      }
    }

    Request.Builder builder = chain.request().newBuilder();
    StringBuffer sb = new StringBuffer();
    for (String key : cacheMap.keySet()) {
      sb.append(key + " : " + cacheMap.get(key) + " ,");
      builder.addHeader(key, cacheMap.get(key)).build();
    }
    LogUtils.d(TAG, "headers : " + sb.toString());

    return chain.proceed(builder.build());
  }
}
