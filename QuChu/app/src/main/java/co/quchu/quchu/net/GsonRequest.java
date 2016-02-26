package co.quchu.quchu.net;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.NetErrorDialogUtil;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;

/**
 * Created by linqipeng on 2016/2/25.
 * email:437943145@qq.com
 * desc: 子线程解析 更好的性能的请求 支持application/json 和urlEncoding两种方式的请求
 */
public class GsonRequest<T> extends Request<T> {
    private Class<T> entity;
    private Type type;
    private String msg;
    private String exception;
    private static Gson gson = new Gson();
    private ResponseListener<T> listener;
    private Map<String, String> params;
    private String paramsJson;
    private boolean result;
    private static RequestQueue queue;

    static {
        queue = Volley.newRequestQueue(AppContext.mContext);
    }

    public GsonRequest(String url, @NonNull Class<T> entity, ResponseListener<T> listener) {
        super(Method.GET, url, listener);
        this.listener = listener;
        this.entity = entity;
    }

    public GsonRequest(int method, String url, Map<String, String> params, @NonNull Class<T> entity, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.entity = entity;
        this.params = params;
    }

    public GsonRequest(int method, String url, Map<String, String> params, @NonNull Type type, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.type = type;
        this.params = params;
    }

    public GsonRequest(int method, String url, @NonNull String json, @NonNull Class<T> entity, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.entity = entity;
        this.paramsJson = json;
    }

    public GsonRequest(int method, String url, @NonNull String json, @NonNull Type type, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.type = type;
        this.paramsJson = json;
    }

    @Override
    protected final Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {

        T t;
        if (networkResponse.statusCode == 200) {
            try {
                String json = new String(networkResponse.data, "utf-8");
                JSONObject jsonObject = new JSONObject(json);
                result = jsonObject.getBoolean("result");
                if (result) {
                    //结果正确
                    String data = jsonObject.getString("data");
                    if (entity != null || type != null) {
                        t = gson.fromJson(data, entity != null ? entity : type);
                    } else {
                        t = (T) data;
                    }
                    LogUtils.e("网络返回result为true");
                    return Response.success(t, HttpHeaderParser.parseCacheHeaders(networkResponse));
                } else {
                    msg = jsonObject.getString("msg");
                    exception = jsonObject.getString("exception");
                    LogUtils.e("网络返回Result为false:" + json);
                }
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
        }
        LogUtils.e("网络异常");
        return Response.error(new NetworkError());
    }

    @Override
    protected void deliverResponse(T t) {
        if (!result && !TextUtils.isEmpty(msg)) {
            switch (msg) {
                case "1077":
                    ActManager.getAppManager().finishActivitiesAndKeepLastOne();
                    ActManager.getAppManager().currentActivity().startActivity(new Intent(ActManager.getAppManager().currentActivity(), UserLoginActivity.class));
                    ActManager.getAppManager().finishLastOne();
                    SPUtils.clearUserinfo(AppContext.mContext);
                    AppContext.user = null;
                    LogUtils.e("登录令牌错误");
                    break;
                case "1078":
                    LogUtils.e("空值异常");
                    break;
                case "1079":
                    LogUtils.e("登录令牌失效");
                    break;
                case "1080":
                    ActManager.getAppManager().currentActivity().startActivity(new Intent(ActManager.getAppManager().currentActivity(), UserLoginActivity.class));
                    LogUtils.e("登录令牌失效,有游客没有权限进行相关操作");
                    break;
            }
        } else {
            listener.onResponse(t, result, exception, msg);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Charset", "UTF-8");
        if (!StringUtils.isEmpty(SPUtils.getUserInfo(AppContext.mContext))) {
            headers.put("quchu-token", SPUtils.getUserToken(AppContext.mContext));
        }
        return headers;
    }

    public void start(Context context, Object tag) {
        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
            NetErrorDialogUtil.showProgess(context, "请检查网络");
            NetErrorActionUtil.UpdateRecommendData();
        } else {
            if (tag != null) {
                setTag(tag);
            }
            setRetryPolicy(new DefaultRetryPolicy(6 * 1000, 1, 1.0f));
            queue.add(this);
            queue.start();
        }
    }

    @Override
    public String getBodyContentType() {
        if (TextUtils.isEmpty(paramsJson)) {
            return super.getBodyContentType();
        } else {
            return "application/json;charset=UTF-8";
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (TextUtils.isEmpty(paramsJson)) {
            return super.getBody();
        } else {
            return paramsJson.getBytes();
        }
    }
}
