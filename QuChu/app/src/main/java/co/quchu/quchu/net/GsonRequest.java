package co.quchu.quchu.net;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.LoginActivity;

/**
 * Created by linqipeng on 2016/2/25.
 * email:437943145@qq.com
 * desc: 子线程解析 更好的性能的请求 支持application/json 和urlEncoding两种方式的请求
 * 封装了进度条提示
 */
public class GsonRequest<T> extends Request<T> {
    private Class<T> entity;
    private Type type;
    private String msg;
    private String errorCode;
    private ResponseListener<T> listener;
    private Map<String, String> params;
    private String paramsJson;
    private boolean result;
    public static RequestQueue queue;
    private Context context;
    private boolean showDialog;


    static {
        queue = Volley.newRequestQueue(AppContext.mContext);

    }

    public GsonRequest(String url, @NonNull Class<T> entity, ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.listener = listener;
        this.entity = entity;
    }

    public GsonRequest(String url, ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.listener = listener;
    }

    public GsonRequest(String url, @NonNull Type type, ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.listener = listener;
        this.type = type;
    }

    public GsonRequest(String url, @NonNull Class<T> entity, Map<String, String> params, ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.listener = listener;
        this.params = params;
        this.entity = entity;
    }

    public GsonRequest(String url, @NonNull Type type, Map<String, String> params, ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.listener = listener;
        this.params = params;
        this.type = type;
    }

    public GsonRequest(int method, String url, Map<String, String> params, @NonNull Class<T> entity, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.entity = entity;
        this.params = params;
    }

    public GsonRequest(int method, String url, @NonNull Type type, ResponseListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.type = type;
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
        if (params != null) {
            StringBuilder encodedParams = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(entry.getKey());
                encodedParams.append('=');
                encodedParams.append(entry.getValue());
                encodedParams.append("\n");
            }
            LogUtils.e("请求参数" + encodedParams.toString());
        }

        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        T t;
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

            LogUtils.e("原始数据为" + json);
            JSONObject jsonObject = new JSONObject(json);
            result = jsonObject.getBoolean("result");

            String data = jsonObject.getString("data");
            if (entity != null || type != null) {
                Gson gson = new Gson();
                t = gson.fromJson(data, entity != null ? entity : type);
            } else {
                t = (T) data;
            }

            msg = jsonObject.getString("msg");
            if (jsonObject.has("errorCode")) {
                errorCode = jsonObject.getString("errorCode");
            }
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("网络异常");
        closeDialog();
        return Response.error(new ParseError());
    }

    private void closeDialog() {
        if (showDialog) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtil.dismissProgressDirectly();
                }
            });
        }
    }


    @Override
    protected void deliverResponse(T t) {
        if (showDialog) {
            DialogUtil.dismissProgressDirectly();
        }
        if (!TextUtils.isEmpty(msg)) {
            switch (msg) {
                case "1077":
                    SPUtils.clearUserinfo(context);
                    reLogin();
                    LogUtils.e("登录令牌错误");
                    return;
                case "1078":
                    reLogin();
                    LogUtils.e("空值异常");
                    return;
                case "1079":
                    reLogin();
                    LogUtils.e("登录令牌失效");
                    return;
                case "1080":
                    reLogin();
                    LogUtils.e("登录令牌失效,游客没有权限进行相关操作");
                    return;
            }
        }
        if (listener != null)
            listener.onResponse(t, result, errorCode, msg);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Charset", "UTF-8");

        if (!TextUtils.isEmpty(AppContext.token)) {
            headers.put("quchu-token", AppContext.token);
//            headers.put("quchu-token", "c20d5d429dc966063da21f2a35da984cb1e6f025");
            LogUtils.e("quchu-token:" + AppContext.token);
        }
        headers.put("quchuVersion", "Android_V" + AppContext.packageInfo.versionName);
        return headers;
    }

    public void start(Context context, @NonNull Object tag) {
        this.context = context;
        setTag(tag);
        setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(this);
    }

    public void start(Context context) {
        this.context = context;
        setTag(context.getClass().getSimpleName());
        setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(this);
    }

    public void start(Context context, Object tag, boolean showDialog) {
        this.context = context;
        this.showDialog = showDialog;
        setTag(tag);
        setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(this);
        if (showDialog)
            DialogUtil.showProgress(context, "加载中~~");
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

    /**
     * 跳转主页重新登录,清除栈
     */
    private void reLogin() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("登录过期了,点击重新登录");
        dialog.setCancelable(false);
        dialog.setNegativeButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                SPUtils.clearUserinfo(context);
                AppContext.user = null;
                AppContext.token = null;
            }
        });
        try {
            dialog.show();

        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

}
