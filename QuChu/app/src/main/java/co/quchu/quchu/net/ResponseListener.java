package co.quchu.quchu.net;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by linqipeng on 2015/2/25.
 * email:437943145@qq.com
 * desc: 网络响应处理类
 */
public interface ResponseListener<T> extends Response.ErrorListener {
    /**
     * 只有网路哟异常回调这个
     *
     * @param error
     */
    @Override
    void onErrorResponse(@Nullable VolleyError error);

    /**
     * 网络请求回调
     *
     * @param response  data结果,只有在result为true的时候才会解析这个节点的数据,否则为空
     * @param errorCode result为false的时候才会解析这个数据
     * @param msg       result为false的时候才会解析这个数据
     */
    void onResponse(T response, boolean result, String errorCode, @Nullable String msg);
}
